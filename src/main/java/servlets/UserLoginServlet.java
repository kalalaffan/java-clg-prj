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
        
        String uName = req.getParameter(IUserContants.COLUMN_USERNAME);
        String pWord = req.getParameter(IUserContants.COLUMN_PASSWORD);

        try {
            
            Connection con = DBConnection.getCon();
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM " + IUserContants.TABLE_USERS + 
                " WHERE " + IUserContants.COLUMN_USERNAME + " = ? AND " +
                IUserContants.COLUMN_PASSWORD + " = ? AND " +
                IUserContants.COLUMN_USERTYPE + " = 2"
            );
            ps.setString(1, uName);
            ps.setString(2, pWord);

            
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
               req.setAttribute("username", uName);
               RequestDispatcher rd = req.getRequestDispatcher("userDashboard.jsp");
               rd.include(req, res);
              
               pw.println("<div style='width: 100%; padding: 4px 0; text-align: center; background-color:  #808080    ; position: fixed; bottom: 10; left: 0; z-index: 1000;'>"
            	        + "<marquee behavior='scroll' direction='right' scrollamount='5'>"
            	        + "<a href='buybook' style='color: #FFFFFF; font-size: 0.85em; font-weight: bold; text-decoration: none; display: inline-block; transition: color 0.3s ease;' "
            	        + "onmouseover='this.style.color=\"#D4EDDA\"' onmouseout='this.style.color=\"#FFFFFF\"'>"
            	        + "GET NEW PUBLISHED BOOKS AT BEST PRICE ! HURRY UP !</a>"
            	        + "</marquee>"
            	        + "</div>");





               RequestDispatcher rrd = req.getRequestDispatcher("user.jsp");
               rrd.include(req, res);
            } else {
                
                RequestDispatcher rd = req.getRequestDispatcher("UserLogin.html");
                rd.include(req, res);
                pw.println("<div style='width: 100%; padding: 10px; text-align: center; background-color: #ffdddd; color: #d8000c; font-weight: bold; position: absolute; top: 0; left: 0;'>"
                	    + "Incorrect Username or Password</div>");

            }

        } catch (SQLException e) {
            e.printStackTrace();
            pw.println("<div> Database Error: " + e.getMessage() + "</div>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<div> An unexpected error occurred: " + e.getMessage() + "</div>");
        } finally {
            pw.close(); 
        }
    }
}
