<html>
<head>
   <meta charset="utf-8">
    <title>卖家后端管理系统</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
    <#--<link rel="stylesheet" href="/css/style.css" media="all">-->

</head>

<body>
<div id="wrapper" class="toggled">
    <#--边栏sidebar-->
    <#--<#include "../common/nav.ftl">-->

    <#--主要内容-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row-fluid">

                <div class="span12">
                    <table class="table table-hover table-bordered">
                        <thead>
                        <tr>
                            <th>订单ID</th>
                            <th>姓名</th>
                            <th>手机号码</th>
                            <th>地址</th>
                            <th>金额</th>
                            <th>订单状态</th>
                            <th>支付状态</th>
                            <th>创建时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                           <#list orderDTOPage.content as orderDTO>
                               <tr>
                                   <td>${orderDTO.orderId}</td>
                                   <td>${orderDTO.buyerName}</td>
                                   <td>${orderDTO.buyerPhone}</td>
                                   <td>${orderDTO.buyerAddress}</td>
                                   <td>${orderDTO.orderAmount}</td>
                                   <td>${orderDTO.getOrderStatusEnum().message}</td>
                                   <td>${orderDTO.getPayStatusEnum().message}</td>
                                   <td></td>
                                   <td> <a href="http://127.0.0.1:8080/sell/seller/order/detail?orderId=${orderDTO.orderId}">详情</a></td>
                                   <td>
                                       <#if orderDTO.getOrderStatusEnum().getMessage()=="新订单">
                                       <a href="http://127.0.0.1:8080/sell/seller/order/cancel?orderId=${orderDTO.orderId}">取消</a>
                                       </#if>
                                   </td>
                               </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            <#--分页-->
                <div class="span12">
                    <div class="pagination  pagination-right">
                        <ul>
                            <#if currentPage lte 1>
                                <li class="disabled"><a href="#">上一页</a></li>
                            <#else>
                            <li><a href="http://127.0.0.1:8080/sell/seller/order/list?page=${currentPage-1}&size=${size}">上一页</a></li>
                            </#if>
                            <#list 1..orderDTOPage.getTotalPages() as index>
                                <#if currentPage==index>
                                    <li class="disabled"><a href="#">${index}</a></li>
                                <#else>
                                    <li><a href="http://127.0.0.1:8080/sell/seller/order/list?page=${index}&size=${size}">${index}</a></li>
                                </#if>
                            </#list>
                            <#if currentPage gte orderDTOPage.getTotalPages()>
                                <li class="disabled"><a href="#">下一页</a></li>
                            <#else>
                            <li><a href="http://127.0.0.1:8080/sell/seller/order/list?page=${currentPage+1}&size=${size}">下一页</a></li>
                            </#if>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>