<html>
<head>
    <title></title>
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
                           <td>详情</td>
                           <td>取消</td>
                       </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>