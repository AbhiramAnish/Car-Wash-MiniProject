package com.carwash;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/paymentDelete")
public class PaymentDelete extends HttpServlet {

	private static final long serialVersionUID = 1L;
	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int pid = Integer.parseInt(request.getParameter("pid"));

		try {
			Class.forName(DRIVER);
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

			PreparedStatement pst = conn.prepareStatement("DELETE FROM payment_details WHERE payment_id=?");
			pst.setInt(1, pid);

			pst.executeUpdate();

			response.sendRedirect("payment");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
