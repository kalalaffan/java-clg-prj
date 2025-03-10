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

@WebServlet("/buys")
public class ReceiptServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        try {
            Connection con = DBConnection.getCon();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM " + IBookConstants.TABLE_BOOK);
            ResultSet rs = ps.executeQuery();
            int i = 0;
            RequestDispatcher rd = req.getRequestDispatcher("ViewBooks.html");
            rd.include(req, res);
            pw.println("<div class=\"tab\">You Successfully Paid for Following Books</div>");
            pw.println(
                    "<div class=\"tab\">\r\n" + "		<table>\r\n" + "			<tr>\r\n" + "				\r\n"
                            + "				<th>Book Code</th>\r\n" + "				<th>Book Name</th>\r\n"
                            + "				<th>Book Author</th>\r\n" + "				<th>Book Price</th>\r\n"
                            + "				<th>Quantity</th><br/>\r\n" + "				<th>Amount</th><br/>\r\n" + "			</tr>");
            double total = 0.0;
            while (rs.next()) {
                int bPrice = rs.getInt(IBookConstants.COLUMN_PRICE);
                String bCode = rs.getString(IBookConstants.COLUMN_BARCODE);
                String bName = rs.getString(IBookConstants.COLUMN_NAME);
                String bAuthor = rs.getString(IBookConstants.COLUMN_AUTHOR);
                int bQty = rs.getInt(IBookConstants.COLUMN_QUANTITY);
                i = i + 1;

                String qt = "qty" + Integer.toString(i);
                int quantity = Integer.parseInt(req.getParameter(qt));
                try {
                    String check1 = "checked" + Integer.toString(i);
                    String getChecked = req.getParameter(check1);
                    if (bQty < quantity) {
                        pw.println(
                                "</table><div class=\"tab\">Please Select the Qty less than Available Books Quantity</div>");
                        break;
                    }

                    if (getChecked.equals("pay")) {
                        pw.println("<tr><td>" + bCode + "</td>");
                        pw.println("<td>" + bName + "</td>");
                        pw.println("<td>" + bAuthor + "</td>");
                        pw.println("<td>" + bPrice + "</td>");
                        pw.println("<td>" + quantity + "</td>");
                        int amount = bPrice * quantity;
                        total = total + amount;
                        pw.println("<td>" + amount + "</td></tr>");
                        bQty = bQty - quantity;
                        System.out.println(bQty);
                        PreparedStatement ps1 = con.prepareStatement("UPDATE " + IBookConstants.TABLE_BOOK + " SET "
                                + IBookConstants.COLUMN_QUANTITY + "=? WHERE " + IBookConstants.COLUMN_BARCODE + "=?");
                        ps1.setInt(1, bQty);
                        ps1.setString(2, bCode);
                        ps1.executeUpdate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            pw.println("</table><br/><div class='tab'>Total Paid Amount: " + total + "</div>");
            String fPay = req.getParameter("f_pay");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
