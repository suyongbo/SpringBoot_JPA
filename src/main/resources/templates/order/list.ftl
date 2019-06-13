<html>
<head>
    <meta charset="utf-8">
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <title>卖家端订单列表</title>
</head>

<body>
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

</body>
</html>