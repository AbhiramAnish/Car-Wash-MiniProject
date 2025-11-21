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
import javax.servlet.http.HttpSession;

@WebServlet("/customer")
public class CustomerControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int i = 1;

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");
			String userType = (String) session.getAttribute("userType");

			out.println("<html>");
			out.println("<body style='font-family: Arial; margin: 20px;'>");

			out.println("<h1 style='color:#333;'>Welcome " + name + "</h1>");

			out.println("<div style='margin-bottom:20px;'>");
			out.println("<a href='" + userType + "' style='margin-right:10px;'>Home</a>");
			
			out.println("<a href='customer' style='margin-right:10px;'>Customer</a>");
			out.println("<a href='logout' style='color:red;'>Logout</a>");
			out.println("</div>");

			out.println("<h2 style='margin-bottom:10px;'>Customer Details</h2>");

			out.println("<form name='customer' method='post' action='customerInsert' "
					+ "style='border:1px solid #ccc; padding:15px; width:350px; border-radius:5px;'>");

			out.println("<label>Select Flat:</label><br>");
			out.println("<select name='flat_id' required " + "style='padding:5px; width:100%; margin-bottom:10px;'>");
			out.println("<option value='' disabled selected>-- Select Flat --</option>");

			pst = conn.prepareStatement("SELECT flat_id, flat_name FROM flat_details");
			rs = pst.executeQuery();
			while (rs.next()) {
				out.println("<option value='" + rs.getInt(1) + "'>" + rs.getString(2) + "</option>");
			}
			out.println("</select>");

			out.println("<label>Customer Name:</label><br>");
			out.println("<input type='text' name='customer_name' required "
					+ "style='padding:5px; width:100%; margin-bottom:10px;'><br>");

			out.println("<label>Flat No:</label><br>");
			out.println("<input type='text' name='flat_no' required "
					+ "style='padding:5px; width:100%; margin-bottom:10px;'><br>");

			out.println("<label>Phone Number:</label><br>");
			out.println("<input type='text' name='customer_phoneNo' required "
					+ "style='padding:5px; width:100%; margin-bottom:10px;'><br>");

			out.println("<input type='submit' value='Save' "
					+ "style='padding:6px 12px; background:#4CAF50; color:white; border:none; border-radius:4px; cursor:pointer;'>");

			out.println("</form>");
			out.println("<br><br>");

			out.println("<table border='1' style='border-collapse:collapse; width:80%;'>");

			out.println("<tr style='background:#f2f2f2;'>");
			out.println("<th>Sl No</th>");
			out.println("<th>Customer Name</th>");
			out.println("<th>Flat No</th>");
			out.println("<th>Flat Name</th>");
			out.println("<th>Phone No</th>");
			out.println("<th>Action</th>");
			out.println("</tr>");

			pst = conn.prepareStatement("SELECT * FROM customer_details");
			rs = pst.executeQuery();

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + rs.getString(3) + "</td>");
				out.println("<td>" + rs.getString(4) + "</td>");

				PreparedStatement pstFlat = conn.prepareStatement("SELECT flat_name FROM flat_details WHERE flat_id=?");
				pstFlat.setInt(1, rs.getInt(2));
				ResultSet rsFlat = pstFlat.executeQuery();
				rsFlat.next();

				out.println("<td>" + rsFlat.getString(1) + "</td>");
				out.println("<td>" + rs.getString(5) + "</td>");

				out.println("<td>" + "<a href='customerEdit?cid=" + rs.getInt(1)
						+ "' style='margin-right:10px;'>Edit</a>" + "<a href='customerDelete?cid=" + rs.getInt(1)
						+ "' style='color:red;'>Delete</a>" + "</td>");

				out.println("</tr>");
				i++;
			}

			out.println("</table>");

			out.println("</body></html>");

		} catch (IOException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
