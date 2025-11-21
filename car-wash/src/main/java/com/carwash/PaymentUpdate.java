package com.carwash;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/paymentUpdate")
public class PaymentUpdate extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int pid = Integer.parseInt(request.getParameter("pid"));
		double amount = Double.parseDouble(request.getParameter("amount"));
		String PaymentDate = request.getParameter("payment_date");
		String mode = request.getParameter("payment_mode");
		String status = request.getParameter("payment_status");

		Date date = Date.valueOf(LocalDate.parse(PaymentDate));

		try {

			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = conn.prepareStatement(
					"UPDATE payment_details SET amount=?, payment_date=?, payment_mode=?, payment_status=? WHERE payment_id=?");

			pst.setDouble(1, amount);
			pst.setDate(2, date);
			pst.setString(3, mode);
			pst.setString(4, status);
			pst.setInt(5, pid);

			pst.executeUpdate();

			response.sendRedirect("payment");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
