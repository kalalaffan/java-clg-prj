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

@WebServlet("/buy")
public class ReceiptServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getCon();
            con.setAutoCommit(false); // Begin transaction

            ps = con.prepareStatement("SELECT * FROM " + IBookConstants.TABLE_BOOK);
            rs = ps.executeQuery();

            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Purchase Receipt</title>");
            pw.println("<style>");
            pw.println("body { font-family: Arial, sans-serif; background-color: #FAF3E0; margin: 0; padding: 20px; }");
            pw.println(".container { max-width: 650px; margin: auto; background: #fff; padding: 20px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2); text-align: center; }");
            pw.println("h1 { color: #245B66; font-size: 24px; font-weight: bold; margin-bottom: 15px; }");
            pw.println(".details { margin: 15px 0; padding: 15px; border-radius: 8px; background-color: #f9f9f9; text-align: left; font-size: 16px; line-height: 1.6; }");
            pw.println(".details p { margin: 8px 0; padding-left: 10px; }");
            pw.println(".details p strong { color: #245B66; }");
            pw.println(".total { font-weight: bold; color: #245B66; text-align: right; font-size: 18px; padding-top: 15px; border-top: 2px solid #3D9970; margin-top: 15px; }");
            pw.println(".btn-container { margin-top: 20px; display: flex; justify-content: center; gap: 12px; }");
            pw.println(".btn { background-color: #245B66; color: white; padding: 10px 40px; border: none; border-radius: 6px; font-size: 16px; cursor: pointer; transition: 0.3s; }");
            pw.println(".btn:hover { background-color: #1a3e4e; transform: scale(1.05); }");
            pw.println("table { width: 100%; border-collapse: collapse; margin-top: 10px; }");
            pw.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: center; }");
            pw.println("th { background-color: #245B66; color: white; }");
            pw.println("</style>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<div class='container'>");
            pw.println("<h1>Purchase Receipt</h1>");
            pw.println("<div class='details'>");

            pw.println("<table>");
            pw.println("<tr><th>Book Code</th><th>Book Name</th><th>Author</th><th>Price</th><th>Quantity</th><th>Amount</th></tr>");

            double total = 0.0;
            int i = 0;

            while (rs.next()) {
                String bCode = rs.getString(IBookConstants.COLUMN_BARCODE);
                String bName = rs.getString(IBookConstants.COLUMN_NAME);
                String bAuthor = rs.getString(IBookConstants.COLUMN_AUTHOR);
                int bPrice = rs.getInt(IBookConstants.COLUMN_PRICE);
                int bQty = rs.getInt(IBookConstants.COLUMN_QUANTITY);

                i++;
                String qtyParam = "qty" + i;
                String checkedParam = "checked" + i;

                String qtyValue = req.getParameter(qtyParam);
                String checkedValue = req.getParameter(checkedParam);

                if (checkedValue != null && "pay".equals(checkedValue) && qtyValue != null) {
                    int quantity = Integer.parseInt(qtyValue);

                    if (quantity > bQty) {
                        pw.println("</table><p style='color:red;'>Quantity exceeds available stock for " + bName + "</p>");
                        con.rollback(); // Rollback transaction on error
                        return;
                    }

                    int amount = bPrice * quantity;
                    total += amount;

                    pw.println("<tr><td>" + bCode + "</td><td>" + bName + "</td><td>" + bAuthor + "</td>"
                            + "<td>₹" + bPrice + "</td><td>" + quantity + "</td><td>₹" + amount + "</td></tr>");

                    // Update book quantity
                    try (PreparedStatement updatePs = con.prepareStatement(
                            "UPDATE " + IBookConstants.TABLE_BOOK + " SET " + IBookConstants.COLUMN_QUANTITY
                                    + "=? WHERE " + IBookConstants.COLUMN_BARCODE + "=?")) {
                        updatePs.setInt(1, bQty - quantity);
                        updatePs.setString(2, bCode);
                        updatePs.executeUpdate();
                    }
                }
            }

            pw.println("</table>");
            pw.println("<p class='total'>Total Paid Amount: ₹" + total + "</p>");
            pw.println("<div class='btn-container'>");
            // pw.println("<button class='btn' onclick='window.print()'>Print Receipt</button>");
            pw.println("<button class='btn' onclick='history.back()'>Back</button>");
            pw.println("</div>");
            pw.println("</div>");
            pw.println("</div>");
            pw.println("</body>");
            pw.println("</html>");

            con.commit(); // Commit transaction
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback(); // Rollback transaction on error
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            pw.println("<p style='color:red;'>An error occurred: " + e.getMessage() + "</p>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}
