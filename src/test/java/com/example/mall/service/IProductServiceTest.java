package com.example.mall.service;

import com.example.mall.MallApplicationTests;
import com.example.mall.enums.ResponseEnum;
import com.example.mall.vo.ProductDetailVo;
import com.example.mall.vo.ResponseVo;
import com.github.pagehelper.PageInfo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IProductServiceTest extends MallApplicationTests {

    @Autowired
    private IProductService productService;

    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = productService.list(null, 4, 1);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void detail(){
        ResponseVo<ProductDetailVo> responseVo = productService.detail(26);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }
}