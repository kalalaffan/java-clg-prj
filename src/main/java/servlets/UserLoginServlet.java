package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import constants.IOnlineBookStoreConstants;
import sql.IUserContants;
@WebServlet("/userlog")
public class UserLoginServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        PrintWriter pw = res.getWriter();
        
        // Fetch user credentials from the request
        String uName = req.getParameter(IUserContants.COLUMN_USERNAME);
        String pWord = req.getParameter(IUserContants.COLUMN_PASSWORD);

        try {
            // Establish database connection
            Connection con = DBConnection.getCon();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM " + IUserContants.TABLE_USERS + 
                " WHERE " + IUserContants.COLUMN_USERNAME + " = ? AND " +
                IUserContants.COLUMN_PASSWORD + " = ? AND " +
                IUserContants.COLUMN_USERTYPE + " = 2"
            );
            ps.setString(1, uName);
            ps.setString(2, pWord);

            // Execute query
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // User found, include sample page and display welcome message
                RequestDispatcher rd = req.getRequestDispatcher("Sample.html");
                rd.include(req, res);
                pw.println("<div class=\"home hd brown\">Welcome, " + uName + "!</div><br/>");
                pw.println("<div class=\"tab hd brown\">User Login Successful!</div><br/>");
                pw.println("<div class=\"tab\"><a href=\"viewbook\">VIEW BOOKS</a></div>");
                pw.println("<div class=\"tab\"><a href=\"buybook\">BUY BOOKS</a></div>");
            } else {
                // Invalid credentials, redirect to login page
                RequestDispatcher rd = req.getRequestDispatcher("UserLogin.html");
                rd.include(req, res);
                pw.println("<div class=\"tab\">Incorrect Username or Password</div>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            pw.println("<div class=\"tab red\">Database Error: " + e.getMessage() + "</div>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<div class=\"tab red\">An unexpected error occurred: " + e.getMessage() + "</div>");
        } finally {
            pw.close(); // Close the PrintWriter
        }
    }
}
