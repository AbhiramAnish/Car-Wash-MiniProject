package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/paymentInsert")
public class PaymentInsert extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int workId = Integer.parseInt(request.getParameter("work_id"));
		double amount = Double.parseDouble(request.getParameter("amount"));
		String paymentDate = request.getParameter("payment_date");
		String mode = request.getParameter("payment_mode");
		String status = request.getParameter("payment_status");

		Date date = Date.valueOf(LocalDate.parse(paymentDate));

		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = conn.prepareStatement(
					"INSERT INTO payment_details(car_wash_id,amount,payment_date,payment_mode,payment_status) VALUES (?,?,?,?,?)");

			pst.setInt(1, workId);
			pst.setDouble(2, amount);
			pst.setDate(3, date);
			pst.setString(4, mode);
			pst.setString(5, status);

			pst.executeUpdate();

			response.sendRedirect("payments");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
