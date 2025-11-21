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

@WebServlet("/staffDetailsInserting")
public class StaffDetailsAction extends HttpServlet {
	
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

	            int uid = Integer.parseInt(request.getParameter("uid"));
	            String fullName = request.getParameter("full_name");
	            String dob = request.getParameter("dob");
	            String address = request.getParameter("address");
	            String phoneNo = request.getParameter("phone_no");
	            String aadharNo = request.getParameter("aadhar_no");

	            pst = conn.prepareStatement(
	                "INSERT INTO staff_details (user_id, full_name, address, phone_no, dob, aadhar) VALUES (?, ?, ?, ?, ?, ?)"
	            );

	            pst.setInt(1, uid);
	            pst.setString(2, fullName);
	            pst.setString(3, address);
	            pst.setString(4, phoneNo);
	            pst.setString(5, dob);
	            pst.setString(6, aadharNo);

	            pst.executeUpdate();

	            response.sendRedirect("addUser");

	        } catch (SQLException | ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }
}
