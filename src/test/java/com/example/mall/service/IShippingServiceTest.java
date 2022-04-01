package com.example.mall.service;

import com.example.mall.MallApplicationTests;
import com.example.mall.enums.ResponseEnum;
import com.example.mall.form.ShippingForm;
import com.example.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public class IShippingServiceTest extends MallApplicationTests {

    @Autowired
    private IShippingService shippingService;

    private Integer uid = 1;

    private ShippingForm form;

    private Integer shippingId;
    // 地址统一处理
    @Before
    public void before(){
        ShippingForm form = new ShippingForm();
        form.setReceiverName("宗鹏");
        form.setReceiverAddress("花园路");
        form.setReceiverCity("北京");
        form.setReceiverMobile("15600109366");
        form.setReceiverPhone("01020104");
        form.setReceiverProvince("北京");
        form.setReceiverDistrict("海淀区");
        form.setReceiverZip("100089");

        this.form = form;

        add();
    }

//    @Test
    public void add() {
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid, form);
        log.info("result={}",responseVo);
        this.shippingId = responseVo.getData().get("shippingId");
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @After
    public void delete() {
//        Integer shippingId = 7;
        ResponseVo responseVo = shippingService.delete(uid, shippingId);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void update() {
//        shippingId = 4;
        form.setReceiverDistrict("顺义区");
        ResponseVo responseVo = shippingService.update(uid, shippingId,form);
        log.info("result={}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo responseVo = shippingService.list(uid,1,10);
        log.info("result={}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}