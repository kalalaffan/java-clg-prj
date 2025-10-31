package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import constants.IOnlineBookStoreConstants;
//import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sql.IBookConstants;

@WebServlet("/buybook")
public class bbservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getCon();
            ps = con.prepareStatement("SELECT * FROM " + IBookConstants.TABLE_BOOK);
            rs = ps.executeQuery();

            //RequestDispatcher rd = req.getRequestDispatcher("ViewBooks.html");
            //rd.include(req, res);

            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<meta charset='UTF-8'>");
            pw.println("<title>Buy Books</title>");
            pw.println("<style>");
            pw.println("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #FAF3E0; }");
            pw.println(".container { max-width: 800px; margin: 50px auto; background-color: #FFFFFF; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }");
            pw.println("h1 { text-align: center; color: #245B66; }");
            pw.println("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
            pw.println("table, th, td { border: 1px solid #ddd; }");
            pw.println("th, td { padding: 10px; text-align: center; }");
            pw.println("th { background-color: #245B66; color: white; }");
            pw.println(".checkbox-container { display: flex; align-items: center; justify-content: center; }");
            pw.println(".checkbox-container input[type='checkbox'] { margin-right: 10px; transform: scale(1.5); }");
            pw.println(".btn-submit { background-color: #245B66; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-size: 1em; }");
            pw.println(".btn-submit:hover { background-color: #1a3e4e; }");
            pw.println("input[type='text'] { width: 50px; text-align: center; }");
            pw.println("</style>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<div class='container'>");
            pw.println("<h1>Available Books</h1>");
            pw.println("<form action='buy' method='post'>");
            pw.println("<table>");
            pw.println("<tr>");
            pw.println("<th>Select</th>");
            pw.println("<th>Code</th>");
            pw.println("<th>Name</th>");
            pw.println("<th>Author</th>");
            pw.println("<th>Price</th>");
            pw.println("<th>Availability</th>");
            pw.println("<th>Quantity</th>");
            pw.println("</tr>");

            int i = 0;
            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bAuthor = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bAvl = rs.getInt(5);

                i++;
                String n = "checked" + i;
                String q = "qty" + i;

                pw.println("<tr>");
                pw.println("<td class='checkbox-container'><input type='checkbox' name='" + n + "' value='pay'></td>");
                pw.println("<td>" + bCode + "</td>");
                pw.println("<td>" + bName + "</td>");
                pw.println("<td>" + bAuthor + "</td>");
                pw.println("<td>₹" + bPrice + "</td>");
                pw.println("<td>" + bAvl + "</td>");
                pw.println("<td><input type='text' name='" + q + "' value='0'></td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            pw.println("<div style='text-align: center; margin-top: 20px;'>");
            pw.println("<input type='submit' class='btn-submit' value='Buy Now'>");
            pw.println("</div>");
            pw.println("</form>");
            pw.println("</div>");
            pw.println("</body>");
            pw.println("</html>");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    }
}
