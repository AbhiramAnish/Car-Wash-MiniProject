package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/report")
public class ReportControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {

			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			HttpSession session = request.getSession(false);
			String name = (String) session.getAttribute("user");
			String userType = (String) session.getAttribute("userType");

			out.println("<html><body style='font-family:Arial; background:#f5f5f5; padding:20px;'>");

			out.println("<h1 style='color:#333;'>Welcome " + name + "</h1>");
			out.println("<a href='" + userType + "'>Home</a> | ");
			out.println("<a href='carWash'>Works</a> | ");
			out.println("<a href='logout' style='color:red'>Logout</a><br><br>");

			out.println("<h1 style='color:#444;'>Add Complaint</h1>");
			out.println(
					"<form method='post' action='reportInsert' style='background:white; padding:20px; border-radius:8px; width:400px;'>");

			out.println("<label style='font-weight:bold;'>Work Id:</label><br>");

			pst = conn.prepareStatement("SELECT car_wash_id,vehicle_id FROM car_wash_details");
			ResultSet rsWork = pst.executeQuery();
			boolean hasWork = false;

			if (rsWork.next()) {
				hasWork = true;
				pst = conn.prepareStatement(
						"SELECT v.vehicle_no FROM vehicle_details as v left join car_wash_details as cw "
								+ " on cw.vehicle_id=v.vehicle_id  where v.vehicle_id=?");
				pst.setInt(1, rsWork.getInt(2));
				rs = pst.executeQuery();
				rs.next();

				out.println("<select name='work_id' required style='padding:5px; width:100%; margin-bottom:10px;'>");
				out.println("<option disabled selected>-- Select Work Id --</option>");
				do {
					out.println("<option value='" + rsWork.getInt(1) + "'>" + rsWork.getInt(1) + " -> "
							+ rs.getString(1) + "</option>");
				} while (rsWork.next());
				out.println("</select><br><br>");
			} else {
				out.println("No car wash records found. <a href='carWash'>Add Car Wash</a><br><br>");
			}

			out.println("<label style='font-weight:bold;'>Complaint:</label><br>");
			out.println(
					"<textarea name='complaint' required placeholder='Describe your complaint' style='padding:5px; width:100%; height:80px; margin-bottom:10px;'></textarea><br><br>");

			if (hasWork) {
				out.println(
						"<input type='submit' value='Save Complaint' style='padding:8px 15px; background:green; color:white; border:none; cursor:pointer;'>");
			} else {
				out.println(
						"<input type='submit' value='Save Complaint' disabled style='padding:8px 15px; background:grey; color:white; border:none; cursor:not-allowed;'>");
			}

			out.println("</form><br><hr>");

			out.println("<h2 style='color:#444;'>Existing Complaints</h2>");

			pst = conn.prepareStatement("SELECT * FROM report_details");
			rs = pst.executeQuery();

			out.println("<table border='1' style='border-collapse:collapse; width:80%; background:white;'>");
			out.println("<tr style='background:#ddd;'>");
			out.println("<th style='padding:8px;'>Sl No</th>");
			out.println("<th style='padding:8px;'>Complaint</th>");
			out.println("<th style='padding:8px;'>Work Id</th>");
			out.println("<th style='padding:8px;'>Resolved</th>");
			out.println("<th style='padding:8px;'>Action</th>");
			out.println("</tr>");

			int i = 1;

			while (rs.next()) {

				out.println("<tr>");
				out.println("<td style='padding:8px;'>" + i + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getString(3) + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getInt(2) + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getString(4) + "</td>");

				out.println("<td style='padding:8px;'><a href='reportEdit?rid=" + rs.getInt(1) + "'>Resolved?</a> | "
						+ "<a href='reportDelete?rid=" + rs.getInt(1) + "' style='color:red'>Delete</a></td>");
				out.println("</tr>");

				i++;
			}

			out.println("</table>");
			out.println("</body></html>");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
