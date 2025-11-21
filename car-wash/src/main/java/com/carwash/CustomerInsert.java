package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/customerInsert")
public class CustomerInsert extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			int flatId = Integer.parseInt(request.getParameter("flat_id"));
			String customerName = request.getParameter("customer_name");
			String flatNo = request.getParameter("flat_no");
			String phoneNo = request.getParameter("customer_phoneNo");

			pst = conn.prepareStatement(
					"INSERT INTO customer_details (flat_id, customer_name, flat_no, customer_phone) VALUES (?, ?, ?, ?)");

			pst.setInt(1, flatId);
			pst.setString(2, customerName);
			pst.setString(3, flatNo);
			pst.setString(4, phoneNo);

			pst.executeUpdate();

			response.sendRedirect("customer");

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
