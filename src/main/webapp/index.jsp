<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="servlets.DBConnection, java.sql.*, sql.IBookConstants" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Rajtara - Book Store</title>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
        background-color: #B2DFDB;
    }

    .container {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 20px; 
        padding: 20px;
        width: 100%;
        margin-top:40px;
    }

    .scroll-wrapper {
        display: flex;
        flex-direction: column; 
        gap: 20px;
        width: 95%;
    }

    .scroll-container {
        display: flex;
        gap: 20px;
        overflow-x: auto;
        scroll-snap-type: x mandatory;
        scrollbar-width: none;
        -ms-overflow-style: none;
        padding: 25px 0;
    }

    .scroll-container::-webkit-scrollbar {
        display: none;
    }

    .book-row {
        display: flex;
        gap: 20px;
        flex-wrap: nowrap;
    }

    .card {
        background-color: #FFFFFF;
        border: 1px solid #DDDDDD;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        width: 200px;
        padding: 15px 10px;
        text-align: center;
        scroll-snap-align: center;
    }

    .card:hover {
        transform: translateY(-5px);
        box-shadow: 0 6px 12px rgba(176, 176, 176, 0.4);
    }

    .card img {
        width: 100%;
        height: auto;
        border-radius: 10px;
    }

    .card h3 {
        margin: 10px 0 5px;
        font-size: 1.2em;
        color: #143842  ;
    }

    .card p {
        margin: 5px 0;
        color: #4A4A4A;
        font-size: 1em;
    }

    .card .price {
        font-weight: bold;
        color: #245B66;
    }

    a {
        text-decoration: none;
        color: inherit;
        transition: color 0.2s;
    }

    a:hover {
        color: #4169E1;
    }
</style>
</head>
<body>
<%@ include file="navbar.html" %>
<div class="container">
    <% 
        try {
            Connection con = DBConnection.getCon();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM " + IBookConstants.TABLE_BOOK);
            ResultSet rs = ps.executeQuery();
            
            int count = 0;
            boolean firstRow = true;
            
            while (rs.next()) {
                if (count % 8 == 0) {
                    if (!firstRow) { 
    %>
                    </div>
                </div>
            </div> 
    <% 
                    }
    %>
        <div class="scroll-wrapper">
            <div class="scroll-container">
                <div class="book-row">
    <% 
                    firstRow = false;
                }
                
                String bName = rs.getString(IBookConstants.COLUMN_NAME);
                String bAuthor = rs.getString(IBookConstants.COLUMN_AUTHOR);
                int bPrice = rs.getInt(IBookConstants.COLUMN_PRICE);
                String imagePath = rs.getString(IBookConstants.COLUMN_IMG);
    %>
                <a href="login.html">
                    <div class="card">
                        <img src="<%= imagePath %>" alt="Book Image">
                        <h3><%= bName %></h3>
                        <p>By: <%= bAuthor %></p>
                        <p class="price">Price: ₹<%= bPrice %></p>
                    </div>
                </a>
    <% 
                count++;
            }
            
            if (count > 0) {
    %>
                </div> 
            </div> 
        </div> 
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
