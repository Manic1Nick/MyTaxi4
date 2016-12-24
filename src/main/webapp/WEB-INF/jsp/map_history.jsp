<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="user" value="${currentUser}"/>
<c:set var="listOrders" value="${listOrders}"/>
<c:set var="ordersJSON" value="${ordersJSON}"/>

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

    <title>Map of your orders</title>

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

    <%--LOGOUT--%>
    <script>
        function logout() {
            $("#logout").modal('show');
        }
    </script>
    <div class="modal fade" id="logout" role="dialog">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Are you sure to exit?</h4>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal"
                            onclick="document.forms['logoutForm'].submit()">Confirm</button>
                </div>
            </div>
        </div>
    </div>

    <div class="menu">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <form id="gomenuForm" method="GET" action="${contextPath}/welcome">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h4>
            <%--LOGOUT--%>
            <a onclick="logout()">Logout</a> |

            <%--RETURN TO MENU--%>
            <a onclick="document.forms['gomenuForm'].submit()">Return to menu</a> |

            <%--RETURN TO HISTORY--%>
            <a href="${contextPath}/order/get/all">Return to history</a>
        </h4>
    </div>

    <script type="text/javascript">
        if (navigator.geolocation) {

            navigator.geolocation.getCurrentPosition(function (p) {
                var LatLng = new google.maps.LatLng(p.coords.latitude, p.coords.longitude);
                var mapOptions = {
                    center: LatLng,
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


                var markerLat, markerLong, orderInfo;
                markerLat = [
                    <c:forEach var="s" items="${listOrders}" varStatus="status">
                        <c:out value="${s.from.location.lat}"/>
                        <c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ];
                markerLong = [
                    <c:forEach var="s" items="${listOrders}" varStatus="status">
                        <c:out value="${s.from.location.lon}"/>
                        <c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ];
                orderInfo = ${ordersJSON};

                //create empty LatLngBounds object
                var bounds = new google.maps.LatLngBounds();
                var markerOrder, i;

                for (i = 0; i < markerLat.length; i++) {
                    markerOrder = new google.maps.Marker({
                        position: new google.maps.LatLng(markerLat[i], markerLong[i]),
                        map: map,
                        icon: 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png',
                        title: "<div style = 'height:150px;width:200px'>" +
                        "<b>Order :</b><br />" +
                        orderInfo[i] +  "<br />"+
                        "Lat: " + markerLat[i] + "<br />" +
                        "Long: " + markerLong[i]
                    });
                    //extend the bounds to include each marker's position
                    bounds.extend(markerOrder.position);

                    google.maps.event.addListener(markerOrder, "click", (function(markerOrder, i) {
                        return function() {
                            infoWindow.setContent(markerOrder.title);
                            infoWindow.open(map, markerOrder);
                        }
                    })(markerOrder, i));

                    //now fit the map to the newly inclusive bounds
                    map.fitBounds(bounds);

                    //(optional) restore the zoom level after the map is done scaling
                    var listener = google.maps.event.addListener(map, "idle", function () {
                        map.setZoom(10);
                        google.maps.event.removeListener(listener);
                    });
                }
            });

        } else {
            alert('Geo Location feature is not supported in this browser.');
        }
    </script>

    <h2 class="form-heading">All your orders:</h2>
    <div id="dvMap" style="width: 1000px; height: 500px">
    </div>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
