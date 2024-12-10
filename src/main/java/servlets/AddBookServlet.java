package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import constants.IOnlineBookStoreConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sql.IBookConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import jakarta.servlet.http.Part;
@WebServlet("/addbook")
@MultipartConfig

public class AddBookServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	        res.setContentType(IOnlineBookStoreConstants.CONTENT_TYPE_TEXT_HTML);
	        PrintWriter pw = res.getWriter();

	        String bCode = req.getParameter(IBookConstants.COLUMN_BARCODE);
	        String bName = req.getParameter(IBookConstants.COLUMN_NAME);
	        String bAuthor = req.getParameter(IBookConstants.COLUMN_AUTHOR);
	        int bPrice = Integer.parseInt(req.getParameter(IBookConstants.COLUMN_PRICE));
	        int bQty = Integer.parseInt(req.getParameter(IBookConstants.COLUMN_QUANTITY));
	        
	        Part filePart = req.getPart("image"); // "image" is the name of the form field
	        String myFile = filePart.getSubmittedFileName();
	        
	        String uploadPath = "C:\\Users\\Dell\\eclipse-workspace\\onlinebookstore\\src\\main\\webapp\\images\\";


	        
	        File uploadFolder = new File(uploadPath);
	        if (!uploadFolder.exists()) {
	            uploadFolder.mkdir();
	        }
	        
//	        String imagePath = uploadPath + myFile;
	        try (InputStream inputStream = filePart.getInputStream(); 
	        	 OutputStream outputStream = new FileOutputStream(uploadPath + myFile)) {
	            byte[] buffer = new byte[1024];
	            int bytesRead;
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }
	        }
	        
	        
	        try (Connection con = DBConnection.getCon()) {
	            PreparedStatement ps = con.prepareStatement(
	                "INSERT INTO " + IBookConstants.TABLE_BOOK + " VALUES (?, ?, ?, ?, ?,?)"
	            );
	            ps.setString(1, bCode);
	            ps.setString(2, bName);
	            ps.setString(3, bAuthor);
	            ps.setInt(4, bPrice);
	            ps.setInt(5, bQty);
	            ps.setString(6, "images/" + myFile);

	            int k = ps.executeUpdate();
	            if (k == 1) {
	                req.getRequestDispatcher("AddBook.html").include(req, res);
	                pw.println("<div class=\"tab\">Book Detail Updated Successfully!<br/>Add More Books</div>");
	            } else {
	                req.getRequestDispatcher("AddBook.html").include(req, res);
	                pw.println("<div class=\"tab\">Failed to Add Books! Fill up Carefully</div>");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            pw.println("<div class=\"tab\">An error occurred while adding the book. Please try again.</div>");
	            req.getRequestDispatcher("AddBook.html").include(req, res);
	        }
	    }
}