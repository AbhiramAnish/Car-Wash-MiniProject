package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/customerEdit")
public class CustomerEdit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			int cid = Integer.parseInt(request.getParameter("cid"));
			pst = conn.prepareStatement("SELECT * FROM customer_details WHERE customer_id=?");
			pst.setInt(1, cid);
			rs = pst.executeQuery();

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			out.println("<html>");
			out.println("<body style='font-family: Arial; margin: 20px;'>");

			out.println("<h1 style='color:#333;'>Update Customer Details</h1><br>");
			
			out.println("<div style='margin-bottom:20px;'>");
			
			out.println("<a href='customer' style='margin-right:10px;'>Back</a>");
			out.println("<a href='logout' style='color:red;'>Logout</a>");
			out.println("</div>");

			if (rs.next()) {

				out.println("<form name='customer' method='post' action='customerUpdate' "
						+ "style='border:1px solid #ccc; padding:20px; width:350px; border-radius:5px;'>");

				out.println("<input type='hidden' name='customer_id' value='" + rs.getInt(1) + "'>");

				out.println("<label>Customer Name:</label><br>");
				out.println("<input type='text' name='customer_name' value='" + rs.getString(3) + "' "
						+ "style='padding:6px; width:100%; margin-bottom:10px;' required><br>");

				out.println("<label>Flat No:</label><br>");
				out.println("<input type='text' name='flat_no' value='" + rs.getString(4) + "' "
						+ "style='padding:6px; width:100%; margin-bottom:10px;' required><br>");

				out.println("<label>Select Flat:</label><br>");
				out.println("<select name='flat_id' style='padding:6px; width:100%; margin-bottom:10px;' required>");

				PreparedStatement pstFlats = conn.prepareStatement("SELECT * FROM flat_details");
				ResultSet rsFlats = pstFlats.executeQuery();

				while (rsFlats.next()) {
					out.println("<option value='" + rsFlats.getInt(1) + "'"
							+ (rsFlats.getInt(1) == rs.getInt(2) ? "selected" : " ") + " >" + rsFlats.getString(2)
							+ "</option>");
				}

				out.println("</select><br>");

				out.println("<label>Phone Number:</label><br>");
				out.println("<input type='text' name='customer_phoneNo' value='" + rs.getString(5) + "' "
						+ "style='padding:6px; width:100%; margin-bottom:15px;' required><br>");

				out.println("<input type='submit' value='Update Customer' "
						+ "style='padding:8px 15px; background:#4CAF50; color:white; border:none; border-radius:5px; cursor:pointer;'>");

				out.println("</form>");
			}

			out.println("</body></html>");

		} catch (IOException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
