<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="servlets.DBConnection, java.sql.*,sql.IBookConstants" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Books Available</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #973131;
        }
        .container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 20px;
            padding: 20px;
        }
        .card {
            background-color: #E0A75E;
            border: 1px solid #000;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 250px;
            padding: 15px;
            text-align: center;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .card:hover {
            transform: translateY(-10px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
        }
        .card img {
            width: 100%;
            height: auto;
            border-radius: 10px;
        }
        .card h3 {
            margin: 10px 0 5px;
            font-size: 1.2em;
            color: #333;
        }
        .card p {
            margin: 5px 0;
            color: #666;
            font-size: 1em;
        }
        .card .price {
            font-weight: bold;
            color: #007bff;
        }
    </style>
</head>
<body>
<iframe src="navbar.html" style="border: none; width: 100%; height: auto;"></iframe> 
    <div class="container">
        <% 
            try {
                Connection con = DBConnection.getCon();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM " + IBookConstants.TABLE_BOOK);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String bName = rs.getString(IBookConstants.COLUMN_NAME);
                    String bAuthor = rs.getString(IBookConstants.COLUMN_AUTHOR);
                    int bPrice = rs.getInt(IBookConstants.COLUMN_PRICE);
                    String imagePath = rs.getString(IBookConstants.COLUMN_IMG);
                    String bquantity = rs.getString(IBookConstants.COLUMN_QUANTITY);
        %>
        <a href="login.html" style="text-decoration:none">
        <div class="card">
            <img src="<%= imagePath %>" alt="Book Image">
            <h3><%= bName %></h3>
            <p>By: <%= bAuthor %></p>
            <%-- <p>Quantity: <%=bquantity %></p> --%>
            <p class="price">Price: ₹<%= bPrice %></p>
        </div>
        </a>
        <% 
                }
            } catch (Exception e) { 
        %>
        <p style="color: red;">An error occurred: <%= e.getMessage() %></p>
        <% 
            } 
        %>
    </div>
</body>
</html>
