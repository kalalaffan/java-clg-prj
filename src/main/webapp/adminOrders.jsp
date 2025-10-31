<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="servlets.DBConnection, java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Pending Orders</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #FAF3E0; margin: 0; padding: 20px; }
        .container { max-width: 100%; margin-top: 50px; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }
        h2 { text-align: center; color: #245B66; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 20px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background-color: #245B66; color: white; }
        tr:hover { background-color: #f1f1f1; }
        .btn { background-color: #245B66; color: white; padding: 8px 12px; border: none; border-radius: 5px; cursor: pointer; }
        .btn:hover { background-color: #1a3e4e; }
    </style>
</head>
<body>
    <%@ include file="usernavbar.html" %>
    <div class="container">
        <h2>Pending Orders</h2>
        <table>
            <tr>
                <th>Order ID</th>
                <th>Book ID</th>
                <th>Customer Name</th>
                <th>Address</th>
                <th>Payment Mode</th>
                <th>Order Date</th>
                <th>Action</th>
                <th>Print</th>
                <th>Remark</th>
            </tr>
            <%
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;

                try {
                    con = DBConnection.getCon();
                    ps = con.prepareStatement("SELECT * FROM orders ORDER BY order_date DESC");
                    rs = ps.executeQuery();
                    
                    while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getInt("order_id") %></td>
                <td><%= rs.getString("book_id") %></td>
                <td><%= rs.getString("full_name") %></td>
                <td><%= rs.getString("address") %></td>
                <td><%= rs.getString("payment_mode") %></td>
                <td><%= rs.getTimestamp("order_date") %></td>
                <td>
                    <form action="markShippedServlet" method="post">
                        <input type="hidden" name="order_id" value="<%= rs.getInt("order_id") %>">
                        <button type="submit" class="btn">Mark as Shipped</button>
                    </form>
                </td>
                
<td>
<form action="adminReceiptServlet" method="get">
    <input type="hidden" name="order_id" value="<%= rs.getInt("order_id") %>">
    <button type="submit" class="btn">Receipt</button>
</form>
</td>
<td>

    <form action="CancelOrderServlet" method="post" onsubmit="return confirm('Are you sure you want to cancel this order?');">
        <input type="hidden" name="order_id" value="<%= rs.getInt("order_id") %>">
        <button type="submit" class="btn" style="background-color: red; color: white;">Cancel</button>
    </form>
</td>

                
            </tr>
            <% 
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (rs != null) rs.close();
                    if (ps != null) ps.close();
                    if (con != null) con.close();
                }
            %>
        </table>
    </div>
</body>
</html>
