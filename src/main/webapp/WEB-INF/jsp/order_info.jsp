<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="currentUser" value="${currentUser}"/>
<c:set var="passenger" value="${passenger}"/>
<c:set var="driver" value="${driver}"/>
<c:set var="order" value="${order}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Welcome</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

    <c:if test="${order != null}">

        <form id="gomenuForm" method="GET" action="${contextPath}/welcome">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <h4><a onclick="document.forms['gomenuForm'].submit()">Return to menu</a></h4>

        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <h4><a onclick="document.forms['logoutForm'].submit()">Logout</a></h4>

        <div class="container">
            <h2>Order Info</h2>
            <table class="table table-hover">
                <tbody>
                <tr>
                    <td>ID</td>
                    <td>${order.id}</td>
                </tr>
                <tr>
                    <td>Time create</td>
                    <td>${order.timeCreate}</td>
                </tr>
                <c:if test="${order.timeTaken != null}">
                    <tr>
                        <td>Time taken</td>
                        <td>${order.timeTaken}</td>
                    </tr>
                </c:if>
                <c:if test="${order.timeCancelled != null}">
                    <tr>
                        <td>Time cancelled</td>
                        <td>${order.timeCancelled}</td>
                    </tr>
                </c:if>
                <c:if test="${order.timeClosed != null}">
                    <tr>
                        <td>Time cancelled</td>
                        <td>${order.timeClosed}</td>
                    </tr>
                </c:if>
                <tr>
                    <td>Status</td>
                    <td>${order.orderStatus}</td>
                </tr>
                <tr>
                    <td>Address FROM</td>
                    <td>${order.from.country}, ${order.from.city},
                        ${order.from.street}, ${order.from.houseNum}</td>
                </tr>
                <tr>
                    <td>Address TO</td>
                    <td>${order.to.country}, ${order.to.city},
                        ${order.to.street}, ${order.to.houseNum}</td>
                </tr>
                <tr>
                    <td>Passenger</td>
                    <td>${passenger.username}, ${passenger.userphone}</td>
                </tr>
                <c:if test="${driver != null}">
                    <tr>
                        <td>Driver</td>
                        <td>${driver.username}, ${driver.userphone}</td>
                    </tr>
                </c:if>
                <tr>
                    <td>Distance, km</td>
                    <td>${order.distance}</td>
                </tr>
                <tr>
                    <td>Price, uah</td>
                    <td>${order.price}</td>
                </tr>
                </tbody>
            </table>
        </div>

        <h4 class="text-left"><a href="${contextPath}/order/make?id=${order.id}">
            Copy order as new</a></h4>
        <h4 class="text-left"><a href="${contextPath}/history">
            Show history</a></h4>
        <h4 class="text-left"><a href="${contextPath}/order/get?id=${currentUser.lastOrderId}">
            Show last order</a></h4>
        <h4 class="text-left"><a href="${contextPath}/map">
            Show map</a></h4>

    </c:if>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
