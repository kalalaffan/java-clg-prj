package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import constants.IOnlineBookStoreConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlets.DBConnection;

@WebServlet("/orderServlet")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        PrintWriter out = response.getWriter();

        // Get form parameters
        String bookId = request.getParameter("bookId");
        String fullName = request.getParameter("fullName");
        String address = request.getParameter("address");
        String paymentMode = request.getParameter("paymentMode");

        if (bookId == null || fullName == null || address == null || paymentMode == null) {
            out.println("<p style='color:red;'>Invalid input! Please fill all fields.</p>");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getCon();
            ps = con.prepareStatement(
                    "INSERT INTO orders (book_id, full_name, address, payment_mode) VALUES (?, ?, ?, ?)");

            ps.setString(1, bookId);
            ps.setString(2, fullName);
            ps.setString(3, address);
            ps.setString(4, paymentMode);

            int result = ps.executeUpdate();
            if (result > 0) {
                // Redirect to receipt page with order details
                response.sendRedirect("receipt?bookId=" + bookId + "&fullName=" + fullName + "&address=" + address + "&paymentMode=" + paymentMode);
            } else {
                out.println("<p style='color:red;'>Order placement failed. Please try again.</p>");
            }
        } catch (SQLException e) {
            out.println("<p style='color:red;'>Database error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
