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

@WebServlet("/staffDesignationInsert")
public class StaffDesignationInsert extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int staffId = Integer.parseInt(request.getParameter("staff_id"));
		int designationId = Integer.parseInt(request.getParameter("designation_id"));
		String joiningDate = request.getParameter("joining_date");
		Date date=Date.valueOf(LocalDate.parse(joiningDate));

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			
			pst = conn.prepareStatement("INSERT INTO staff_designation_details (staff_id, designation_id, join_date) VALUES (?, ?, ?)");
			pst.setInt(1, staffId);
			pst.setInt(2, designationId);
			pst.setDate(3, date);

			pst.executeUpdate();

			response.sendRedirect("staffDesignation");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
