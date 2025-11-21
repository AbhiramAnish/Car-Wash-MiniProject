package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/staffSalaryEdit")
public class StaffSalaryEdit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int salaryId = Integer.parseInt(request.getParameter("sid"));

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement("SELECT staff_id, basic_salary FROM staff_salary_details WHERE salary_id=?");
			pst.setInt(1, salaryId);
			rs = pst.executeQuery();

			if (rs.next()) {

				out.println(
						"<html><body style='font-family: Arial, sans-serif; background-color:#f5f5f5; padding:20px;'>");
				out.println("<h2 style='color:#2c3e50;'>Edit Staff Salary</h2>");
				

				out.println("<a href='staffSalary' style='margin-right:10px;'>Back</a>");
				out.println("<a href='logout' style='color:red;'>Logout</a><br><br>");
				
				out.println("<form method='post' action='staffSalaryUpdate' style='margin-top:20px;'>");

				out.println("<input type='hidden' name='salary_id' value='" + salaryId + "'>");

				out.println("Select Staff:<br>");
				PreparedStatement pstStaff = conn.prepareStatement("SELECT staff_id, full_name FROM staff_details");
				ResultSet rsStaff = pstStaff.executeQuery();

				out.println(
						"<select name='staff_id' required style='padding:5px; margin-top:5px; margin-bottom:10px; width:300px;'>");
				while (rsStaff.next()) {
					if (rsStaff.getInt(1) == rs.getInt(1)) {
						out.println("<option value='" + rsStaff.getInt(1) + "' selected>" + rsStaff.getString(2)
								+ "</option>");
					} else {
						out.println("<option value='" + rsStaff.getInt(1) + "'>" + rsStaff.getString(2) + "</option>");
					}
				}
				out.println("</select><br><br>");

				out.println("Basic Salary:<br>");
				out.println("<input type='number' name='basic_salary' value='" + rs.getDouble(2)
						+ "' required style='padding:5px; margin-top:5px; margin-bottom:10px; width:200px;'><br><br>");

				out.println(
						"<input type='submit' value='Update Salary' style='padding:5px 10px; background-color:#27ae60; color:white; border:none; border-radius:5px;'>");

				out.println("</form>");
				out.println("</body></html>");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
