<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${currentUser}"/>
<c:set var="message" value="${message}"/>

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

    <c:if test="${pageContext.request.userPrincipal.name != null}">

        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <c:if test="${message != null}">
            <div class="alert alert-success">
                <h4><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${message}</strong></h4>
            </div>
        </c:if>

        <h4><a onclick="document.forms['logoutForm'].submit()">Logout</a></h4>

        <div class="container">
            <h2>User Info</h2>
            <table class="table table-hover">
                <tbody>
                <tr>
                    <td>ID</td>
                    <td>${user.id}</td>
                </tr>
                <c:if test="${user.homeAddress != null}">
                    <tr>
                        <td>Identifier</td>
                        <td>Passenger</td>
                    </tr>
                    <tr>
                        <td>Home address</td>
                        <td>${user.homeAddress.country}, ${user.homeAddress.city},
                                ${user.homeAddress.street}, ${user.homeAddress.houseNum}</td>
                    </tr>
                </c:if>
                <c:if test="${user.car != null}">
                    <tr>
                        <td>Identifier</td>
                        <td>Driver</td>
                    </tr>
                    <tr>
                        <td>Car</td>
                        <td>${user.car.type}, ${user.car.model}, ${user.car.number}</td>
                    </tr>
                </c:if>

                <tr>
                    <td>Phone</td>
                    <td>${user.userphone}</td>
                </tr>
                <tr>
                    <td>Name</td>
                    <td>${user.username}</td>
                </tr>

                </tbody>
            </table>
        </div>

        <c:if test="${user.homeAddress != null}">
            <h4 class="text-left"><a href="${contextPath}/order/make">Make order</a></h4>
        </c:if>

        <c:if test="${user.car != null}">
            <h4 class="text-left"><a href="${contextPath}/order/get/new">Find passenger</a></h4>
        </c:if>

        <h4 class="text-left"><a href="${contextPath}/order/get/all">Show history</a></h4>
        <h4 class="text-left"><a href="${contextPath}/order/get?id=${user.lastOrderId}">Show last order</a></h4>
        <h4 class="text-left"><a href="${contextPath}/map">Show map</a></h4>

    </c:if>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
