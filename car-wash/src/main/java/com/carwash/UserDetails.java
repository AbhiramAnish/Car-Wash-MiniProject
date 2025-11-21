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

@WebServlet("/userDetails")
public class UserDetails extends HttpServlet {

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
			int uid = Integer.parseInt(request.getParameter("uid"));
			int staffId = 0;

			pst = conn.prepareStatement("SELECT * FROM staff_details WHERE user_id=?");
			pst.setInt(1, uid);
			rs = pst.executeQuery();

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			HttpSession session = request.getSession(false);
			String userType = (String) session.getAttribute("userType");
			
			if(rs.next()) {
				staffId = rs.getInt(1);

				out.println("<html><body style='font-family:Arial,sans-serif; background-color:#f9f9f9; padding:20px;'>");
				out.println("<h2 style='color:#2c3e50;'>Details of " + rs.getString(3) + "</h2>");
				out.println("<a href='" + userType + "' style='margin-right:15px;'>Home</a>");
				out.println("<a href='addUser' style='margin-right:15px;'>Back</a>");
				out.println("<a href='logout' style='color:red'>Logout</a>");
				out.println("<hr style='margin:20px 0;'>");

				out.println("<p><strong>Full Name:</strong> " + rs.getString(3) + "</p>");
				out.println("<p><strong>Date of Birth (yyyy-MM-dd):</strong> " + rs.getDate(6) + "</p>");
				out.println("<p><strong>Address:</strong> " + rs.getString(4) + "</p>");
				out.println("<p><strong>Phone No:</strong> " + rs.getString(5) + "</p>");
				out.println("<p><strong>Aadhar Number:</strong> " + rs.getString(7) + "</p>");
				out.println("<br>");

				out.println("<form action='userEdit' method='get'>");
				out.println("<input type='hidden' name='staffId' value='" + staffId + "'>");
				out.println("<input type='submit' value='Edit Details' style='padding:6px 12px; background-color:#3498db; color:white; border:none; border-radius:4px;'>");
				out.println("</form>");

				out.println("</body></html>");
			} else {
				out.println("<html><body><h3>No details found for this user.</h3></body></html>");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
