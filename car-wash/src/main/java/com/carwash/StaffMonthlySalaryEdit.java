package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/staffMonthlySalaryEdit")
public class StaffMonthlySalaryEdit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		int monthlySalaryId = Integer.parseInt(request.getParameter("msid"));

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement(
					"SELECT staff_id, salary, salary_month FROM staff_monthly_salary_details WHERE monthly_salary_id=?");
			pst.setInt(1, monthlySalaryId);
			rs = pst.executeQuery();

			if (rs.next()) {

				double salary = rs.getDouble(2);
				String salaryMonth = rs.getString(3);

				String formattedMonth = salaryMonth.substring(0, 7);

				out.println("<html>");
				out.println("<body style='font-family:Arial; background:#f8f8f8; padding:20px;'>");

				out.println("<h2 style='color:#2c3e50;'>Edit Monthly Salary</h2>");
				
				out.println("<a href='staffMonthlySalary' style='margin-right:10px;'>Back</a>");
				out.println("<a href='logout' style='color:red;'>Logout</a><br><br>");

				out.println("<form method='post' action='staffMonthlySalaryUpdate' style='margin-top:20px;'>");

				out.println("<input type='hidden' name='monthly_salary_id' value='" + monthlySalaryId + "'>");

				out.println("Select Staff :<br>");

				PreparedStatement pstStaff = conn.prepareStatement("SELECT staff_id, full_name FROM staff_details");
				ResultSet rsStaff = pstStaff.executeQuery();
				if (rsStaff.next()) {

					out.println("<select name='staff_id' required style='padding:5px; width:300px; margin-top:5px;'>");

					do {
						out.println("<option value='" + rsStaff.getInt(1) + "' "
								+ (rsStaff.getInt(1) == rs.getInt(1) ? "selected" : "") + ">" + rsStaff.getString(2)
								+ "</option>");
					} while (rs.next());
					out.println("</select><br><br>");
				}

				out.println("Salary Month:<br>");
				out.println("<input type='month' name='salary_month' value='" + formattedMonth
						+ "' required style='padding:5px; margin-bottom:10px; width:200px;'><br>");

				out.println("Salary:<br>");
				out.println("<input type='number' name='salary' value='" + salary
						+ "' required style='padding:5px; margin-bottom:10px; width:200px;'><br>");

				out.println("<input type='submit' value='Update' "
						+ "style='padding:6px 12px; background:#2980b9; color:white; border:none; border-radius:4px;'>");

				out.println("</form>");

				out.println("</body></html>");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
