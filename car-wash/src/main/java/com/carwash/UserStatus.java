package com.carwash;

import java.io.IOException;
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

@WebServlet("/userStatus")
public class UserStatus extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			int uid = Integer.parseInt(request.getParameter("uid"));
			String status = "";

			pst = conn.prepareStatement("select status from user_details where user_id=?");
			pst.setInt(1, uid);

			rs = pst.executeQuery();
			if (rs.next()) {
				status = rs.getString(1);
			}
			if (status.equals("active")) {
				pst = conn.prepareStatement("update user_details set status='inactive' where user_id=?");

				pst.setInt(1, uid);

				pst.executeUpdate();
			} else {
				pst = conn.prepareStatement("update user_details set status='active' where user_id=?");

				pst.setInt(1, uid);

				pst.executeUpdate();
			}

			response.sendRedirect("addUser");

		} catch (IOException | SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}