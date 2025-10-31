package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/updateOrderStatus")
public class UpdateOrderStatus extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");

        try (Connection con = DBConnection.getCon();
             PreparedStatement ps = con.prepareStatement("UPDATE orders SET order_status = 'Delivered' WHERE id = ?")) {
            ps.setInt(1, Integer.parseInt(orderId));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        res.sendRedirect("orders.jsp");
    }
}
