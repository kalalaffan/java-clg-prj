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
        
        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        PrintWriter pw = res.getWriter();

        
        String uName = req.getParameter(IUserContants.COLUMN_USERNAME);
        String pWord = req.getParameter(IUserContants.COLUMN_PASSWORD);

        try {
           
            Connection con = DBConnection.getCon();
            
            
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM " + IUserContants.TABLE_USERS + " WHERE "
                + IUserContants.COLUMN_USERNAME + "=? AND " 
                + IUserContants.COLUMN_PASSWORD + "=? AND "
                + IUserContants.COLUMN_USERTYPE + "=1"
            );
            ps.setString(1, uName);
            ps.setString(2, pWord);
            ResultSet rs = ps.executeQuery();

            
            if (rs.next()) {
                RequestDispatcher rd = req.getRequestDispatcher("usernavbar.html");
                rd.include(req, res);
                
                pw.println("<div style='display: flex; height: 100vh; '>");

                
                pw.println("<div style='width: 250px; background-color: #245B66; color: white; padding: 20px; padding-top:100px'>");
                pw.println("<h2 style='text-align: center;'>📚 Admin Panel</h2>");
                pw.println("<div style='display: flex; flex-direction: column;'>");
                pw.println("<a href='AddBook.html' style='color: white; text-decoration: none; padding: 12px; display: block; background: rgba(255, 255, 255, 0.2); border-radius: 5px; margin-bottom: 10px;'>➕ Add Books</a>");
                pw.println("<a href='RemoveBooks.html' style='color: white; text-decoration: none; padding: 12px; display: block; background: rgba(255, 255, 255, 0.2); border-radius: 5px; margin-bottom: 10px;'>❌ Remove Books</a>");
                pw.println("<a href='adminOrders.jsp' style='color: white; text-decoration: none; padding: 12px; display: block; background: rgba(255, 255, 255, 0.2); border-radius: 5px; margin-bottom: 10px;'>📖 View Orders</a>");
                pw.println("</div>");
                pw.println("</div>");

                
                pw.println("<div style='flex: 1; padding: 30px; font-size: 1.3em; color: #245B66; font-weight: bold; display: flex; justify-content: center; align-items: center;'>");
                pw.println("Welcome, Admin! Select an option from the left menu.");
                pw.println("</div>");

                pw.println("</div>"); 
            }
else {
                RequestDispatcher rd = req.getRequestDispatcher("AdminLogin.html");
                rd.include(req, res);
                pw.println("<div style='width: 100%; padding: 10px; text-align: center; background-color: #ffdddd; color: #d8000c; font-weight: bold; position: absolute; top: 0; left: 0;'>"
                	    + "Incorrect Username or Password</div>");

            }

            
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            
            e.printStackTrace(pw);
            pw.println("<div >An unexpected error occurred. Please try again later.</div>");
        }
    }
}
