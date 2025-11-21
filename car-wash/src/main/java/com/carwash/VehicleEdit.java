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

@WebServlet("/vehicleEdit")
public class VehicleEdit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	final String DRIVER = "com.mysql.cj.jdbc.Driver";
	final String URL = "jdbc:mysql://localhost:3306/car_wash";
	final String USER = "root";
	final String PASSWORD = "password";

	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int vid = Integer.parseInt(request.getParameter("vid"));

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);

			pst = conn.prepareStatement("SELECT * FROM vehicle_details WHERE vehicle_id = ?");
			pst.setInt(1, vid);
			rs = pst.executeQuery();

			if (rs.next()) {

				int currentCustomerId = rs.getInt(2);
				String currentVehicleType = rs.getString(3);
				String currentVehicleNo = rs.getString(4);

				out.println("<html><body style='font-family:Arial,sans-serif; padding:20px;'>");
				out.println("<h2 style='color:#333;'>Edit Vehicle</h2>");
				out.println("<div style='margin-bottom:20px;'>");
				
				out.println("<a href='vehicles' style='margin-right:10px;'>Back</a>");
				out.println("<a href='logout'>Logout</a>");
				out.println("</div>");
				out.println("<form method='post' action='vehicleUpdate'>");

				out.println("<input type='hidden' name='vehicle_id' value='" + vid + "'>");

				out.println("Select Customer:<br>");
				PreparedStatement pstCustomer = conn
						.prepareStatement("SELECT customer_id, customer_name FROM customer_details");
				ResultSet rsCustomer = pstCustomer.executeQuery();

				out.println(
						"<select name='customer_id' required style='width:100%; padding:5px; margin-bottom:10px;'>");
				while (rsCustomer.next()) {

					out.println("<option value='" + rsCustomer.getInt(1) + "' "
							+ (rsCustomer.getInt(1) == currentCustomerId ? "selected" : "") + ">"
							+ rsCustomer.getString(2) + "</option>");

				}
				out.println("</select><br><br>");

				out.println("Vehicle Type:<br>");
				out.println(
						"<select name='vehicle_type' required style='width:100%; padding:5px; margin-bottom:10px;'>");

				String[] types = { "car", "bike", "other" };
				for (String type : types) {

					out.println("<option value='" + type + "' " + (type.equals(currentVehicleType) ? "selected" : "")
							+ ">" + type.toUpperCase() + "</option>");

				}

				out.println("</select><br><br>");

				out.println("Vehicle Number:<br>");
				out.println("<input type='text' name='vehicle_no' value='" + currentVehicleNo
						+ "' required style='width:100%; padding:5px; margin-bottom:10px;'><br><br>");

				out.println(
						"<input type='submit' value='Update' style='padding:8px 15px; background-color:#007BFF; color:white; border:none; border-radius:4px; cursor:pointer;'>");
				out.println("</form>");
				out.println("</body></html>");
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
