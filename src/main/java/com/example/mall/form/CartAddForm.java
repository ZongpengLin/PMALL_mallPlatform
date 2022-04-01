package com.example.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加商品，表单统一验证
 */
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;

    private Boolean selected = true;
}
