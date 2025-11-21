package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/paymentEdit")
public class PaymentEdit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int pid = Integer.parseInt(request.getParameter("pid"));

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = conn.prepareStatement("SELECT * FROM payment_details WHERE payment_id=?");
			pst.setInt(1, pid);
			ResultSet rs = pst.executeQuery();
			rs.next();

			out.println("<html><body style='font-family: Arial; background:#f5f5f5; padding:20px;'>");
			out.println("<h1 style='color:#333;'>Edit Payment</h1>");
			out.println("<a href='payments' style='margin-right:10px;'>Back</a>");
			out.println("<a href='logout' style='color:red;'>Logout</a><br><br>");

			out.println(
					"<form method='post' action='paymentUpdate' style='background:white; padding:20px; border-radius:8px; width:350px;'>");

			out.println("<input type='hidden' name='pid' value='" + pid + "'>");

			out.println("<label style='font-weight:bold;'>Amount:</label><br>");
			out.println("<input type='number' name='amount' value='" + rs.getDouble(3)
					+ "' required style='padding:5px; width:100%; margin-bottom:10px;'><br>");

			out.println("<label style='font-weight:bold;'>Payment Date:</label><br>");
			out.println("<input type='date' name='payment_date' value='" + rs.getDate(4)
					+ "' required style='padding:5px; width:100%; margin-bottom:10px;'><br>");

			out.println("<label style='font-weight:bold;'>Payment Mode:</label><br>");
			out.println("<select name='payment_mode' style='padding:5px; width:100%; margin-bottom:10px;'>");

			out.println(
					"<option value='cash' " + (rs.getString(5).equals("cash") ? "selected" : "") + ">Cash</option>");
			out.println("<option value='upi' " + (rs.getString(5).equals("upi") ? "selected" : "") + ">UPI</option>");
			out.println(
					"<option value='card' " + (rs.getString(5).equals("card") ? "selected" : "") + ">Card</option>");
			out.println("</select><br>");

			out.println("<label style='font-weight:bold;'>Payment Status:</label><br>");

			out.println("<select name='payment_status' style='padding:5px; width:100%; margin-bottom:10px;'>");
			out.println(
					"<option value='paid' " + (rs.getString(6).equals("paid") ? "selected" : "") + ">Paid</option>");
			out.println("<option value='unpaid' " + (rs.getString(6).equals("unpaid") ? "selected" : "")
					+ ">Unpaid</option>");
			out.println("</select><br>");

			out.println(
					"<input type='submit' value='Update Payment' style='padding:8px 15px; background:green; color:white; border:none; cursor:pointer;'>");
			out.println("</form>");

			out.println("</body></html>");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
