package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import constants.IOnlineBookStoreConstants;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sql.IBookConstants;

@WebServlet("/buybook")
public class BuyBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        
        try {
            // Get the connection
            Connection con = DBConnection.getCon();
            
            // Prepare the SQL query to fetch books
            PreparedStatement ps = con.prepareStatement("SELECT * FROM " + IBookConstants.TABLE_BOOK);
            ResultSet rs = ps.executeQuery();
            
            // Include the ViewBooks.html content
            RequestDispatcher rd = req.getRequestDispatcher("ViewBooks.html");
            rd.include(req, res);
            
            // Print the table with books
            pw.println("<div class=\"tab hd brown \">Books Available In Our Store</div>");
            pw.println("<div class=\"tab\"><form action=\"buys\" method=\"post\">");
            pw.println("<table>\r\n" + 
                    "            <tr>\r\n" + 
                    "                <th>Books</th>\r\n" + 
                    "                <th>Code</th>\r\n" + 
                    "                <th>Name</th>\r\n" + 
                    "                <th>Author</th>\r\n" + 
                    "                <th>Price</th>\r\n" + 
                    "                <th>Avail</th>\r\n" + 
                    "                <th>Qty</th>\r\n" + 
                    "            </tr>");
            
            int i = 0;
            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bAuthor = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bAvl = rs.getInt(5);
                
                i = i + 1;
                String n = "checked" + Integer.toString(i);
                String q = "qty" + Integer.toString(i);
                
                pw.println("<tr>\r\n" + 
                        "                <td>\r\n" + 
                        "                    <input type=\"checkbox\" name=" + n + " value=\"pay\">\r\n" + // Value is made equal to bcode
                        "                </td>");
                pw.println("<td>" + bCode + "</td>");
                pw.println("<td>" + bName + "</td>");
                pw.println("<td>" + bAuthor + "</td>");
                pw.println("<td>" + bPrice + "</td>");
                pw.println("<td>" + bAvl + "</td>");
                pw.println("<td><input type=\"text\" name=" + q + " value=\"0\" text-align=\"center\"></td></tr>");
            }
            
            pw.println("</table>\r\n" + "<input type=\"submit\" value=\" PAY NOW \">" + "<br/>" + 
                    "    </form>\r\n" + 
                    "    </div>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
