package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/washStaffInsert")
public class WashStaffInsert extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		

		int car_wash_id = Integer.parseInt(request.getParameter("car_wash_id"));
		int staff_id = Integer.parseInt(request.getParameter("staff_id"));

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement("INSERT INTO car_wash_staff(car_wash_id, staff_id) VALUES(?, ?)");

			pst.setInt(1, car_wash_id);
			pst.setInt(2, staff_id);

			pst.executeUpdate();

			response.sendRedirect("washStaff");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
