package com.example.mall.service.impl;

import com.example.mall.dao.OrderItemMapper;
import com.example.mall.dao.OrderMapper;
import com.example.mall.dao.ProductMapper;
import com.example.mall.dao.ShippingMapper;
import com.example.mall.enums.OrderStatusEnum;
import com.example.mall.enums.PaymentTypeEnum;
import com.example.mall.enums.ProductStatusEnum;
import com.example.mall.enums.ResponseEnum;
import com.example.mall.pojo.*;
import com.example.mall.service.ICartService;
import com.example.mall.service.IOrderService;
import com.example.mall.vo.OrderItemVo;
import com.example.mall.vo.OrderVo;
import com.example.mall.vo.ResponseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    // 事务控制
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        // 收货地址校验（总之要查出来的）
        Shipping shipping = shippingMapper.selecetByUidAndShippingId(uid, shippingId);
        if (shipping == null) {
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        //获取购物车，校验（是否有商品、库存）
        List<Cart> cartList = cartService.listForCart(uid).stream().filter(Cart::getProductSelected).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }
        //检查数据库中是否有该商品，库存是否充足
        //获取cartList里的productIds
        Set<Integer> productIdSet = cartList.stream().map(Cart::getProductId).collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        // 根据id 查商品内容
        Map<Integer, Product> map = productList.stream().collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();
        for (Cart cart : cartList) {
            // 根据productId来查询数据库
            Product product = map.get(cart.getProductId());
            //是否有该商品
            if (product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST, "商品不存在, productId = " + cart.getProductId());
            }
            // 商品的上下架状态
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())) {
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, "商品不是在售状态" + product.getName());
            }

            //判断库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR, "库存不正确." + product.getName());
            }

            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);

            orderItemList.add(orderItem);

            //减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0) {
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }
        // 计算总价，只计算选中的商品

        // 生成订单，入库： order 和 order_item, 事务来控制（Transactional）
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);

        int rowForOrder = orderMapper.insertSelective(order);
        if (rowForOrder <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        /**
         * 重点,批量写入
         */
        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowForOrderItem <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        //更新购物车（选中的商品），删除redis中的记录
        // Redis 有事务（打包命令），不能回滚，所以得保证上述操作已经成功！
        for (Cart cart : cartList) {
            cartService.delete(uid, cart.getProductId());
        }

        // 构造orderVo， 返回给前端
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);


        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);

        Set<Long> orderNoSet = orderList.stream().map(Order::getOrderNo).collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        //查收货地址，每一个订单的收货地址可以不一样
        Set<Integer> shippingIdSet = orderList.stream().map(Order::getShippingId).collect(Collectors.toSet());
        /**
         *  将order 下的 orderList 对应起来
         *  key 和 value 对比71-72
         */
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream().collect(Collectors.groupingBy(OrderItem::getOrderNo));

        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);

        //收货地址
        Map<Integer, Shipping> shippingMap = shippingList.stream().collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        List<OrderVo> orderVoList = new ArrayList<>();
        //要获取order 下面对应的orderItem 才可以
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order, orderItemMap.get(order.getOrderNo()),       // 可以更近一步做个判断，看看可否得到orderNo
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }

        PageInfo pageInfo = new PageInfo(orderList);

        pageInfo.setList(orderVoList);

        return ResponseVo.success(pageInfo);
    }

    @Override
    //订单详情查询，通过orderNo
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }

        Set<Long> orderNoSet = new HashSet();
        orderNoSet.add(order.getOrderNo());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        //查地址
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());

        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        //首先校验该订单是否属于这个用户
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        /** 逻辑判断，什么情况下可以取消
         *  未付款的 情况下
         */
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.CANCELED.getCode());

        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);

        if (row <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        return ResponseVo.success();
    }

    @Override
    public void paid(Long orderNo) {
        //更新订单的状态
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单：" + orderNo);
        }
        // 只有未付款 订单才能变成已付款
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc() + "订单：" + orderNo);
        }

        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());

        int row = orderMapper.updateByPrimaryKeySelective(order);

        if (row <= 0) {
            throw new RuntimeException("将订单更新为已支付状态失败，id:" + orderNo);
        }

    }


    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);


        List<OrderItemVo> OrderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());

        orderVo.setOrderItemVoList(OrderItemVoList);

        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }
        return orderVo;
    }

    private Order buildOrder(Integer uid, Long orderNo, Integer shippingId, List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        return order;
    }

    /**
     * 生成订单号，使用时间戳+随机数
     * 如果是企业级： 分布式唯一id/主建 ;
     *
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
}
