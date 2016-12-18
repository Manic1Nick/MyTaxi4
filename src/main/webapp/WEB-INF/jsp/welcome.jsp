<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${currentUser}"/>
<c:set var="message" value="${message}"/>
<c:set var="errorMessage" value="${error}"/>

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

    <script>
        $(document).ready(function(){
            $('[data-toggle="tooltip"]').tooltip();
        });
    </script>

    <c:if test="${pageContext.request.userPrincipal.name != null}">

    <div id="menu" class="container">
        <%--ALERT MESSAGE--%>
        <c:if test="${message != null}">
            <div class="alert alert-success">
                <h4><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${message}</strong></h4>
            </div>
        </c:if>
        <c:if test="${errorMessage != null}">
            <div class="alert alert-danger">
                <h4><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${errorMessage}</strong></h4>
            </div>
        </c:if>

        <%--LOGOUT--%>
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <h4><a onclick="document.forms['logoutForm'].submit()">Logout</a> |

        <%--SHOW HISTORY--%>
            <a href="${contextPath}/order/get/all">Show history
                <span class="badge">${user.quantityOrders}</span></a> |

        <%--SHOW LAST ORDER--%>
            <c:if test="${user.lastOrderId != null}">
                <a href="${contextPath}/order/get?id=${user.lastOrderId}">Show last order</a> |
            </c:if>
            <c:if test="${user.lastOrderId == null}">
                <a class="text-left" data-toggle="tooltip" title="You don't have any orders!"
                    style="color:grey">Show last order</a> |
            </c:if>

        <%--MAKE ORDER / FIND PESSENGER--%>
            <c:if test="${user.active == false}">
                <c:if test="${user.homeAddress != null}">
                    <a href="${contextPath}/order/make">Make new order</a>
                </c:if>
                <c:if test="${user.car != null}">
                    <a href="${contextPath}/order/get/all-new">Find passenger</a>
                </c:if>
            </c:if>

            <c:if test="${user.active == true}">
                <c:if test="${user.homeAddress != null}">
                    <a class="text-left" data-toggle="tooltip" title="You have active orders now!"
                       style="color:grey">Make new order</a>
                </c:if>
                <c:if test="${user.car != null}">
                    <a class="text-left" data-toggle="tooltip" title="You have active orders now!"
                       style="color:grey">Find passenger</a>
                </c:if>
            </c:if>


        </h4>
    </div>

    <div id="userInfo" class="container">
        <h2>User Info</h2>

        <div id="text" class="col-md-6">
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
                        <td>${user.homeAddress.country},
                            ${user.homeAddress.city},
                            ${user.homeAddress.street},
                            ${user.homeAddress.houseNum}</td>
                    </tr>
                </c:if>

                <c:if test="${user.car != null}">
                    <tr>
                        <td>Identifier</td>
                        <td>Driver</td>
                    </tr>
                    <tr>
                        <td>Car</td>
                        <td>${user.car.type},
                            ${user.car.model},
                            ${user.car.number}</td>
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

        <div id="map" class="col-md-6" style="width: 500px; height: 500px"></div>
    </div>

    </c:if>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD-KmQUcMJUpRzjthK1CvNtmYw3mLf9vzs&callback=initMap"
        type="text/javascript"></script>

<%--user location--%>
<script type="text/javascript">
    if (navigator.geolocation) {

        navigator.geolocation.getCurrentPosition(function (p) {
            var LatLng = new google.maps.LatLng(p.coords.latitude, p.coords.longitude);
            var mapOptions = {
                center: LatLng,
                zoom: 13,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            var map = new google.maps.Map(document.getElementById("map"), mapOptions);
            var infoWindow = new google.maps.InfoWindow();
            var marker = new google.maps.Marker({
                position: LatLng,
                map: map,
                title: "<div style = 'height:60px;width:200px;color: #c7254e'>" +
                "<b>Your location:</b><br />" +
                "Latitude: " + p.coords.latitude + "<br />" +
                "Longitude: " + p.coords.longitude
            });
            google.maps.event.addListener(marker, "click", function (e) {
                infoWindow.setContent(marker.title);
                infoWindow.open(map, marker);
            });
        });

    } else {
        alert('Geo Location feature is not supported in this browser.');
    }
</script>

</body>
</html>
