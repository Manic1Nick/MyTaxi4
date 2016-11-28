<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
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
    <!-- The above 3 meta tags *must* come first in the head;
    any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Order ${order.id} info page</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
    <%--<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>--%>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyD-KmQUcMJUpRzjthK1CvNtmYw3mLf9vzs&callback=initMap"
            type="text/javascript"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<div class="container">

    <div class="menu">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <form id="gomenuForm" method="GET" action="${contextPath}/welcome">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h4>
            <%--LOGOUT--%>
            <a onclick="document.forms['logoutForm'].submit()">Logout</a> |

            <%--RETURN TO MENU--%>
            <a onclick="document.forms['gomenuForm'].submit()">Return to menu</a> |

            <%--RETURN TO ORDER INFO--%>
            <a href="${contextPath}/order/get?id=${order.id}">Order info</a>
        </h4>
    </div>

    <script type="text/javascript">
        if (navigator.geolocation) {

            navigator.geolocation.getCurrentPosition(function (p) {
                var LatLng = new google.maps.LatLng(p.coords.latitude, p.coords.longitude);
                var mapOptions = {
                    zoom: 13,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("dvMap"), mapOptions);
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

                markerFrom = new google.maps.Marker({
                    position: new google.maps.LatLng(markerFromLat, markerFromLong),
                    map: map,
                    icon: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png',
                    title: "<div style = 'height:150px;width:200px'>" +
                    "<b>Order :</b><br />" +
                    orderInfo +  "<br /><br />"+
                    "Lat: " + markerFromLat + "<br />" +
                    "Long: " + markerFromLong
                });
                bounds.extend(markerFrom.position);

                markerTo = new google.maps.Marker({
                    position: new google.maps.LatLng(markerToLat, markerToLong),
                    map: map,
                    icon: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png',
                    title: "<div style = 'height:150px;width:200px'>" +
                    "<b>Order :</b><br />" +
                    orderInfo +  "<br /><br />"+
                    "Lat: " + markerToLat + "<br />" +
                    "Long: " + markerToLong
                });
                bounds.extend(markerTo.position);

                //fit the map to the newly inclusive bounds
                map.fitBounds(bounds);

                //for route
                directionsDisplay.setMap(map);

                google.maps.event.addListener(markerFrom, "click", function (e) {
                    infoWindow.setContent(markerFrom.title);
                    infoWindow.open(map, markerFrom);
                });
                google.maps.event.addListener(markerTo, "click", function (e) {
                    infoWindow.setContent(markerTo.title);
                    infoWindow.open(map, markerTo);
                });

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
                        "<b>Driver :</b><br />" +
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
                var start = markerFrom.position;
                var end = markerTo.position;
                var request = {
                    origin:start,
                    destination:end,
                    travelMode: google.maps.TravelMode.DRIVING
                };

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

    <h2 class="form-heading">Order id ${order.id}
        (distance ${order.distance} km, price ${order.price} uah)</h2>
    <div id="dvMap" style="width: 1000px; height: 500px">
    </div>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
