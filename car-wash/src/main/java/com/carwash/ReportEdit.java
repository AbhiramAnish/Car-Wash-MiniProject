package com.carwash;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reportEdit")
public class ReportEdit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			int rid = Integer.parseInt(request.getParameter("rid"));

			pst = conn.prepareStatement("SELECT resolved FROM report_details WHERE report_id=?");
			pst.setInt(1, rid);
			rs = pst.executeQuery();
			rs.next();

			if (rs.getString(1).equals("yes")) {
				pst = conn.prepareStatement("UPDATE report_details SET resolved=? WHERE report_id=?");
				pst.setString(1, "no");
				pst.setInt(2, rid);
				pst.executeUpdate();
			} else {
				pst = conn.prepareStatement("UPDATE report_details SET resolved=? WHERE report_id=?");
				pst.setString(1, "yes");
				pst.setInt(2, rid);
				pst.executeUpdate();
			}
			response.sendRedirect("report");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
