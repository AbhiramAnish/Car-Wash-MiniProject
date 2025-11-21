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

@WebServlet("/carWashEdit")
public class CarWashEdit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int wid = Integer.parseInt(request.getParameter("wid"));

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {

			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn
					.prepareStatement("SELECT vehicle_id, wash_date, status FROM car_wash_details WHERE car_wash_id=?");
			pst.setInt(1, wid);
			rs = pst.executeQuery();

			if (rs.next()) {

				out.println("<html><body style='font-family: Arial; margin: 20px;'>");

				out.println("<h2 style='text-align:center;'>Edit Car Wash Details</h2>");
				out.println("<div style='margin-bottom:20px; text-align:center;'>");
				
				out.println("<a href='carWash' style='margin-right:10px;'>Back</a>");
				out.println("<a href='logout' style='color:red;'>Logout</a><br><br>");
				out.println("</div>");

				out.println("<form method='post' action='carWashUpdate' "
						+ "style='max-width: 400px; margin: auto; padding: 20px; border: 1px solid #ccc; border-radius: 6px;'>");

				out.println("<input type='hidden' name='wid' value='" + wid + "'>");

				out.println("<label style='font-weight:bold;'>Select Vehicle:</label><br>");
				PreparedStatement pstDemo = conn.prepareStatement("SELECT vehicle_id, vehicle_no FROM vehicle_details");
				ResultSet rsDemo = pstDemo.executeQuery();

				out.println(
						"<select name='vehicle_id' required style='width:100%; padding:7px; margin-top:5px; margin-bottom:15px;'>");

				while (rsDemo.next()) {

					if (rsDemo.getInt(1) == rs.getInt(1)) {
						out.println("<option value='" + rsDemo.getInt(1) + "' selected>" + rsDemo.getString(2)
								+ "</option>");
					} else {
						out.println("<option value='" + rsDemo.getInt(1) + "'>" + rsDemo.getString(2) + "</option>");
					}
				}
				out.println("</select>");

				out.println("<label style='font-weight:bold;'>Wash Date:</label><br>");
				out.println("<input type='date' name='wash_date' value='" + rs.getDate(2)
						+ "' required style='width:100%; padding:7px; margin-top:5px; margin-bottom:15px;'><br>");

				out.println("<label style='font-weight:bold;'>Status:</label><br>");
				out.println("<select name='status' required "
						+ "style='width:100%; padding:7px; margin-top:5px; margin-bottom:15px;'>");

				out.println("<option value='pending'" + (rs.getString(3).equals("pending") ? "selected" : "")
						+ " selected>Pending</option>");
				out.println("<option value='completed'" + (rs.getString(3).equals("completed") ? "selected" : "")
						+ ">Completed</option>");

				out.println("</select>");

			
				out.println("<input type='submit' value='Update' "
						+ "style='background:#4CAF50; color:white; padding:10px 20px; "
						+ "border:none; width:100%; border-radius:4px; cursor:pointer;'>");

				out.println("</form>");
				out.println("</body></html>");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
