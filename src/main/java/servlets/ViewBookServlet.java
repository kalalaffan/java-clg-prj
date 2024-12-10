package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
//import java.sql.DriverManager;
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

@WebServlet("/viewbook")
public class ViewBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        PrintWriter pw = res.getWriter();

        
       

        try {
            // Load the driver and connection building
        	Connection con = DBConnection.getCon();

            // Prepare and execute the SQL query
        	PreparedStatement ps = con.prepareStatement("SELECT * FROM " + IBookConstants.TABLE_BOOK);
        	ResultSet rs = ps.executeQuery();

            // Include the ViewBooks.html content
            RequestDispatcher rd = req.getRequestDispatcher("ViewBooks.html");
            rd.include(req, res);

            // Display book data in the same format
            pw.println("<div class=\"tab\">Books Available In Our Store</div>");
            pw.println("<div class=\"tab\">\r\n" +
                    "        <table>\r\n" +
                    "            <tr>\r\n" +
                    "                <th>Book Code</th>\r\n" +
                    "                <th>Book Name</th>\r\n" +
                    "                <th>Book Author</th>\r\n" +
                    "                <th>Book Price</th>\r\n" +
                    "                <th>Quantity</th>\r\n" +
                    "                <th>Image</th>\r\n" +
                    "            </tr>");

            while (rs.next()) {
                String bCode = rs.getString(1);
                String bName = rs.getString(2);
                String bAuthor = rs.getString(3);
                int bPrice = rs.getInt(4);
                int bQty = rs.getInt(5);
                String imagePath = rs.getString(IBookConstants.COLUMN_IMG);

                pw.println("<tr><td>" + bCode + "</td>");
                pw.println("<td>" + bName + "</td>");
                pw.println("<td>" + bAuthor + "</td>");
                pw.println("<td>" + bPrice + "</td>");
                pw.println("<td>" + bQty + "</td>");
                pw.println("<td><img src='" + imagePath + "' width='100' height='100' alt='Book Image'/></td></tr>");
            }

            pw.println("</table>\r\n" +
                    "    </div>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<div class=\"tab\">An error occurred while fetching book details. " + e.getMessage() + " Please try again later.</div>");
        }
    }
}
