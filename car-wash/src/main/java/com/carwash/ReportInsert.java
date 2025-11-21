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

@WebServlet("/reportInsert")
public class ReportInsert extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			String complaint = request.getParameter("complaint");
			int workId = Integer.parseInt(request.getParameter("work_id"));

			pst = conn.prepareStatement("INSERT INTO report_details (work_id, complaint,resolved) VALUES (?, ?, ?)");

			pst.setInt(1, workId);
			pst.setString(2, complaint);
			pst.setString(3, "no");

			pst.executeUpdate();

			response.sendRedirect("report");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
