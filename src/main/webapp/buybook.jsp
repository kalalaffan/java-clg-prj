<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="servlets.DBConnection, java.sql.*, sql.IBookConstants" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Buy Book</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #FAF3E0;
        }
        .container {
            max-width: 600px;
            margin: 100px auto;
            background-color: #FFFFFF;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }
        h1 {
            text-align: center;
            color: #3D9970;
        }
        .book-details {
            text-align: center;
        }
        .book-details img {
            max-width: 100%;
            border-radius: 10px;
        }
        .book-details h3 {
            margin: 10px 0;
            color: #1A202C;
        }
        .book-details p {
            margin: 5px 0;
            color: #245B66;
        }
        .price {
            font-weight: bold;
            color: #3D9970;
        }
        .actions {
            margin-top: 20px;
            text-align: center;
        }
        .actions button {
            padding: 10px 20px;
            font-size: 1em;
            background-color: #245B66;
            color: #FFFFFF;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .actions button:hover {
            background-color: #1a3e4e;
        }
    </style>
</head>
<body>
    <%@ include file="usernavbar.html" %>
    <div class="container">
        <% 
            String bookId = request.getParameter("bookId");
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            if (bookId != null) {
                try {
                    con = DBConnection.getCon();
                    ps = con.prepareStatement(
                        "SELECT * FROM " + IBookConstants.TABLE_BOOK + " WHERE " + IBookConstants.COLUMN_BARCODE + " = ?"
                    );
                    ps.setInt(1, Integer.parseInt(bookId));
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        String bName = rs.getString(IBookConstants.COLUMN_NAME);
                        String bAuthor = rs.getString(IBookConstants.COLUMN_AUTHOR);
                        int bPrice = rs.getInt(IBookConstants.COLUMN_PRICE);
                        String imagePath = rs.getString(IBookConstants.COLUMN_IMG);
        %>
        <div class="book-details">
            <img src="<%= imagePath %>" alt="Book Image">
            <h3><%= bName %></h3>
            <p>By: <%= bAuthor %></p>
            <p class="price">Price: ₹<%= bPrice %></p>
        </div>
        <div class="actions">
            <form action="orderDetails.jsp" method="post">
                <input type="hidden" name="bookId" value="<%= bookId %>">
                <button type="submit">Buy Now</button>
            </form>
        </div>
        <% 
                    } else {
                        out.println("<p>Book not found!</p>");
                    }
                } catch (Exception e) {
                    out.println("<p style='color:red;'>An error occurred: " + e.getMessage() + "</p>");
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (ps != null) ps.close();
                        if (con != null) con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                out.println("<p style='color:red;'>Invalid book selection!</p>");
            }
        %>
    </div>
</body>
</html>
