package com.carwash;

import java.io.IOException;
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

@WebServlet("/vehicleUpdate")
public class VehicleUpdate extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int vehicleId = Integer.parseInt(request.getParameter("vehicle_id"));
		int customerId = Integer.parseInt(request.getParameter("customer_id"));
		String vehicleType = request.getParameter("vehicle_type");
		String vehicleNo = request.getParameter("vehicle_no");

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement(
					"UPDATE vehicle_details SET customer_id = ?, vehicle_type = ?, vehicle_no = ? WHERE vehicle_id = ?");

			pst.setInt(1, customerId);
			pst.setString(2, vehicleType);
			pst.setString(3, vehicleNo);
			pst.setInt(4, vehicleId);

			pst.executeUpdate();

			response.sendRedirect("vehicles");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
