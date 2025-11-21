package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/payments")
public class PaymentControl extends HttpServlet {

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

			out.println("<html><body style='font-family: Arial; background:#f5f5f5; padding:20px;'>");

			out.println("<h1 style='color:#333;'>Welcome " + name + "</h1>");
			out.println("<a style='margin-right:10px; ' href='" + userType + "'>Home</a>");
			out.println("<a style='margin-right:10px;' href='carWash'>Works</a>");
			out.println("<a style='color:red;' href='logout'>Logout</a><br><br>");

			out.println("<h1 style='color:#444;'>Add Payment</h1>");
			out.println(
					"<form method='post' action='paymentInsert' style='background:white; padding:20px; border-radius:8px; width:350px;'>");

			out.println("<label style='font-weight:bold;'>Work Id:</label><br>");

			pst = conn.prepareStatement("SELECT car_wash_id, vehicle_id , status FROM car_wash_details");
			ResultSet rsWork = pst.executeQuery();
			boolean hasWork = false;

			if (rsWork.next()) {
				if (rsWork.getString(3).equals("completed")) {
					hasWork = true;
					pst = conn.prepareStatement("SELECT v.vehicle_no FROM vehicle_details v "
							+ "LEFT JOIN car_wash_details cw ON cw.vehicle_id = v.vehicle_id "
							+ "WHERE v.vehicle_id = ?");
					pst.setInt(1, rsWork.getInt(2));
					rs = pst.executeQuery();
					rs.next();

					out.println("<select name='work_id' required style='padding:5px; width:100%; margin:5px 0;'>");
					out.println("<option disabled selected>-- Select Work Id --</option>");

					do {
						out.println("<option value='" + rsWork.getInt(1) + "'>" + rsWork.getInt(1) + " -> "
								+ rs.getString(1) + "</option>");
					} while (rsWork.next());

					out.println("</select><br><br>");
				} else {
					out.println(
							" <a href='carWash'>Complete Work First</a><br><br>");
				}
			} else {
				out.println(
						"<a href='carWash'>Add Work</a><br><br>");
			}

			out.println("<label style='font-weight:bold;'>Amount:</label><br>");
			out.println(
					"<input type='number' name='amount' required style='padding:5px; width:100%; margin-bottom:10px;'><br>");

			out.println("<label style='font-weight:bold;'>Payment Date:</label><br>");
			out.println(
					"<input type='date' name='payment_date' required style='padding:5px; width:100%; margin-bottom:10px;'><br>");

			out.println("<label style='font-weight:bold;'>Payment Mode:</label><br>");
			out.println("<select name='payment_mode' required style='padding:5px; width:100%; margin-bottom:10px;'>");
			out.println("<option value='cash'>Cash</option>");
			out.println("<option value='upi'>UPI</option>");
			out.println("<option value='card'>Card</option>");
			out.println("</select><br>");

			out.println("<label style='font-weight:bold;'>Payment Status:</label><br>");
			out.println("<select name='payment_status' required style='padding:5px; width:100%; margin-bottom:10px;'>");
			out.println("<option value='paid'>Paid</option>");
			out.println("<option value='unpaid'>Unpaid</option>");
			out.println("</select><br>");

			if (hasWork) {
				out.println(
						"<input type='submit' value='Save Payment' style='padding:8px 15px; background:green; color:white; border:none; cursor:pointer;'>");
			} else {
				out.println(
						"<input type='submit' value='Save Payment' disabled style='padding:8px 15px; background:gray; color:white; border:none;'>");
			}

			out.println("</form><br><hr style='margin-top:30px;'>");

			out.println("<h2 style='color:#444;'>Existing Payments</h2>");

			pst = conn.prepareStatement("SELECT * FROM payment_details");
			rs = pst.executeQuery();

			out.println("<table border='1' style='border-collapse:collapse; width:100%; background:white;'>");
			out.println("<tr style='background:#ddd;'>");
			out.println("<th style='padding:8px;'>Sl No</th>");
			out.println("<th style='padding:8px;'>Work Id</th>");
			out.println("<th style='padding:8px;'>Amount</th>");
			out.println("<th style='padding:8px;'>Date</th>");
			out.println("<th style='padding:8px;'>Mode</th>");
			out.println("<th style='padding:8px;'>Status</th>");
			out.println("<th style='padding:8px;'>Action</th>");
			out.println("</tr>");

			int i = 1;

			while (rs.next()) {
				int pid = rs.getInt("payment_id");

				out.println("<tr>");
				out.println("<td style='padding:8px;'>" + i + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getInt("car_wash_id") + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getDouble("amount") + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getDate("payment_date") + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getString("payment_mode") + "</td>");
				out.println("<td style='padding:8px;'>" + rs.getString("payment_status") + "</td>");

				out.println("<td style='padding:8px;'><a href='paymentEdit?pid=" + pid + "'>Edit</a> | "
						+ "<a href='paymentDelete?pid=" + pid + "' style='color:red;'>Delete</a></td>");

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
