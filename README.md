# PMALL_mallPlatform
PMALL系统的电商通用模块
> 基于当前主流的Java We技术，完成了用户模块、商品模块、购物车模块、收货地址模块、订单模块的开发。

## 各个子模块对应的API
分模块完成，返回的是Json格式：

### 用户
- 1.登录

**POST /user/login**

> request

Content-Type: application/json

```
{
	"username":"admin",
	"password":"admin",
}
```
> response

fail
```
{
    "status": 1,
    "msg": "密码错误"
}
```

success
```
{
    "status": 0,
    "data": {
        "id": 12,
        "username": "aaa",
        "email": "aaa@163.com",
        "phone": null,
        "role": 0,
        "createTime": 1479048325000,
        "updateTime": 1479048325000
    }
}
```

-----

- 2.注册
**POST /user/register**

> request

```
{
	"username":"admin",
	"password":"admin",
	"email":"admin@qq.com"
}
```


> response

success
```
{
    "status": 0,
    "msg": "校验成功"
}
```


fail
```
{
    "status": 2,
    "msg": "用户已存在"
}
```

-----

- 3.获取登录用户信息
**GET /user**

> request

```
无参数
```
> response

success
```
{
    "status": 0,
    "data": {
        "id": 12,
        "username": "aaa",
        "email": "aaa@163.com",
        "phone": null,
        "role": 0,
        "createTime": 1479048325000,
        "updateTime": 1479048325000
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,无法获取当前用户信息"
}

```

------

- 4.退出登录
**POST /user/logout

> request

```
无
```

> response

success

```
{
    "status": 0,
    "msg": "退出成功"
}
```

fail
```
{
    "status": -1,
    "msg": "服务端异常"
}
```

---

### 类目
- 所有类目

**GET /categories**

> request

无需登录

> response


success
```
{
    "status": 0,
    "data": [{
        "id": 100001,
        "parentId": 0,
        "name": "家用电器",
        "sortOrder": 1,
        "subCategories": [{
            "id": 100006,
            "parentId": 100001,
            "name": "冰箱",
            "sortOrder": 2,
            "subCategories": [{
                "id": 100040,
                "parentId": 100006,
                "name": "进口冰箱",
                "sortOrder": 1,
                "subCategories": []
            }]
        },  {
        "id": 100005,
        "parentId": 0,
        "name": "酒水饮料",
        "sortOrder": 1,
        "subCategories": [{
            "id": 100026,
            "parentId": 100005,
            "name": "白酒",
            "sortOrder": 1,
            "subCategories": []
        }, {
            "id": 100027,
            "parentId": 100005,
            "name": "红酒",
            "sortOrder": 1,
            "subCategories": []
        }]
    }]
}
```

-------

### 商品
- 1.商品列表
 **GET /products**

 > request

```
categoryId(非必传，子类目的商品也要查出来)
pageNum(default=1)
pageSize(default=10)

```

> response

success

```
{
    "status": 0,
    "data": {
        "pageNum": 1,
        "pageSize": 10,
        "size": 2,
        "orderBy": null,
        "startRow": 1,
        "endRow": 2,
        "total": 2,
        "pages": 1,
        "list": [
            {
                "id": 1,
                "categoryId": 3,
                "name": "iphone7",
                "subtitle": "双十一促销",
                "mainImage": "mainimage.jpg",
                "status":1,
                "price": 7199.22
            },
            {
                "id": 2,
                "categoryId": 2,
                "name": "oppo R8",
                "subtitle": "oppo促销进行中",
                "mainImage": "mainimage.jpg",
                "status":1,
                "price": 2999.11
            }
        ],
        "firstPage": 1,
        "prePage": 0,
        "nextPage": 0,
        "lastPage": 1,
        "isFirstPage": true,
        "isLastPage": true,
        "hasPreviousPage": false,
        "hasNextPage": false,
        "navigatePages": 8,
        "navigatepageNums": [
            1
        ]
    }
}
```

------

- 2.商品详情
 **GET /products/{productId}**

 **GET /products/{productId}

> request

```
productId
```

> response

success

```
{
  "status": 0,
  "data": {
    "id": 2,
    "categoryId": 2,
    "name": "oppo R8",
    "subtitle": "oppo促销进行中",
    "mainImage": "mainimage.jpg",
    "subImages": "[\"mmall/aa.jpg\",\"mmall/bb.jpg\",\"mmall/cc.jpg\",\"mmall/dd.jpg\",\"mmall/ee.jpg\"]",
    "detail": "richtext",
    "price": 2999.11,
    "stock": 71,
    "status": 1,
    "createTime": "2016-11-20 14:21:53",
    "updateTime": "2016-11-20 14:21:53"
  }
}

```

fail
```
{
    "status": 12,
    "msg": "该商品已下架或删除"
}
```

-----

### 收货地址
- 1.添加地址

** POST /shippings


> request

```
receiverName=宗鹏
receiverPhone=01020104
receiverMobile=15600109366
receiverProvince=北京
receiverCity=北京市
receiverDistrict=海淀区
receiverAddress=花园路
receiverZip=100089
```

> response

success

```
{
    "status": 0,
    "msg": "新建地址成功",
    "data": {
        "shippingId": 28
    }
}
```

fail
```
{
    "status": 1,
    "msg": "新建地址失败"
}
```

------

- 2.删除地址

**DELETE /shippings/{shippingId}

DELETE /shippings/28

> request

```
shippingId
```

> response

success

```
{
    "status": 0,
    "msg": "删除地址成功"
}
```

fail
```
{
    "status": 1,
    "msg": "删除地址失败"
}
```

------

- 3.更新地址

**PUT /shippings/{shippingId}

> request

```
receiverName=宗鹏
receiverPhone=01020104
receiverMobile=18688888888
receiverProvince=北京
receiverCity=北京市
receiverDistrict=顺义区
receiverAddress=柳家胡同
receiverZip=100089
```

> response

success

```
{
    "status": 0,
    "msg": "更新地址成功"
}
```

fail
```
{
    "status": 1,
    "msg": "更新地址失败"
}
```

------

- 4.地址列表
**GET /shippings**

> request

```
pageNum(默认1),pageSize(默认10)
```

> response

success

```
{
    "status": 0,
    "data": {
        "total": 2,
        "list": [
            {
                "id": 8,
                "userId": 28,
                "receiverName": "宗鹏",
                "receiverPhone": "01020104",
                "receiverMobile": "15600109366",
                "receiverProvince": "北京",
                "receiverCity": "北京",
                "receiverDistrict": "海淀区",
                "receiverAddress": "花园路",
                "receiverZip": "100089",
                "createTime": "2022-03-15T01:45:29.000+0000",
                "updateTime": "2022-04-01T01:11:01.000+0000"
            },
            {
                "id": 12,
                "userId": 28,
                "receiverName": "宗鹏",
                "receiverPhone": "01020104",
                "receiverMobile": "15600109366",
                "receiverProvince": "北京",
                "receiverCity": "北京",
                "receiverDistrict": "顺义区",
                "receiverAddress": "柳家胡同",
                "receiverZip": "100089",
                "createTime": "2000-01-22T14:26:25.000+0000",
                "updateTime": "2022-04-01T01:11:06.000+0000"
            }
        ],
        "pageNum": 1,
        "pageSize": 10,
        "size": 2,
        "startRow": 1,
        "endRow": 2,
        "pages": 1,
        "prePage": 0,
        "nextPage": 0,
        "isFirstPage": true,
        "isLastPage": true,
        "hasPreviousPage": false,
        "hasNextPage": false,
        "navigatePages": 8,
        "navigatepageNums": [
            1
        ],
        "navigateFirstPage": 1,
        "navigateLastPage": 1
    }
}
```

fail
```
{
    "status": 1,
    "msg": "请登录之后查询"
}
```

-----

### 购物车
- 1.购物车List列表

** GET /carts **

> request

```
无参数,需要登录状态
```

> response

success

```

{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "productId": 1,
                "quantity": 1,
                "productName": "iphone7",
                "productSubtitle": "双十一促销",
                "productMainImage": "mainimage.jpg",
                "productPrice": 7199.22,
                "productStatus": 1,
                "productTotalPrice": 7199.22,
                "productStock": 86,
                "productSelected": true,
            },
            {
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productSelected": false,
            }
        ],
        "selectedAll": false,
        "cartTotalPrice": 10198.33,
        "cartTotalQuantity": 2
    }
}

```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------


- 2.购物车添加商品

** POST /carts **

> request

```
productId
selected: true
```

`注意`数量不用传，添加商品永远是以1累加

> response

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "productId": 1,
                "quantity": 12,
                "productName": "iphone7",
                "productSubtitle": "双十一促销",
                "productMainImage": "mainimage.jpg",
                "productPrice": 7199.22,
                "productStatus": 1,
                "productTotalPrice": 86390.64,
                "productStock": 86,
                "productSelected": true
            },
            {
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productSelected": true
            }
        ],
        "selectedAll": true,
        "cartTotalPrice": 89389.75,
        "cartTotalQuantity": 13
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------

- 3.更新购物车

** PUT /carts/{productId} **

> request

```
quantity //非必填
selected: true //非必填
```

> response

响应同2

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "productId": 1,
                "quantity": 12,
                "productName": "iphone7",
                "productSubtitle": "双十一促销",
                "productMainImage": "mainimage.jpg",
                "productPrice": 7199.22,
                "productStatus": 1,
                "productTotalPrice": 86390.64,
                "productStock": 86,
                "productSelected": true
            },
            {
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productSelected": true,
            }
        ],
        "selectedAll": true,
        "cartTotalPrice": 89389.75,
        "cartTotalQuantity": 13
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------

- 4.移除购物车某个产品

** DELETE /carts/{productId} **

> request

```
productId
```

> response

success

```
{
    "status": 0,
    "data": {
        "cartProductVoList": [
            {
                "productId": 2,
                "quantity": 1,
                "productName": "oppo R8",
                "productSubtitle": "oppo促销进行中",
                "productMainImage": "mainimage.jpg",
                "productPrice": 2999.11,
                "productStatus": 1,
                "productTotalPrice": 2999.11,
                "productStock": 86,
                "productSelected": true
            }
        ],
        "selectedAll": true,
        "cartTotalPrice": 2999.11,
        "cartTotalQuantity": 1
    }
}
```

fail
```
{
    "status": 10,
    "msg": "用户未登录,请登录"
}
```


------

- 5.全选中

** PUT /carts/selectAll **

> request

```
无参数,需要登录状态
```

> response

success

同接口 获取购物车列表

------

- 6.全不选中

** PUT /carts/unSelectAll**

> request

```
无参数,需要登录状态
```

> response

success

同接口 获取购物车列表

------

- 7.获取购物中所有商品数量总和

** GET /carts/products/sum **

> request

```
无参数,需要登录状态
```

> response

```
{
    "status": 0,
    "data": 2
}
```

### 订单
- 1. 创建订单

** POST /orders**

> request

```
shippingId
```

> response

success

```
{
    "status": 0,
    "data": {
        "orderNo": 1291136461000,
        "payment": 2999.11,
        "paymentType": 1,
        "postage": 0,
        "status": 10,
        "paymentTime": null,
        "sendTime": null,
        "endTime": null,
        "closeTime": null,
        "createTime": 1291136461000,
        "orderItemVoList": [
            {
                "orderNo": 1291136461000,
                "productId": 2,
                "productName": "oppo R8",
                "productImage": "mainimage.jpg",
                "currentUnitPrice": 2999.11,
                "quantity": 1,
                "totalPrice": 2999.11,
                "createTime": null
            }
        ],
        "shippingId": 5,
        "shippingVo": {
                "id": 4,
                "userId": 28,
                "receiverName": "宗鹏",
                "receiverPhone": "0100204",
                "receiverMobile": "18688888888",
                "receiverProvince": "北京",
                "receiverCity": "北京市",
                "receiverDistrict": "海淀区",
                "receiverAddress": "花园路",
                "receiverZip": "100089",
                "createTime": 1485066385000,
                "updateTime": 1485066385000
            }
    }
}
```

fail
```
{
    "status": 1,
    "msg": "创建订单失败"
}
```

------

- 2.订单List

** GET /orders **

> request

```
pageSize(default=10)
pageNum(default=1)
```

订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭

> response

success

```
{
  "status": 0,
  "data": {
    "pageNum": 1,
    "pageSize": 3,
    "size": 3,
    "orderBy": null,
    "startRow": 1,
    "endRow": 3,
    "total": 16,
    "pages": 6,
    "list": [
      {
        "orderNo": 1291136461000,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2022-03-24 12:27:18",
        "sendTime": "2022-03-34 12:27:18",
        "endTime": "2022-03-24 12:27:18",
        "closeTime": "2022-03-24 12:27:18",
        "createTime": "2022-03-24 16:04:36",
        "orderItemVoList": [
          {
            "orderNo": 1291136461000,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2022-03-24 16:04:36"
          }
        ],
        "shippingId": 5,
        "receiverName": "宗鹏",
        "shippingVo": null
      },
      {
        "orderNo": 1291136461001,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2022-03-24 12:27:18",
        "sendTime": "2022-03-24 12:27:18",
        "endTime": "2022-03-24 12:27:18",
        "closeTime": "2022-03-24 12:27:18",
        "createTime": "2022-03-24 16:04:35",
        "orderItemVoList": [
          {
            "orderNo": 1291136461001,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2022-03-24 16:04:35"
          }
        ],
        "shippingId": 5,
        "receiverName": "廖师兄",
        "shippingVo": null
      },
      {
        "orderNo": 1291136461002,
        "payment": 2999.11,
        "paymentType": 1,
        "paymentTypeDesc": "在线支付",
        "postage": 0,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": "2022-03-24 12:27:18",
        "sendTime": "2022-03-24 12:27:18",
        "endTime": "2022-03-24 12:27:18",
        "closeTime": "2022-03-24 12:27:18",
        "createTime": "2022-03-24 16:04:35",
        "orderItemVoList": [
          {
            "orderNo": 1291136461002,
            "productId": 2,
            "productName": "oppo R8",
            "productImage": "mainimage.jpg",
            "currentUnitPrice": 2999.11,
            "quantity": 1,
            "totalPrice": 2999.11,
            "createTime": "2022-03-24 16:04:35"
          }
        ],
        "shippingId": 5,
        "receiverName": "廖师兄",
        "shippingVo": null
      }
    ],
    "firstPage": 1,
    "prePage": 0,
    "nextPage": 2,
    "lastPage": 6,
    "isFirstPage": true,
    "isLastPage": false,
    "hasPreviousPage": false,
    "hasNextPage": true,
    "navigatePages": 8,
    "navigatepageNums": [
      1,
      2,
      3,
      4,
      5,
      6
    ]
  }
}
```

fail
```
{
  "status": 10,
  "msg": "用户未登录,请登录"
}


或

{
  "status": 1,
  "msg": "没有权限"
}



```

------

- 3.订单详情

** GET /orders/{orderNo} **

> request

```
orderNo
```

> response

success

```
{
  "status": 0,
  "data": {
    "orderNo": 1291136461000,
    "payment": 30000.00,
    "paymentType": 1,
    "paymentTypeDesc": "在线支付",
    "postage": 0,
    "status": 10,
    "statusDesc": "未支付",
    "paymentTime": "",
    "sendTime": "",
    "endTime": "",
    "closeTime": "",
    "createTime": "2022-03-26 22:23:49",
    "orderItemVoList": [
      {
        "orderNo": 1291136461000,
        "productId": 1,
        "productName": "iphone7",
        "productImage": "mainimage.jpg",
        "currentUnitPrice": 10000.00,
        "quantity": 1,
        "totalPrice": 10000.00,
        "createTime": "2022-03-26 22:23:49"
      },
      {
        "orderNo": 1291136461000,
        "productId": 2,
        "productName": "oppo R8",
        "productImage": "mainimage.jpg",
        "currentUnitPrice": 20000.00,
        "quantity": 1,
        "totalPrice": 20000.00,
        "createTime": "2022-03-26 22:23:49"
      }
    ],
    "shippingId": 3,
    "receiverName": "宗鹏",
    "shippingVo": {
      "receiverName": "宗鹏",
      "receiverPhone": "010204",
      "receiverMobile": "186",
      "receiverProvince": "北京",
      "receiverCity": "北京",
      "receiverDistrict": "顺义区",
      "receiverAddress": "柳家胡同",
      "receiverZip": "100000"
    }
  }
}

```

fail
```
{
  "status": 1,
  "msg": "没有找到订单"
}
```

------

-  4.取消订单

** PUT /orders/{orderNo} **

> request

```
orderNo
```

> response

success

```
{
  "status": 0
}

```

fail
```
{
  "status": 1,
  "msg": "该用户没有此订单"
}

或
{
  "status": 1,
  "msg": "此订单已付款，无法被取消"
}
```

------
