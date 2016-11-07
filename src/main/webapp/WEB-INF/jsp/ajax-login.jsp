<%@ include file="include.jsp"%>

<html>
<head>
    <title>Login</title>
    <p align="center">
        <img src="https://lh6.ggpht.com/UGBUfOqA2CZRly317V5U70F-YAY9bMPtFiWAw3JADOfErhIL9V8iNN4x3gR6JGZX51s=w300"
             alt="MyTaxiLogo">
    </p>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function sendLogin() {
            var phone = $("#phoneInput").val();
            var password = $("#passInput").val();
            var confObj = {
                type:"POST",
                url: "login",
                data: {
                    phone : phone,
                    password : password
                },
                error: function(resp){
                    alert(resp);
                }
            };
            $.ajax(confObj);
        }
    </script>

    <script>
        function colorBackground(x) {
            x.style.background = "lemonchiffon";
        }
    </script>

    <style>
        div {
            font-family: arial, sans-serif;
        }
        body {
            background-image: url("https://www.skotcher.com/wall/f300b886a001ea4237a18522a7f91d51/bokeh-buildings-street-taxi-tilt-shift.jpg");
        }
        h1 {
            color: yellow;
            font-size: 24px;
        }
    </style>

</head>

<body>
<div align="center" id="login">
    <h1>
        <p>
            <label>Input phone</label>
            <input id="phoneInput" type="text" onfocus="colorBackground(this)">
        </p>
        <p>
            <label>Input password</label>
            <input id="passInput" type="password" onfocus="colorBackground(this)"><br>
        </p>
    </h1>
    <h2>
        <p>
            <button onclick="sendLogin()" style="background-color:limegreen">
                LOGIN</button>
        </p>
    </h2>
    <h3>
        <p>
            <input type="button" value="REGISTER PASSENGER" style="background-color:lightgrey"
                   onclick="window.location='register-passenger'" />
        </p>
        <p>
            <input type="button" value="REGISTER DRIVER" style="background-color:lightgrey"
                   onclick="window.location='register-driver'" />
        </p>
    </h3>

</div>
</body>
</html>