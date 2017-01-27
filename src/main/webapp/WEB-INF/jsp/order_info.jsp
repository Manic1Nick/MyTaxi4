<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="currentUser" value="${currentUser}"/>
<c:set var="passenger" value="${passenger}"/>
<c:set var="order" value="${order}"/>
<c:set var="orderJSON" value="${orderJSON}"/>
<c:set var="driver" value="${driver}"/>
<c:set var="driverJSON" value="${driverJSON}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Order info</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

    <%--open alert if currect location is null--%>
    <c:if test="${driver != null && driver.currentAddress == null}">
        <div class="alert alert-warning">
            <h4><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <strong>Unfortunately current address of your driver is not determined</strong></h4>
        </div>
    </c:if>

    <c:if test="${order != null}">

        <div id="menu" class="container">
                <%--MENU--%>
            <form id="gomenuForm" method="GET" action="${contextPath}/welcome">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>

            <h4>
                    <%--RETURN TO MENU--%>
                <a onclick="document.forms['gomenuForm'].submit()">Return to menu</a> |

                    <%--HISTORY--%>
                <a href="${contextPath}/order/get/all">Show history
                    <span class="badge">${currentUser.quantityOrders}</span></a> |

                    <%--LAST ORDER--%>
                <c:if test="${currentUser.lastOrderId != null}">
                    <c:if test="${currentUser.lastOrderId == order.id}">
                        <a data-toggle="tooltip" title="Your last order is open now!"
                           style="color:grey">Show last order</a>
                    </c:if>
                    <c:if test="${currentUser.lastOrderId != order.id}">
                        <a href="${contextPath}/order/get?id=${currentUser.lastOrderId}">
                            Show last order</a>
                    </c:if>
                </c:if>
                <c:if test="${currentUser.lastOrderId == null}">
                    <a data-toggle="tooltip" title="You don't have any orders!"
                       style="color:grey">Show last order</a>
                </c:if>

            </h4>
        </div>

        <div id="orderInfo" class="container">
            <h2>Order Info</h2>

            <div id="text" class="col-md-6">
                <table class="table table-hover">
                    <tbody>
                    <tr>
                        <td>ID:</td>
                        <td>${order.id}</td>
                    </tr>
                    <tr>
                        <td>Time create:</td>
                        <td>${order.timeCreate}</td>
                    </tr>
                    <c:if test="${order.timeTaken != null}">
                        <tr>
                            <td>Time taken:</td>
                            <td>${order.timeTaken}</td>
                        </tr>
                    </c:if>
                    <c:if test="${order.timeCancelled != null}">
                        <tr>
                            <td>Time cancelled:</td>
                            <td>${order.timeCancelled}</td>
                        </tr>
                    </c:if>
                    <c:if test="${order.timeClosed != null}">
                        <tr>
                            <td>Time closed:</td>
                            <td>${order.timeClosed}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>Status:</td>
                        <td>${order.orderStatus}</td>
                    </tr>
                    <tr>
                        <td>Address FROM:</td>
                        <td>${order.from.country}, ${order.from.city},
                                ${order.from.street}, ${order.from.houseNum}</td>
                    </tr>
                    <tr>
                        <td>Address TO:</td>
                        <td>${order.to.country}, ${order.to.city},
                                ${order.to.street}, ${order.to.houseNum}</td>
                    </tr>
                    <tr>
                        <td>Passenger:</td>
                        <td>${passenger.username}, ${passenger.userphone}</td>
                    </tr>
                    <c:if test="${driver != null}">
                        <tr>
                            <td>Driver:</td>
                            <td>${driver.username}, ${driver.userphone}</td>
                        </tr>
                        <tr>
                            <td>Car:</td>
                            <td>${driver.car.type}, ${driver.car.model}, ${driver.car.number}</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>Distance:</td>
                        <td>${order.distance} km</td>
                    </tr>
                    <tr>
                        <td>Price:</td>
                        <td>${order.price} uah</td>
                    </tr>
                    </tbody>
                </table>
                <div id="buttons" class="container">
                        <%--button COPY ORDER AS NEW--%>
                    <c:if test="${currentUser.homeAddress != null}">
                        <c:if test="${currentUser.active == false}">
                            <a href="${contextPath}/order/make?id=${order.id}" style="color:white"
                               role="button" class="btn btn-danger active btn-lg">MAKE</a>
                        </c:if>
                        <c:if test="${currentUser.active == true}">
                            <a href="${contextPath}/order/make?id=${order.id}" style="color:white"
                               role="button" class="btn btn-danger disabled btn-lg">MAKE</a>
                        </c:if>
                    </c:if>

                        <%--button TAKE THIS ORDER--%>
                    <c:if test="${currentUser.car != null}">
                        <c:if test="${currentUser.active == false}">
                            <c:if test="${order.orderStatus == 'NEW'}">
                                <a href="${contextPath}/order/take?id=${order.id}" style="color:white"
                                   role="button" class="btn btn-danger active btn-lg">TAKE</a>
                            </c:if>
                            <c:if test="${order.orderStatus != 'NEW'}">
                                <a href="${contextPath}/order/take?id=${order.id}" style="color:white"
                                   role="button" class="btn btn-danger disabled btn-lg">TAKE</a>
                            </c:if>
                        </c:if>
                        <c:if test="${currentUser.active == true}">
                            <a href="${contextPath}/order/take?id=${order.id}" style="color:white"
                               role="button" class="btn btn-danger disabled btn-lg">TAKE</a>
                        </c:if>
                    </c:if>

                        <%--button CANCEL ORDER--%>
                    <c:if test="${currentUser.lastOrderId == order.id}">
                        <c:if test="${order.orderStatus == 'NEW'}">
                            <a href="${contextPath}/order/cancel?id=${order.id}" style="color:white"
                               role="button" class="btn btn-warning active btn-lg">CANCEL</a>
                        </c:if>
                        <c:if test="${order.orderStatus == 'IN_PROGRESS'}">
                            <a href="${contextPath}/order/cancel?id=${order.id}" style="color:white"
                               role="button" class="btn btn-warning active btn-lg">CANCEL</a>
                        </c:if>
                        <c:if test="${order.orderStatus == 'CANCELLED'}">
                            <a href="${contextPath}/order/cancel?id=${order.id}" style="color:white"
                               role="button" class="btn btn-warning disabled btn-lg">CANCEL</a>
                        </c:if>
                        <c:if test="${order.orderStatus == 'CLOSED'}">
                            <a href="${contextPath}/order/cancel?id=${order.id}" style="color:white"
                               role="button" class="btn btn-warning disabled btn-lg">CANCEL</a>
                        </c:if>
                    </c:if>

                    <c:if test="${currentUser.lastOrderId != order.id}">
                        <a href="${contextPath}/order/cancel?id=${order.id}" style="color:white"
                           role="button" class="btn btn-warning disabled btn-lg">CANCEL</a>
                    </c:if>


                        <%--button CLOSE ORDER--%>
                    <c:if test="${order.orderStatus == 'IN_PROGRESS'}">
                        <c:if test="${currentUser.lastOrderId == order.id}">
                            <a href="${contextPath}/order/close?id=${order.id}" style="color:white"
                               role="button" class="btn btn-success active btn-lg">CLOSE</a>
                        </c:if>
                        <c:if test="${currentUser.lastOrderId != order.id}">
                            <a href="${contextPath}/order/close?id=${order.id}" style="color:white"
                               role="button" class="btn btn-success disabled btn-lg">CLOSE</a>
                        </c:if>
                    </c:if>
                    <c:if test="${order.orderStatus != 'IN_PROGRESS'}">
                        <a href="${contextPath}/order/close?id=${order.id}" style="color:white"
                           role="button" class="btn btn-success disabled btn-lg">CLOSE</a>
                    </c:if>
                </div>
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

<%--route--%>
<script type="text/javascript">
    if (navigator.geolocation) {

        navigator.geolocation.getCurrentPosition(function (p) {
            var LatLng = new google.maps.LatLng(p.coords.latitude, p.coords.longitude);
            var mapOptions = {
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

            var markerFrom, markerTo;

            var markerFromLat = <c:out value="${order.from.location.lat}"/>;
            var markerFromLong = <c:out value="${order.from.location.lon}"/>;
            var markerToLat = <c:out value="${order.to.location.lat}"/>;
            var markerToLong = <c:out value="${order.to.location.lon}"/>;
            var orderInfo = ${orderJSON};

            //create empty LatLngBounds object
            var bounds = new google.maps.LatLngBounds();

            //for route
            var directionsDisplay = new google.maps.DirectionsRenderer();
            var directionsService = new google.maps.DirectionsService();

         <c:if test="${driver != null}">
            var markerDriver;

            var markerDriverLat = <c:out value="${driver.currentAddress.location.lat}"/>;
            var markerDriverLong = <c:out value="${driver.currentAddress.location.lon}"/>;
            var driverInfo = ${driverJSON};

            markerDriver = new google.maps.Marker({
                position: new google.maps.LatLng(markerDriverLat, markerDriverLong),
                map: map,
                icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png',
                title: "<div style = 'height:150px;width:200px;color: #2e6da4'>" +
                "<b>Your driver :</b><br />" +
                driverInfo +  "<br /><br />"+
                "Latitude: " + markerDriverLat + "<br />" +
                "Longitude: " + markerDriverLong
            });
            bounds.extend(markerDriver.position);

            //fit the map to the newly inclusive bounds
            map.fitBounds(bounds);

            //for route
            directionsDisplay.setMap(map);

            google.maps.event.addListener(markerDriver, "click", function (e) {
                infoWindow.setContent(markerDriver.title);
                infoWindow.open(map, markerDriver);
            });

        </c:if>

            //route
            var start = new google.maps.LatLng(markerFromLat, markerFromLong);
            var end = new google.maps.LatLng(markerToLat, markerToLong);
            var request = {
                origin:start,
                destination:end,
                travelMode: google.maps.TravelMode.DRIVING
            };
            bounds.extend(start, end);
            //fit the map to the newly inclusive bounds
            map.fitBounds(bounds);
            directionsDisplay.setMap(map);

            directionsService.route(request, function(result, status) {
                if (status == google.maps.DirectionsStatus.OK) {
                    directionsDisplay.setDirections(result);
                }
            });
        });

    } else {
        alert('Geo Location feature is not supported in this browser.');
    }
</script>
</body>
</html>
