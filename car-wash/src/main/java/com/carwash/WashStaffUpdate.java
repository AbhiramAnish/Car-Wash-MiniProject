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

@WebServlet("/washStaffUpdate")
public class WashStaffUpdate extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			int wsid = Integer.parseInt(request.getParameter("wsid"));
			int carWashId = Integer.parseInt(request.getParameter("car_wash_id"));
			int staffId = Integer.parseInt(request.getParameter("staff_id"));

			pst = conn
					.prepareStatement("UPDATE car_wash_staff SET car_wash_id=?, staff_id=? WHERE car_wash_staff_id=?");

			pst.setInt(1, carWashId);
			pst.setInt(2, staffId);
			pst.setInt(3, wsid);

			pst.executeUpdate();

			response.sendRedirect("washStaff");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
