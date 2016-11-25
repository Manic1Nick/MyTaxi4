<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Create a passenger</title>

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

    <form id="gomenuForm" method="GET" action="${contextPath}/welcome">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <form id="logoutForm" method="POST" action="${contextPath}/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    <h4><a onclick="document.forms['logoutForm'].submit()">Logout</a> |
        <a onclick="document.forms['gomenuForm'].submit()">Return to menu</a></h4>

    <form:form method="POST" modelAttribute="userForm" class="form-signin">
        <h2 class="form-signin-heading">Create your account</h2>
        <spring:bind path="userphone">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="userphone" class="form-control" placeholder="Userphone"
                            autofocus="true"/>
                <form:errors path="userphone"/>
            </div>
        </spring:bind>

        <spring:bind path="password">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="password" path="password" class="form-control"
                            placeholder="Password"/>
                <form:errors path="password"/>
            </div>
        </spring:bind>

        <spring:bind path="passwordConfirm">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="password" path="passwordConfirm" class="form-control"
                            placeholder="Confirm your password"/>
                <form:errors path="passwordConfirm"/>
            </div>
        </spring:bind>

        <spring:bind path="username">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="username" class="form-control" placeholder="Username"
                            autofocus="true"/>
                <form:errors path="username"/>
            </div>
        </spring:bind>

        <spring:bind path="homeAddress.country">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="homeAddress.country" class="form-control" placeholder="Homecountry"
                            autofocus="true"/>
                <form:errors path="homeAddress.country"/>
            </div>
        </spring:bind>
        <spring:bind path="homeAddress.city">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="homeAddress.city" class="form-control" placeholder="Homecity"
                            autofocus="true"/>
                <form:errors path="homeAddress.city"/>
            </div>
        </spring:bind>
        <spring:bind path="homeAddress.street">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="homeAddress.street" class="form-control" placeholder="Homestreet"
                            autofocus="true"></form:input>
                <form:errors path="homeAddress.street"></form:errors>
            </div>
        </spring:bind>
        <spring:bind path="homeAddress.houseNum">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="homeAddress.houseNum" class="form-control" placeholder="Housenumber"
                            autofocus="true"/>
                <form:errors path="homeAddress.houseNum"/>
            </div>
        </spring:bind>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
    </form:form>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
