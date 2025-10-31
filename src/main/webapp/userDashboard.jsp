<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome User</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #F5F5F5;
            margin: 0;
            padding: 0;
        }
        
        .welcome {
            font-size: 1.2rem;
            color: #fff;
            margin-bottom: 0;
            margin-top: -3px;
            margin-left:890px;
            z-index: 2000;
            width: 60px;
            /*height:23px;*/
            padding: 4px 4px;
            display: flex;
   			justify-content: center;
   			align-items: center;
   			border-radius:5px;
            
        }
        .welcome:hover{
        background-color:#3A7D8A;
        }
        .links a {
            display: block;
            margin: 10px 0;
            text-decoration: none;
            padding: 10px 15px;
            background: #3D9970;
            color: #FFF;
            border-radius: 5px;
            text-align: center;
        }
        .links a:hover {
            background: #2E7D55;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="welcome">${username}</div>
    </div>
</body>
</html>
