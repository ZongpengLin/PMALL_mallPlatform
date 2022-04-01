package com.example.mall.service;

import com.example.mall.form.CartAddForm;
import com.example.mall.form.CartUpdateForm;
import com.example.mall.pojo.Cart;
import com.example.mall.vo.CartVo;
import com.example.mall.vo.ResponseVo;

import java.util.List;

public interface ICartService {

    ResponseVo<CartVo> add(Integer uid, CartAddForm cartAddForm);

    ResponseVo<CartVo> list(Integer uid);

    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);

    ResponseVo<CartVo> delete(Integer uid, Integer productId);

    ResponseVo<CartVo> selectAll(Integer uid);

    ResponseVo<CartVo> unSelectAll(Integer uid);
    // 获取购物车中商品数量总和
    ResponseVo<Integer> sum(Integer uid);
    //获取购物车
    List<Cart> listForCart(Integer uid) ;
}
