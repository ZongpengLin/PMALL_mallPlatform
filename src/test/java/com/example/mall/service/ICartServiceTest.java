package com.example.mall.service;

import com.example.mall.MallApplicationTests;
import com.example.mall.enums.ResponseEnum;
import com.example.mall.form.CartAddForm;
import com.example.mall.form.CartUpdateForm;
import com.example.mall.vo.CartVo;
import com.example.mall.vo.ResponseVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ICartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    //序列化
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Integer productId = 26;

    private Integer uid = 1;

    @Before
    public void add() {
        log.info("[新增购物车...]");
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(1, form);
        log.info("list={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo<CartVo> list = cartService.list(uid);
        log.info("list={}", gson.toJson(list));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),list.getStatus());
    }

    //测试更新方法
    @Test
    public void update(){
        CartUpdateForm form = new CartUpdateForm();
        form.setQuantity(5);
        form.setSlected(false);
        ResponseVo<CartVo> responseVo = cartService.update(uid, productId, form);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    //测试删除方法
    @After
    public void delete() {
        log.info("[删除购物车...]");
        ResponseVo<CartVo> responseVo = cartService.delete(uid, productId);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
    @Test
    public void selectAll() {
        ResponseVo<CartVo> responseVo = cartService.selectAll(uid);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
    @Test
    public void unSelectAll() {
        ResponseVo<CartVo> responseVo = cartService.unSelectAll(uid);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
    @Test
    public void sum() {
        ResponseVo<Integer> responseVo = cartService.sum(uid);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}