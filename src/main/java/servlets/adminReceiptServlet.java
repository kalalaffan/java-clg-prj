package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/adminReceiptServlet")
public class adminReceiptServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Retrieve order ID from request
            int orderId = Integer.parseInt(req.getParameter("order_id"));

            // Establish database connection
            con = DBConnection.getCon();

            // SQL query to fetch order and book details
            String sql = "SELECT o.order_id, o.full_name, o.address, o.payment_mode, b.name, b.author, b.price "
                    + "FROM orders o JOIN books b ON o.book_id = b.barcode WHERE o.order_id = ?";


            ps = con.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Fetching order and book details
                String fullName = rs.getString("full_name");
                String address = rs.getString("address");
                String paymentMode = rs.getString("payment_mode");
                String bookName = rs.getString("name");
                String author = rs.getString("author");
                int price = rs.getInt("price");

                // Generate receipt HTML
                pw.println("<!DOCTYPE html>");
                pw.println("<html>");
                pw.println("<head>");
                pw.println("<title>Receipt</title>");
                pw.println("<style>");
                pw.println("body { font-family: Arial, sans-serif; background-color: #B2DFDB; padding: 20px; }");
                pw.println(".container { max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }");
                pw.println("h1 { text-align: center; color: #245B66; }");
                pw.println(".details { margin: 20px 0; padding: 15px; border-radius: 8px; background-color: #f9f9f9; text-align: left; font-size: 18px; line-height: 1.8; }");
                pw.println(".details p { margin: 10px 0; }");
                pw.println(".total { font-weight: bold; color: #245B66; text-align: right; font-size: 20px; padding-top: 20px; border-top: 2px solid #3D9970; margin-top: 20px; }");
                pw.println(".btn-container { text-align: center; margin-top: 25px; }");
                pw.println(".btn { background-color: #245B66; color: white; padding: 10px 20px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer; transition: 0.3s; }");
                pw.println(".btn:hover { background-color: #1a3e4e; transform: scale(1.05); }");
                pw.println("</style>");
                pw.println("</head>");
                pw.println("<body>");
                pw.println("<div class='container'>");
                pw.println("<h1>Order Receipt</h1>");
                pw.println("<div class='details'>");

                // Customer Details
                pw.println("<p><strong>Order ID:</strong> " + orderId + "</p>");
                pw.println("<p><strong>Customer Name:</strong> " + fullName + "</p>");
                pw.println("<p><strong>Shipping Address:</strong> " + address + "</p>");
                pw.println("<p><strong>Payment Mode:</strong> " + paymentMode + "</p>");
                pw.println("<hr>");

                // Book Details
                pw.println("<p><strong>Book Name:</strong> " + bookName + "</p>");
                pw.println("<p><strong>Author:</strong> " + author + "</p>");
                pw.println("<p><strong>Price:</strong> ₹" + price + "</p>");
                pw.println("<p><strong>Quantity:</strong> 1</p>");

                pw.println("</div>");
                pw.println("<p class='total'>Total Amount Paid: ₹" + price + "</p>");
                pw.println("<div class='btn-container'>");
                pw.println("<button class='btn' onclick='window.print()'>Print Receipt</button>");
                pw.println("<button class='btn' onclick='history.back()'>Back</button>");
                pw.println("</div>");
                pw.println("</div>");
                pw.println("</body>");
                pw.println("</html>");
            } else {
                pw.println("<p style='color:red;'>Order not found!</p>");
            }

        } catch (NumberFormatException e) {
            pw.println("<p style='color:red;'>Invalid order ID format. Please try again.</p>");
        } catch (Exception e) {
            pw.println("<p style='color:red;'>An error occurred: " + e.getMessage() + "</p>");
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
