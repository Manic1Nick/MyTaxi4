<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="order" value="${orderForm}"/>
<c:set var="user" value="${currentUser}"/>
<c:set var="lastOrder" value="${lastOrder}"/>
<%--<c:set var="addressFrom" value="${addressFrom}"/>
<c:set var="addressTo" value="${addressTo}"/>--%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <title>Create new order</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<div class="container">

    <%--orderChange FROM--%>
    <script>
        function fromCurrentLocation() {
            document.getElementById("Fromcountry").value = "${user.currentAddress.country}";
            document.getElementById("Fromcity").value = "${user.currentAddress.city}";
            document.getElementById("Fromstreet").value = "${user.currentAddress.street}";
            document.getElementById("Fromnumber").value = "${user.currentAddress.houseNum}";
        }
    </script>
    <script>
        function fromHomeAddress() {
            document.getElementById("Fromcountry").value = "${user.homeAddress.country}";
            document.getElementById("Fromcity").value = "${user.homeAddress.city}";
            document.getElementById("Fromstreet").value = "${user.homeAddress.street}";
            document.getElementById("Fromnumber").value = "${user.homeAddress.houseNum}";
        }
    </script>
    <c:if test="${lastOrder.from != null}">
        <script>
            function fromLastOrder() {
                document.getElementById("Fromcountry").value = "${lastOrder.from.country}";
                document.getElementById("Fromcity").value = "${lastOrder.from.city}";
                document.getElementById("Fromstreet").value = "${lastOrder.from.street}";
                document.getElementById("Fromnumber").value = "${lastOrder.from.houseNum}";
            }
        </script>
    </c:if>

    <%--orderChange TO--%>
    <script>
        function toCurrentLocation() {
            document.getElementById("Tocountry").value = "${user.currentAddress.country}";
            document.getElementById("Tocity").value = "${user.currentAddress.city}";
            document.getElementById("Tostreet").value = "${user.currentAddress.street}";
            document.getElementById("Tonumber").value = "${user.currentAddress.houseNum}";
        }
    </script>
    <script>
        function toHomeAddress() {
            document.getElementById("Tocountry").value = "${user.homeAddress.country}";
            document.getElementById("Tocity").value = "${user.homeAddress.city}";
            document.getElementById("Tostreet").value = "${user.homeAddress.street}";
            document.getElementById("Tonumber").value = "${user.homeAddress.houseNum}";
        }
    </script>
    <c:if test="${lastOrder.to != null}">
        <script>
            function toLastOrder() {
                document.getElementById("Tocountry").value = "${lastOrder.to.country}";
                document.getElementById("Tocity").value = "${lastOrder.to.city}";
                document.getElementById("Tostreet").value = "${lastOrder.to.street}";
                document.getElementById("Tonumber").value = "${lastOrder.to.houseNum}";
            }
        </script>
    </c:if>

    <%--calculate order for alert--%>
    <script>
        $('#addresses').keyup(
                function(){
                    var from = $("#Fromcountry").val() + "," +
                            $("#Fromcity").val() + "," +
                            $("#Fromstreet").val() + "," +
                            $("#Fromnumber").val();
                    var to = $("#Tocountry").val() + "," +
                            $("#Tocity").val() + "," +
                            $("#Tostreet").val() + "," +
                            $("#Tonumber").val();
                    $("#calculateOrder").attr("href",
                            "${contextPath}/order/calculate?from=" + from + "&to=" + to);
                });
    </script>

    <%--MENU--%>
    <form id="gomenuForm" method="GET" action="${contextPath}/welcome">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <form id="logoutForm" method="POST" action="${contextPath}/logout">
       <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <h4><a onclick="document.forms['logoutForm'].submit()">Logout</a> |
        <a onclick="document.forms['gomenuForm'].submit()">Return to menu</a></h4>

    <%--ALERT calculate distance & price--%>
    <c:if test="${order.price > 0}">
        <div class="alert alert-warning alert-dismissable fade in">
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            <h4 class="text-center">Distance: <strong>${order.distance} km, </strong>
                Price: <strong>${order.price} uah</strong></h4>
        </div>
    </c:if>

    <%--FORM INPUT ADDRESSES--%>
    <form:form method="POST" modelAttribute="orderForm" class="form-signin" id="addresses">

        <%--Address FROM--%>
        <div class="dropdown">
            <button class="btn btn-primary dropdown-toggle"
                        type="button" data-toggle="dropdown">
                <h4>Enter address FROM<span class="caret"></span></h4>
            </button>
            <ul class="dropdown-menu">
                <h4><li><a onclick="fromCurrentLocation()">Use your current location</a></li></h4>
                <h4><li><a onclick="fromHomeAddress()">Use your home address</a></li></h4>
                <c:if test="${lastOrder != null}">
                    <h4><li><a onclick="fromLastOrder()">Use your last order</a></li></h4>
                </c:if>
            </ul>
        </div>

        <spring:bind path="from.country">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="from.country" class="form-control"
                            placeholder="Fromcountry" id="Fromcountry"
                            autofocus="true" value="${order.from.country}"/>
                <form:errors path="from.country"/>
            </div>
        </spring:bind>
        <spring:bind path="from.city">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="from.city" class="form-control"
                            placeholder="Fromcity" id="Fromcity"
                            autofocus="true" value="${order.from.city}"/>
                <form:errors path="from.city"/>
            </div>
        </spring:bind>
        <spring:bind path="from.street">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="from.street" class="form-control"
                            placeholder="Fromstreet" id="Fromstreet"
                            autofocus="true" value="${order.from.street}"/>
                <form:errors path="from.street"/>
            </div>
        </spring:bind>
        <spring:bind path="from.houseNum">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="from.houseNum" class="form-control"
                            placeholder="Fromnumber" id="Fromnumber"
                            autofocus="true" value="${order.from.houseNum}"/>
                <form:errors path="from.houseNum"/>
            </div>
        </spring:bind>

        <%--Address TO--%>
        <div class="dropdown">
            <button class="btn btn-primary dropdown-toggle"
                    type="button" data-toggle="dropdown">
                <h4>Enter address TO<span class="caret"></span></h4>
            </button>
            <ul class="dropdown-menu">
                <h4><li><a onclick="toCurrentLocation()">Use your current location</a></li></h4>
                <h4><li><a onclick="toHomeAddress()">Use your home address</a></li></h4>
                <c:if test="${lastOrder != null}">
                    <h4><li><a onclick="toLastOrder()">Use your last order</a></li></h4>
                </c:if>
            </ul>
        </div>

        <spring:bind path="to.country">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="to.country" class="form-control"
                            placeholder="Tocountry" id="Tocountry"
                            autofocus="true" value="${order.to.country}"/>
                <form:errors path="to.country"/>
            </div>
        </spring:bind>
        <spring:bind path="to.city">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="to.city" class="form-control"
                            placeholder="Tocity" id="Tocity"
                            autofocus="true" value="${order.to.city}"/>
                <form:errors path="to.city"/>
            </div>
        </spring:bind>
        <spring:bind path="to.street">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="to.street" class="form-control"
                            placeholder="Tostreet" id="Tostreet"
                            autofocus="true" value="${order.to.street}"/>
                <form:errors path="to.street"/>
            </div>
        </spring:bind>
        <spring:bind path="to.houseNum">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="to.houseNum" class="form-control"
                            placeholder="Tonumber" id="Tonumber"
                            autofocus="true" value="${order.to.houseNum}"/>
                <form:errors path="to.houseNum"/>
            </div>
        </spring:bind>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Make order</button>

    </form:form>

        <div style="text-align: center">
            <a href="${contextPath}/order/calculate" role="button"
               class="btn btn-warning active btn-lg" id="calculateOrder"
               style="color:white; width: 300px; margin: 0 auto;">Calculate order</a>
        </div>


</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
