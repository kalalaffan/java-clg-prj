package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import constants.IOnlineBookStoreConstants;
import sql.IUserContants;

import java.io.PrintWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

// Map the servlet to a specific URL
@WebServlet("/userreg")
public class UserRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        PrintWriter pw = res.getWriter();

        // Retrieve form parameters
        String uName = req.getParameter(IUserContants.COLUMN_USERNAME);
        String pWord = req.getParameter(IUserContants.COLUMN_PASSWORD);
        String fName = req.getParameter(IUserContants.COLUMN_FIRSTNAME);
        String lName = req.getParameter(IUserContants.COLUMN_LASTNAME);
        String addr = req.getParameter(IUserContants.COLUMN_ADDRESS);
        String phNo = req.getParameter(IUserContants.COLUMN_PHONE);
        String mailId = req.getParameter(IUserContants.COLUMN_MAILID);

        // Check if any required field is empty
        if (uName == null || uName.trim().isEmpty() ||
            pWord == null || pWord.trim().isEmpty() ||
            fName == null || fName.trim().isEmpty() ||
            lName == null || lName.trim().isEmpty() ||
            addr == null || addr.trim().isEmpty() ||
            phNo == null || phNo.trim().isEmpty() ||
            mailId == null || mailId.trim().isEmpty()) {

            // If any field is empty, send an error message
            RequestDispatcher rd = req.getRequestDispatcher("UserRegister.html");
            rd.include(req, res);
            pw.println("<p style='color:red;'>All fields are required. Please fill in all the details to register.</p>");
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {
            // Load MySQL Driver and establish connection
            Class.forName(constants.IDatabase.DRIVER_NAME);
            con = DriverManager.getConnection(
                    constants.IDatabase.CONNECTION_STRING,
                    constants.IDatabase.USER_NAME,
                    constants.IDatabase.PASSWORD);

            // Prepare the SQL statement
            String sql = "INSERT INTO " + IUserContants.TABLE_USERS + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, uName);
            ps.setString(2, pWord);
            ps.setString(3, fName);
            ps.setString(4, lName);
            ps.setString(5, addr);
            ps.setString(6, phNo);
            ps.setString(7, mailId);
            ps.setInt(8, 2); // Assuming 2 represents a default user type

            int result = ps.executeUpdate();

            if (result == 1) {
                RequestDispatcher rd = req.getRequestDispatcher("Sample.html");
                rd.include(req, res);
                pw.println("<h3 class='tab'>User Registered Successfully</h3>");
            } else {
                RequestDispatcher rd = req.getRequestDispatcher("userreg");
                rd.include(req, res);
                pw.println("<p>Sorry for the interruption! Please try again.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<p>Error occurred: " + e.getMessage() + "</p>");
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
