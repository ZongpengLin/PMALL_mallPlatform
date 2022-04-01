package com.example.mall.service;


import com.example.mall.MallApplicationTests;
import com.example.mall.enums.ResponseEnum;
import com.example.mall.form.CartAddForm;
import com.example.mall.vo.CartVo;
import com.example.mall.vo.OrderVo;
import com.example.mall.vo.ResponseVo;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional    //在测试中加上这个注释是为了测试完进行回滚，不会留下脏数据
public class IOrderServiceTest extends MallApplicationTests {


    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICartService cartService;

    private Integer uid = 1;

    private Integer shippingId = 8;

    private Integer productId = 26;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    //订单生成前先加入购物车
    @Before
    public void before() {
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(uid, form);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void createTest(){
        ResponseVo<OrderVo> responseVo = create();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    private ResponseVo<OrderVo> create() {
        ResponseVo<OrderVo> responseVo = orderService.create(uid, shippingId);
        log.info("result={}", gson.toJson(responseVo));
        return responseVo;
    }
    @Test
    public void list() {
        // order 表和 orderItem表 的 order_no 必须要对应得上
        ResponseVo<PageInfo> responseVo = orderService.list(uid, 1,2);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void detail() {
        ResponseVo<OrderVo> vo = create();
        ResponseVo<OrderVo> responseVo = orderService.detail(uid,vo.getData().getOrderNo());
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());

    }

    @Test
    public void cancel() {
        ResponseVo<OrderVo> vo = create();
        ResponseVo responseVo = orderService.cancel(uid,vo.getData().getOrderNo());
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());

    }
}