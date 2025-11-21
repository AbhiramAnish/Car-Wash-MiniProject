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

@WebServlet("/staffDesignationEdit")
public class StaffDesignationEdit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int sid = Integer.parseInt(request.getParameter("sdid")); // staff_designation_id

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement(
					"SELECT staff_id, designation_id, join_date FROM staff_designation_details WHERE staff_designation_id=?");
			pst.setInt(1, sid);
			rs = pst.executeQuery();

			while (rs.next()) {
				out.println("<html>");
				out.println("<body style='font-family: Arial, sans-serif; background-color:#f9f9f9; padding:20px;'>");
				out.println("<h2 style='color:#2c3e50;'>Edit Staff Designation</h2>");
				out.println("<div style='margin-bottom:20px;'>");
	            
	            out.println("<a href='staffDesignation' style='margin-right:10px;'>Back</a>");
	      
	            out.println("<a href='logout' style='color:red;'>Logout</a>");
	            out.println("</div>");
				out.println("<form method='post' action='staffDesignationUpdate' style='margin-top:20px;'>");
				out.println("<input type='hidden' name='sdid' value='" + sid + "'>");

				out.println("Select Staff:<br>");
				PreparedStatement pstStaff = conn.prepareStatement("SELECT staff_id, full_name FROM staff_details");
				ResultSet rsStaff = pstStaff.executeQuery();
				out.println(
						"<select name='staff_id' required style='padding:5px; margin-top:5px; margin-bottom:10px;'>");
				while (rsStaff.next()) {

					out.println("<option value='" + rsStaff.getInt(1) + "' "
							+ (rs.getInt(1) == rsStaff.getInt(1) ? "selected" : "") + ">" + rsStaff.getString(2)
							+ "</option>");

				}
				out.println("</select><br><br>");

				out.println("Select Designation:<br>");
				PreparedStatement pstDesig = conn
						.prepareStatement("SELECT designation_id, designation_name FROM designation_details");
				ResultSet rsDesig = pstDesig.executeQuery();
				out.println(
						"<select name='designation_id' required style='padding:5px; margin-top:5px; margin-bottom:10px;'>");
				while (rsDesig.next()) {

					out.println("<option value='" + rsDesig.getInt(1) + "' "
							+ (rsDesig.getInt(1) == rs.getInt(2) ? "selected" : "") + ">" + rsDesig.getString(2)
							+ "</option>");

				}
				out.println("</select><br><br>");

				out.println("Joining Date:<br>");
				out.println("<input type='date' name='joining_date' value='" + rs.getDate(3)
						+ "' required style='padding:5px; margin-top:5px; margin-bottom:10px;'><br><br>");

				out.println(
						"<input type='submit' value='Update' style='padding:5px 10px; background-color:#27ae60; color:white; border:none; border-radius:5px;'>");

				out.println("</form>");
				out.println("</body></html>");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
