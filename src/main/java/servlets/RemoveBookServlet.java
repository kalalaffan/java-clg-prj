package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sql.IBookConstants;

@WebServlet("/remove")
public class RemoveBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        String bkid = req.getParameter("barcode"); // Get the book ID (barcode) from the request.

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Get a new connection
            con = DBConnection.getCon();

            // Check connection validity (optional but helpful for debugging)
            if (con == null || con.isClosed()) {
                throw new Exception("Database connection is null or closed!");
            }

            // Prepare the DELETE statement
            ps = con.prepareStatement(
                "DELETE FROM " + IBookConstants.TABLE_BOOK + " WHERE " + IBookConstants.COLUMN_BARCODE + "=?"
            );
            ps.setString(1, bkid);

            // Execute the DELETE query
            int result = ps.executeUpdate();

            // Forward to the page with appropriate messages
            RequestDispatcher rd = req.getRequestDispatcher("RemoveBooks.html");
            rd.include(req, res);

            if (result == 1) {
            	pw.println("<div style='text-align: center; font-weight: bold; font-size: 18px; color: #3D9970; margin-top: 20px;'>Book Removed Successfully</div>");
            	pw.println("<div style='text-align: center; margin-top: 10px;'><a href='RemoveBooks.html' style='color: #fff; background-color: #3D9970; padding: 10px 15px; border-radius: 5px; text-decoration: none;'>Remove more Books</a></div>");
            } else {
            	pw.println("<div style='text-align: center; font-weight: bold; font-size: 18px; color: #D32F2F; margin-top: 20px;'>Book Not Available In The Store</div>");
            	pw.println("<div style='text-align: center; margin-top: 10px;'><a href='RemoveBooks.html' style='color: #fff; background-color: #D32F2F; padding: 10px 15px; border-radius: 5px; text-decoration: none;'>Remove more Books</a></div>");
            }
        } catch (Exception e) {
            // Log exception and send an error message to the client
            e.printStackTrace();
            pw.println("<div style='text-align: center; font-weight: bold; font-size: 18px; color: #FF5722; margin-top: 20px;'>An unexpected error occurred: " + e.getMessage() + "</div>");
        } finally {
            // Close resources to avoid memory leaks
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
