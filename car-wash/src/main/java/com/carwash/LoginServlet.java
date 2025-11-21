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

@WebServlet("/login")

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	final String DRIVER="com.mysql.cj.jdbc.Driver";
	final String URL="jdbc:mysql://localhost:3306/car_wash";
	final String USER="root";
	final String PASSWORD="password";
	
	String userName="";
	String role="";
	String status="";
	
	Connection conn=null;
	PreparedStatement pst=null;
	ResultSet rs=null;

	public void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException, IOException {

		try {
			
			role="";
			Class.forName(DRIVER);
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
			String user=req.getParameter("user");
			String password=req.getParameter("password");
			pst=conn.prepareStatement("select user_name,user_role,status from user_details where user_name=? and user_password=?");
			pst.setString(1, user);
			pst.setString(2, password);
			
			rs=pst.executeQuery();
			
			while(rs.next()) {
				userName=rs.getString(1);
				role=rs.getString(2);
				status=rs.getString(3);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		HttpSession session=req.getSession();
		session.setAttribute("user", userName);
		session.setAttribute("userType", role);
		
		if(role.equals("admin") && status.equals("active")) {
			res.sendRedirect("admin");
		}else if(role.equals("staff") && status.equals("active")) {
			res.sendRedirect("staff");
		}else {
			
			res.sendRedirect("index.html");
		}
		
	}
}
