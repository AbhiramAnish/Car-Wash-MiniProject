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

@WebServlet("/staffMonthlySalaryInsert")
public class StaffMonthlySalaryInsert extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int staffId = Integer.parseInt(request.getParameter("staff_id"));
		double salary = Double.parseDouble(request.getParameter("salary"));
		String salaryMonth = request.getParameter("salary_month") + "-01";
		Date date = Date.valueOf(LocalDate.parse(salaryMonth));

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement(
					"INSERT INTO staff_monthly_salary_details (staff_id, salary, salary_month) VALUES (?, ?, ?)");

			pst.setInt(1, staffId);
			pst.setDouble(2, salary);
			pst.setDate(3, date);

			pst.executeUpdate();

			response.sendRedirect("staffMonthlySalary");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
