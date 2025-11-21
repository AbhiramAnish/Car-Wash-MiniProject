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

@WebServlet("/staffMonthlySalaryUpdate")
public class StaffMonthlySalaryUpdate extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int monthlyId = Integer.parseInt(request.getParameter("monthly_salary_id"));
		int staffId = Integer.parseInt(request.getParameter("staff_id"));
		String salaryMonth = request.getParameter("salary_month") + "-01";
		double salary = Double.parseDouble(request.getParameter("salary"));

		Date date = Date.valueOf(LocalDate.parse(salaryMonth));

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement(
					"UPDATE staff_monthly_salary_details SET staff_id=?, salary_month=?, salary=? WHERE monthly_salary_id=?");

			pst.setInt(1, staffId);
			pst.setDate(2, date);
			pst.setDouble(3, salary);
			pst.setInt(4, monthlyId);

			pst.executeUpdate();

			response.sendRedirect("staffMonthlySalary");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
