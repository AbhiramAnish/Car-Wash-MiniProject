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

@WebServlet("/washStaffDelete")
public class WashStaffDelete extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			int wsid = Integer.parseInt(request.getParameter("wsid"));

			pst = conn.prepareStatement("DELETE FROM car_wash_staff WHERE car_wash_staff_id = ?");
			pst.setInt(1, wsid);

			pst.executeUpdate();

			response.sendRedirect("washStaff");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
