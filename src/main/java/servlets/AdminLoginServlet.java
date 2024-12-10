package servlets;

import java.io.*;
import java.sql.*;
import constants.IOnlineBookStoreConstants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sql.IUserContants;

@WebServlet("/adminlog")
public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Set response type and get writer
        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        PrintWriter pw = res.getWriter();

        // Retrieve parameters
        String uName = req.getParameter(IUserContants.COLUMN_USERNAME);
        String pWord = req.getParameter(IUserContants.COLUMN_PASSWORD);

        try {
            // Establish database connection
            Connection con = DBConnection.getCon();
            
            // Prepare and execute SQL query
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM " + IUserContants.TABLE_USERS + " WHERE "
                + IUserContants.COLUMN_USERNAME + "=? AND " 
                + IUserContants.COLUMN_PASSWORD + "=? AND "
                + IUserContants.COLUMN_USERTYPE + "=1"
            );
            ps.setString(1, uName);
            ps.setString(2, pWord);
            ResultSet rs = ps.executeQuery();

            // Handle login result
            if (rs.next()) {
                RequestDispatcher rd = req.getRequestDispatcher("Sample.html");
                rd.include(req, res);
                pw.println("<div class=\"tab\">Admin Login Successful</div>");
                pw.println("<div class=\"tab\"><br/><a href=\"AddBook.html\">ADD BOOKS</a><br/></div>");
                pw.println("<div class=\"tab\"><br/><a href=\"RemoveBooks.html\">REMOVE BOOKS</a><br/></div>");
                pw.println("<div class=\"tab\"><br/><a href=\"viewbook\">VIEW BOOKS</a></div>");
            } else {
                RequestDispatcher rd = req.getRequestDispatcher("AdminLogin.html");
                rd.include(req, res);
                pw.println("<div class=\"tab\">Incorrect Username or Password</div>");
            }

            // Close resources
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            // Log error and display a user-friendly message
            e.printStackTrace(pw);
            pw.println("<div class=\"tab\">An unexpected error occurred. Please try again later.</div>");
        }
    }
}
