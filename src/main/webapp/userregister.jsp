<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register Page</title>
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
        padding: 25px;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); 
        text-align: center;
        margin-top: 75px;
    }

    .center-box h1 {
        font-size: 1.5em;
        color: #245B66;
        margin-bottom: 15px;
    }

    .center-box .input-field {
        width: 90%;
        padding: 10px;
        margin: 10px 0;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 14px;
    }

    .center-box .submit-button {
        width: 90%;
        padding: 10px;
        margin: 10px 0;
        background-color: #245B66;
        color: white;
        border: none;
        border-radius: 5px;
        font-size: 14px;
        cursor: pointer;
        transition: background-color 0.3s, transform 0.2s;
    }

    .center-box .submit-button:hover {
        background-color: #1a3e4e;
        transform: scale(1.05);
    }
</style>
</head>
<body>
	<%@ include file="navbar.html" %>
    <div class="center-box">
        <h1>Register Here</h1>
        <form action="userreg" method="post">
            <input type="text" name="username" placeholder="Username" class="input-field" required>
            <input type="password" name="password" placeholder="Password" class="input-field" required>
            <input type="text" name="firstname" placeholder="First Name" class="input-field" required>
            <input type="text" name="lastname" placeholder="Last Name" class="input-field" required>
            <input type="text" name="address" placeholder="Address" class="input-field" required>
            <input type="text" name="phone" placeholder="Phone Number" class="input-field" required>
            <input type="text" name="mailid" placeholder="Email ID" class="input-field" required>
            <input type="submit" value="REGISTER ME!" class="submit-button">
        </form>
    </div>
</body>
</html>
