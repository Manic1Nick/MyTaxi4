<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="list" value="${listOrders}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>History page</title>

    <%--<link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">--%>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/css/dataTables.bootstrap.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/jquery.dataTables.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/datatables/1.10.12/js/dataTables.bootstrap.min.js"></script>


    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

        <table id="example" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
            <thead>
            <tr>
                <th>User ID</th>
                <th>Order ID</th>
                <th>Status</th>
                <th>Time create</th>
                <th>Time taken</th>
                <th>Time cancelled</th>
                <th>Time closed</th>
                <th>Address FROM</th>
                <th>Address TO</th>
                <th>Distance, km</th>
                <th>Price, uah</th>
                <th>Driver ID</th>
                <th>Show</th>
                <th>Make new</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${list}" var="order">
                <tr>
                    <td><c:out value="${order.idPassenger}" /></td>
                    <td><c:out value="${order.id}" /></td>
                    <td><c:out value="${order.orderStatus}" /></td>
                    <td><c:out value="${order.timeCreate}" /></td>
                    <td><c:out value="${order.timeTaken}" /></td>
                    <td><c:out value="${order.timeCancelled}" /></td>
                    <td><c:out value="${order.timeClosed}" /></td>
                    <td><c:out value="${order.from.country},
                                    ${order.from.city},
                                    ${order.from.street},
                                    ${order.from.houseNum}" /></td>
                    <td><c:out value="${order.to.country},
                                    ${order.to.city},
                                    ${order.to.street},
                                    ${order.to.houseNum}" /></td>
                    <td><c:out value="${order.distance}" /></td>
                    <td><c:out value="${order.price}" /></td>
                    <td><c:out value="${order.idDriver}" /></td>
                    <td>
                        <a href="${contextPath}/order/get?id=${order.id}">INFO</a>
                    </td>
                    <td>
                        <a href="${contextPath}/order/make?id=${order.id}">COPY</a>
                    </td>
                </tr>
            </c:forEach>

           </tbody>
        </table>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>

</body>
</html>
