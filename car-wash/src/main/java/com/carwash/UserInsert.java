package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/insertUser")
public class UserInsert extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session=request.getSession(false);
		String userType=(String)session.getAttribute("userType");
		
		String userName = request.getParameter("user_name");
		String password = request.getParameter("password");
		String rePassword = request.getParameter("check_password");
		String userRole = request.getParameter("user_role").toLowerCase();

		try {

			if (password.equals(rePassword)) {
				Class.forName(DRIVER);
				conn = DriverManager.getConnection(URL, USER, PASSWORD);

				pst = conn.prepareStatement("INSERT INTO user_details (user_name, user_password, user_role) values (?, ?, ?)");
				pst.setString(1, userName);
				pst.setString(2, password);
				pst.setString(3, userRole);
				pst.executeUpdate();
				pst = conn.prepareStatement("Select LAST_INSERT_ID()");
				ResultSet rs=pst.executeQuery();
				rs.next();
				int userId=rs.getInt(1);
				
				if(userRole.equals("staff")) {
					response.sendRedirect("staffDetails?uid="+userId);
				}else {
					response.sendRedirect("addUser");
				}
				
			} else {
				response.sendRedirect(userType);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
