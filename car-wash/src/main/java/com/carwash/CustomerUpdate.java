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

@WebServlet("/customerUpdate")
public class CustomerUpdate extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			int customer_id = Integer.parseInt(request.getParameter("customer_id"));
			int flat_id = Integer.parseInt(request.getParameter("flat_id"));
			String customer_name = request.getParameter("customer_name");
			String flat_no = request.getParameter("flat_no");
			String customer_phoneNo = request.getParameter("customer_phoneNo");

			pst = conn.prepareStatement(
					"UPDATE customer_details SET flat_id=?, customer_name=?, flat_no=?, customer_phone=? WHERE customer_id=?");

			pst.setInt(1, flat_id);
			pst.setString(2, customer_name);
			pst.setString(3, flat_no);
			pst.setString(4, customer_phoneNo);
			pst.setInt(5, customer_id);
			pst.executeUpdate();

			response.sendRedirect("customer");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
