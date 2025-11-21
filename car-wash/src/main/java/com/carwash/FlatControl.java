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

@WebServlet("/flat")
public class FlatControl extends HttpServlet {

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

			pst = conn.prepareStatement("select flat_id,flat_name,flat_address from flat_details");
			rs = pst.executeQuery();

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");
			String userType = (String) session.getAttribute("userType");

			out.println("<html>");
			out.println("<body style='font-family: Arial; background:#f4f4f4; padding:20px;'>");

			out.println("<h1 style='color:#333;'>Welcome " + name + "</h1>");

			out.println("<div style='margin-bottom:20px;'>");
			out.println("<a href='" + userType + "' style='margin-right:10px; '>Home</a>");

			out.println("<a href='customer' style='margin-right:10px; '>Customer</a>");
			out.println("<a href='logout' style='color:red;'>Logout</a>");
			out.println("</div>");

			out.println("<h2 style='color:#444;'>Flat Details</h2>");

			out.println("<div style='background:white; padding:20px; border-radius:5px; max-width:400px; "
					+ "border:1px solid #ccc;'>");

			out.println("<form name='flat' method='post' action='flatInsert'>");

			out.println("<label style='font-weight:bold;'>Flat Name:</label><br>");
			out.println("<input type='text' name='flat_name' "
					+ "style='width:100%; padding:8px; margin-bottom:15px; border:1px solid #aaa; border-radius:4px;'><br>");

			out.println("<label style='font-weight:bold;'>Flat Address:</label><br>");
			out.println("<input type='text' name='flat_address' "
					+ "style='width:100%; padding:8px; margin-bottom:15px; border:1px solid #aaa; border-radius:4px;'><br>");

			out.println("<input type='submit' value='Save' "
					+ "style='padding:10px 20px; background:#4CAF50; color:white; border:none; cursor:pointer; border-radius:4px;'>");

			out.println("</form>");
			out.println("</div>");

			out.println("<br><br>");

			out.println("<table border='1' style='border-collapse:collapse; width:80%; background:white;'>");
			out.println("<tr style='background:#ddd; text-align:left;'>");
			out.println("<th style='padding:8px;'>Sl No</th>");
			out.println("<th style='padding:8px;'>Flat Name</th>");
			out.println("<th style='padding:8px;'>Flat Address</th>");
			out.println("<th style='padding:8px;'>Action</th>");
			out.println("</tr>");

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td style='padding:8px;'>" + i + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getString(2) + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getString(3) + "</td>");
				out.println("<td style='padding:8px;'><a href='flatEdit?fid=" + rs.getInt(1)
						+ "'>Edit</a> | <a href='flatDelete?fid=" + rs.getInt(1) + "' style='color:red;'>Delete</a></td>");
				out.println("</tr>");
				i++;
			}

			out.println("</table>");

			out.println("</body>");
			out.println("</html>");

		} catch (IOException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
