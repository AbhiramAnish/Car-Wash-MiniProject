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

@WebServlet("/staffSalaryInsert")
public class StaffSalaryInsert extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int staffId = Integer.parseInt(request.getParameter("staff_id"));
		double basicSalary = Double.parseDouble(request.getParameter("basic_salary"));

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement("INSERT INTO staff_salary_details (staff_id, basic_salary) VALUES (?, ?)");
			pst.setInt(1, staffId);
			pst.setDouble(2, basicSalary);

			pst.executeUpdate();

			response.sendRedirect("staffSalary");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
