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

@WebServlet("/flatUpdate")
public class FlatUpdate extends HttpServlet {

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
			int fid = Integer.parseInt(request.getParameter("flat_id"));
			String flatName = request.getParameter("flat_name").toLowerCase();
			String flatAddress = request.getParameter("flat_address").toLowerCase();

			pst = conn.prepareStatement("update flat_details set flat_name=?,flat_address=? where flat_id=?");

			pst.setString(1, flatName);
			pst.setString(2, flatAddress);
			pst.setInt(3, fid);

			pst.executeUpdate();

			response.sendRedirect("flat");

		} catch (IOException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}