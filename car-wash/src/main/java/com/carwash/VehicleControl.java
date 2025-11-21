package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/vehicles")
public class VehicleControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String URL = "jdbc:mysql://localhost:3306/car_wash";
    final String USER = "root";
    final String PASSWORD = "password";

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            HttpSession session = request.getSession(false);
            String name = (String) session.getAttribute("user");
            String userType = (String) session.getAttribute("userType");

            out.println("<html><body style='font-family:Arial,sans-serif; padding:20px;'>");

            out.println("<h1 style='color:#333;'>Welcome " + name + "</h1>");
            out.println("<a href='" + userType + "'>Home</a> | ");
            out.println("<a href='logout'>Logout</a><br><br>");

            out.println("<h1 style='color:#333;'>Add Vehicle</h1>");
            out.println("<form method='post' action='vehicleInsert'>");

            boolean hasCustomer = false;

           
            out.println("Select Customer:<br>");
            pst = conn.prepareStatement("SELECT customer_id, customer_name FROM customer_details");
            rs = pst.executeQuery();

            if (rs.next()) {
                hasCustomer = true;
                out.println("<select name='customer_id' required style='width:100%; padding:5px; margin-bottom:10px;'>");
                out.println("<option disabled selected>-- Select Customer --</option>");
                do {
                    out.println("<option value='" + rs.getInt(1) + "'>" + rs.getString(2) + "</option>");
                } while (rs.next());
                out.println("</select><br><br>");
            } else {
                out.println("<a href='customer'>Add Customer</a><br><br>");
            }

            out.println("Vehicle Type:<br>");
            out.println("<select name='vehicle_type' required style='width:100%; padding:5px; margin-bottom:10px;'>");
            out.println("<option disabled selected>-- Select Type --</option>");
            out.println("<option value='car'>Car</option>");
            out.println("<option value='bike'>Bike</option>");
            out.println("<option value='other'>Other</option>");
            out.println("</select><br><br>");

            out.println("Vehicle Number:<br>");
            out.println("<input type='text' name='vehicle_no' required style='width:100%; padding:5px; margin-bottom:10px;'><br><br>");

            if (hasCustomer) {
                out.println("<input type='submit' value='Save Vehicle' style='padding:8px 15px; background-color:#007BFF; color:white; border:none; border-radius:4px; cursor:pointer;'>");
            } else {
                out.println("<input type='submit' value='Save Vehicle' disabled style='padding:8px 15px; background-color:#ccc; color:white; border:none; border-radius:4px;'>");
            }

            out.println("</form><br><hr>");

            out.println("<h2 style='color:#333;'>Existing Vehicles</h2>");

            pst = conn.prepareStatement("SELECT vehicle_id, customer_id, vehicle_type, vehicle_no FROM vehicle_details");
            rs = pst.executeQuery();

            out.println("<table border='1' style='border-collapse:collapse; width:100%;'>");
            out.println("<tr style='background-color:#f2f2f2;'>");
            out.println("<th>Sl No</th>");
            out.println("<th>Customer Name</th>");
            out.println("<th>Vehicle Type</th>");
            out.println("<th>Vehicle No</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");

            int i = 1;

            while (rs.next()) {

                int vid = rs.getInt(1);
                int customerId = rs.getInt(2);

                
                PreparedStatement pstCus = conn.prepareStatement("SELECT customer_name FROM customer_details WHERE customer_id=?");
                pstCus.setInt(1, customerId);
                ResultSet rsCus = pstCus.executeQuery();
                String customerName = "";
                if (rsCus.next()) {
                    customerName = rsCus.getString(1);
                }

                out.println("<tr>");
                out.println("<td>" + i + "</td>");
                out.println("<td>" + customerName + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("<td><a href='vehicleEdit?vid=" + vid + "'>Edit</a> | "
                        + "<a href='vehicleDelete?vid=" + vid + "'>Delete</a></td>");
                out.println("</tr>");

                i++;
            }

            out.println("</table>");
            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
