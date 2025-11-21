package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/carWashInsert")
public class CarWashInsert extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int vehicleId = Integer.parseInt(request.getParameter("vehicle_id"));
		String washDate = request.getParameter("wash_date");
		String status = request.getParameter("status");

		Date date = Date.valueOf(LocalDate.parse(washDate));

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement("INSERT INTO car_wash_details (vehicle_id, wash_date, status) VALUES (?, ?, ?)");

			pst.setInt(1, vehicleId);
			pst.setDate(2, date);
			pst.setString(3, status);

			pst.executeUpdate();

			response.sendRedirect("carWash");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
