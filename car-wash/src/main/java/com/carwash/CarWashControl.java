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

@WebServlet("/carWash")
public class CarWashControl extends HttpServlet {

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

            out.println("<html>");
            out.println("<body style='font-family: Arial; margin: 20px;'>");

            out.println("<h1 style='color:#333;'>Welcome " + name + "</h1>");

            out.println("<div style='margin-bottom: 20px;'>");
            out.println("<a href='" + userType + "'>Home</a> | ");
            out.println("<a href='washStaff' >Allocate Staffs</a> | ");
            out.println("<a href='logout' style='color:red;'>Logout</a>");
            out.println("</div>");

            
            out.println("<h1 style='color:#444;'>Add Car Wash Entry</h1>");

            out.println("<form method='post' action='carWashInsert' style='margin-bottom:30px;'>");

            boolean hasVehicle = false;

            out.println("<label style='font-weight:bold;'>Select Vehicle:</label><br>");
            pst = conn.prepareStatement("SELECT vehicle_id, vehicle_no FROM vehicle_details");
            rs = pst.executeQuery();

            if (rs.next()) {
                hasVehicle = true;
                out.println("<select name='vehicle_id' required style='padding:5px; margin:5px 0; width:200px;'>");
                out.println("<option disabled selected>-- Select Vehicle --</option>");
                do {
                    out.println("<option value='" + rs.getInt(1) + "'>" + rs.getString(2) + "</option>");
                } while (rs.next());
                out.println("</select><br><br>");
            } else {
                out.println("No vehicles found. <a href='vehicles'>Add Vehicle</a><br><br>");
            }

            
            out.println("<label style='font-weight:bold;'>Wash Date:</label><br>");
            out.println("<input type='date' name='wash_date' required style='padding:5px; margin:5px 0;'><br><br>");

            out.println("<label style='font-weight:bold;'>Status:</label><br>");
            out.println("<select name='status' required style='padding:5px; margin:5px 0; width:200px;'>");
            out.println("<option value='completed'>Completed</option>");
            out.println("<option value='pending'>Pending</option>");
            out.println("</select><br><br>");

            
            if (hasVehicle) {
                out.println("<input type='submit' value='Save Wash' style='padding:8px 15px; background:blue; color:white; border:none; cursor:pointer;'>");
            } else {
                out.println("<input type='submit' value='Save Wash' disabled style='padding:8px 15px; background:gray; color:white; border:none;'>");
            }

            out.println("</form>");

            out.println("<hr>");

           
            out.println("<h2 style='color:#333;'>Existing Car Wash Records</h2>");

            

            out.println("<table border='1' style='border-collapse: collapse; width:100%;'>");
            out.println("<tr style='background:#f2f2f2; text-align:left;'>");
            out.println("<th style='padding:8px;'>Sl No</th>");
            out.println("<th style='padding:8px;'>Wash ID</th>");
            out.println("<th style='padding:8px;'>Vehicle No</th>");
            out.println("<th style='padding:8px;'>Wash Date</th>");
            out.println("<th style='padding:8px;'>Status</th>");
            out.println("<th style='padding:8px;'>Action</th>");
            out.println("</tr>");

            int i = 1;
            pst = conn.prepareStatement("SELECT car_wash_id, vehicle_id, wash_date, status FROM car_wash_details");
            rs = pst.executeQuery();
            while (rs.next()) {

                PreparedStatement pstDemo = conn.prepareStatement(
                        "SELECT vehicle_no FROM vehicle_details WHERE vehicle_id=?");
                pstDemo.setInt(1, rs.getInt(2));
                ResultSet rsDemo = pstDemo.executeQuery();
                rsDemo.next();

                out.println("<tr>");
                out.println("<td style='padding:8px;'>" + i + "</td>");
                out.println("<td style='padding:8px;'>" + rs.getInt(1) + "</td>");
                out.println("<td style='padding:8px;'>" + rsDemo.getString(1) + "</td>");
                out.println("<td style='padding:8px;'>" + rs.getDate(3) + "</td>");
                out.println("<td style='padding:8px;'>" + rs.getString(4) + "</td>");
                out.println("<td style='padding:8px;'><a href='carWashEdit?wid=" + rs.getInt(1)
                        + "'>Edit</a> | <a href='carWashDelete?wid=" + rs.getInt(1)+ "' style='color:red;'>Delete</a></td>");
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
