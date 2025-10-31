<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Page</title>
<style>
    body {
        margin: 0;
        padding: 0;
        font-family: Arial, sans-serif;
        background-color: #B2DFDB; 
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh; 
    }

    .center-box {
        background-color: #FFFFFF; 
        width: 300px;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); 
        text-align: center;
    }

    .center-box h1 {
        font-size: 1.5em;
        color: #245B66;
        margin-bottom: 20px;
    }

    .center-box a {
        display: block; 
        text-decoration: none;
        font-size: 1.1em;
        color: #FFFFFF;
        background-color: #245B66;
        padding: 10px 20px;
        margin: 10px 0; 
        border-radius: 5px; 
        transition: background-color 0.3s, transform 0.2s;
    }

    .center-box a:hover {
        background-color: #1a3e4e; 
        transform: scale(1.05);
    }

    .center-box a span {
        font-weight: bold; 
    }

    .center-box .new-user {
        background-color: #ff3131 ;
    }

    .center-box .new-user:hover {
        background-color: #1a3e4e; 
    }
</style>
</head>
<body>
	<%@ include file="navbar.html" %>
    <div class="center-box">
        <h1>Login or Signup Below</h1>
        <a href="AdminLogin.html">Login As Admin</a>
        <a href="UserLogin.html">Login As <span>User</span></a>
        <a href="userregister.jsp" class="new-user">New user ! Register Here</a>
    </div>
</body>
</html>