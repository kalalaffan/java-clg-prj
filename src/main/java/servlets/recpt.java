package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import constants.IOnlineBookStoreConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sql.IBookConstants;

@WebServlet("/receipt")
public class recpt extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            
            String bookId = req.getParameter("bookId");
            String fullName = req.getParameter("fullName");
            String address = req.getParameter("address");
            String paymentMode = req.getParameter("paymentMode");

            if (bookId == null || bookId.trim().isEmpty()) {
                pw.println("<p style='color:red;'>Invalid book selection!</p>");
                return;
            }

            // Initialize database connection and execute query
            con = DBConnection.getCon();
            ps = con.prepareStatement(
                    "SELECT * FROM " + IBookConstants.TABLE_BOOK + " WHERE " + IBookConstants.COLUMN_BARCODE + " = ?");
            ps.setInt(1, Integer.parseInt(bookId));
            rs = ps.executeQuery();

            if (rs.next()) {
                // Retrieve book details
                String bName = rs.getString(IBookConstants.COLUMN_NAME);
                String bAuthor = rs.getString(IBookConstants.COLUMN_AUTHOR);
                int bPrice = rs.getInt(IBookConstants.COLUMN_PRICE);
                String bCode = rs.getString(IBookConstants.COLUMN_BARCODE);
                
                String insertOrderSQL = "INSERT INTO orders (book_id, full_name, address, payment_mode) VALUES (?, ?, ?, ?)";
                PreparedStatement insertPs = con.prepareStatement(insertOrderSQL);
                insertPs.setString(1, bCode);
                insertPs.setString(2, fullName);
                insertPs.setString(3, address);
                insertPs.setString(4, paymentMode);
                insertPs.executeUpdate();
                insertPs.close();

                // Generate receipt HTML
                pw.println("<!DOCTYPE html>");
                pw.println("<html>");
                pw.println("<head>");
                pw.println("<title>Receipt</title>");
                pw.println("<style>");
                pw.println("body { font-family: Arial, sans-serif; background-color: #FAF3E0; margin: 0; padding: 10px; }");
                pw.println(".container { max-width: 550px; margin: 10px auto; background: #fff; padding: 10px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2); text-align: center; }");
                pw.println("h1 { text-align: center; color: #245B66; font-size: 24px; font-weight: bold; margin-bottom: 25px; }");
                pw.println(".details { margin: 20px 0; padding: 15px; border-radius: 8px; background-color: #f9f9f9; text-align: left; font-size: 18px; line-height: 1.8; }");
                pw.println(".details p { margin: 12px 0; padding-left: 15px; }");
                pw.println(".details p strong { font-weight: bold; color: #245B66; }");
                pw.println(".total { font-weight: bold; color: #245B66; text-align: right; font-size: 20px; padding-top: 20px; border-top: 2px solid #3D9970; margin-top: 20px; }");
                pw.println(".btn-container { margin-top: 25px; display: flex; justify-content: center; gap: 15px; }");
                pw.println(".btn { background-color: #245B66; color: white; padding: 12px 68px; border: none; border-radius: 6px; font-size: 18px; cursor: pointer; font-weight: bold; transition: 0.3s; }");
                pw.println(".btn:hover { background-color: #1a3e4e; transform: scale(1.05); }");
                pw.println("</style>");

                pw.println("</head>");
                pw.println("<body>");
                pw.println("<div class='container'>");
                pw.println("<h1>Purchase Receipt</h1>");
                pw.println("<div class='details'>");

                // User Details
                pw.println("<p><strong>Customer Name:</strong> " + fullName + "</p>");
                pw.println("<p><strong>Shipping Address:</strong> " + address + "</p>");
                pw.println("<p><strong>Payment Mode:</strong> " + paymentMode + "</p>");
                pw.println("<hr>");

                // Book Details
                pw.println("<p><strong>Book Code:</strong> " + bCode + "</p>");
                pw.println("<p><strong>Book Name:</strong> " + bName + "</p>");
                pw.println("<p><strong>Author:</strong> " + bAuthor + "</p>");
                pw.println("<p><strong>Price:</strong> ₹" + bPrice + "</p>");
                pw.println("<p><strong>Quantity:</strong> 1</p>"); // Assuming single quantity purchase

                pw.println("</div>");
                pw.println("<p class='total'>Total Amount Paid: ₹" + bPrice + "</p>");
                pw.println("<div class='btn-container'>");
                //pw.println("<button class='btn' onclick='window.print()'>Print Receipt</button>");
                pw.println("<button class='btn' onclick='history.back()'>Back</button>");
                pw.println("</div>");
                pw.println("</div>");
                
                pw.println("</body>");
                pw.println("</html>");
            } else {
                pw.println("<p>Book not found in the database!</p>");
            }
        } catch (NumberFormatException e) {
            pw.println("<p style='color:red;'>Invalid book ID format. Please try again.</p>");
        } catch (Exception e) {
            pw.println("<p style='color:red;'>An error occurred: " + e.getMessage() + "</p>");
        } finally {
            // Close resources to prevent memory leaks
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
