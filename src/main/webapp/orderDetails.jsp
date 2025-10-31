<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Confirmation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #FAF3E0;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 500px;
            margin: 100px auto;
            background: #fff;
            padding: 20px 40px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        h2 {
            text-align: center;
            color: #245B66;
        }
        label {
            font-weight: bold;
            display: block;
            margin: 10px 0 5px;
        }
        input, select, textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 1em;
        }
        .btn {
            display: block;
            width: 100%;
            background-color: #245B66;
            color: white;
            padding: 10px;
            border: none;
            border-radius: 5px;
            font-size: 1.1em;
            cursor: pointer;
        }
        .btn:hover {
            background-color: #1a3e4e;
        }
        .btn{
        	margin-top: 10px;	
        }
    </style>
</head>
<body>
    <%@ include file="usernavbar.html" %>
    <div class="container">
        <h2>Confirm Your Order</h2>
        <form action="receipt" method="post">
            <input type="hidden" name="bookId" value="<%= request.getParameter("bookId") %>">

            <label for="fullName">Full Name:</label>
            <input type="text" id="fullName" name="fullName" required>

            <label for="address">Shipping Address:</label>
            <textarea id="address" name="address" rows="4" required></textarea>

            <label for="paymentMode">Select Payment Mode:</label>
            <select id="paymentMode" name="paymentMode" required>
                <option value="COD">Cash on Delivery</option>
                <!-- <option value="Online">Online Payment</option> -->
            </select>

            <button type="submit" class="btn">Confirm Order</button>
            <button class='btn' onclick="history.back()">Back</button>
        </form>
    </div>
</body>
</html>
