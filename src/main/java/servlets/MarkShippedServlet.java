package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Servlet to handle marking an order as shipped
@WebServlet("/markShippedServlet") // Annotation-based servlet mapping
public class MarkShippedServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get the order ID from the request
        String orderIdParam = req.getParameter("order_id");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            res.sendRedirect("adminOrders.jsp?error=InvalidOrderID");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdParam);
        } catch (NumberFormatException e) {
            res.sendRedirect("adminOrders.jsp?error=InvalidOrderIDFormat");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Get the database connection
            con = DBConnection.getCon();

            // Delete the order from the database
            String sql = "DELETE FROM orders WHERE order_id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Order ID " + orderId + " marked as shipped and removed from database.");
                res.sendRedirect("adminOrders.jsp?success=OrderShipped");
            } else {
                System.out.println("Order ID " + orderId + " not found.");
                res.sendRedirect("adminOrders.jsp?error=OrderNotFound");
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("adminOrders.jsp?error=ServerError");
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
