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

@WebServlet("/washStaff")
public class WashStaffControl extends HttpServlet {

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
            out.println("<a href='carWash'>Car Wash</a> | ");
            out.println("<a href='logout' style='color:red;'>Logout</a>");

            out.println("<h1 style='color:#333;'>Allocate Staff to Car Wash</h1>");
            out.println("<form method='post' action='washStaffInsert'>");

            boolean hasWash = false;
            boolean hasStaff = false;

            
            out.println("Select Car Wash:<br>");
            pst = conn.prepareStatement("SELECT car_wash_id FROM car_wash_details");
            rs = pst.executeQuery();

            if (rs.next()) {
                hasWash = true;
                out.println("<select name='car_wash_id' required style='width:100%; padding:5px; margin-bottom:10px;'>");
                out.println("<option disabled selected>-- Select Car Wash --</option>");
                do {
                    out.println("<option value='" + rs.getInt(1) + "'>Wash ID: " + rs.getInt(1) + "</option>");
                } while (rs.next());
                out.println("</select><br><br>");
            } else {
                out.println("No car wash records. <a href='carWash'>Add Car Wash</a><br><br>");
            }

            
            out.println("Select Staff:<br>");
            pst = conn.prepareStatement("SELECT staff_id, full_name FROM staff_details");
            rs = pst.executeQuery();

            if (rs.next()) {
                hasStaff = true;
                out.println("<select name='staff_id' required style='width:100%; padding:5px; margin-bottom:10px;'>");
                out.println("<option disabled selected>-- Select Staff --</option>");
                do {
                    out.println("<option value='" + rs.getInt(1) + "'>" + rs.getString(2) + "</option>");
                } while (rs.next());
                out.println("</select><br><br>");
            } else {
                out.println("No staff found. <a href='staff'>Add Staff</a><br><br>");
            }

            if (hasWash && hasStaff) {
                out.println("<input type='submit' value='Allocate Staff' style='padding:8px 15px; background-color:#007BFF; color:white; border:none; border-radius:4px; cursor:pointer;'>");
            } else {
                out.println("<input type='submit' value='Allocate Staff' disabled style='padding:8px 15px; background-color:#ccc; color:white; border:none; border-radius:4px;'>");
            }

            out.println("</form><br><hr>");

            out.println("<h2 style='color:#333;'>Existing Staff Allocations</h2>");

            pst = conn.prepareStatement( "SELECT car_wash_staff_id, car_wash_id, staff_id FROM car_wash_staff");
            rs = pst.executeQuery();

            out.println("<table border='1' style='border-collapse:collapse; width:100%;'>");
            out.println("<tr style='background-color:#f2f2f2;'>");
            out.println("<th style='padding:8px;'>Sl No</th>");
            out.println("<th style='padding:8px;'>Car Wash ID</th>");
            out.println("<th style='padding:8px;'>Staff Name</th>");
            out.println("<th style='padding:8px;'>Action</th>");
            out.println("</tr>");

            int i = 1;

            while (rs.next()) {

                int wsId = rs.getInt(1);
                int washId = rs.getInt(2);
                int staffId = rs.getInt(3);

                PreparedStatement pstS = conn.prepareStatement(
                        "SELECT full_name FROM staff_details WHERE staff_id=?");
                pstS.setInt(1, staffId);
                ResultSet rsS = pstS.executeQuery();

                String staffName = "";
                if (rsS.next()) {
                    staffName = rsS.getString(1);
                }

                out.println("<tr>");
                out.println("<td style='padding:8px;'>" + i + "</td>");
                out.println("<td style='padding:8px;'>" + washId + "</td>");
                out.println("<td style='padding:8px;'>" + staffName + "</td>");
                out.println("<td style='padding:8px;'><a href='washStaffEdit?wsid=" + wsId + "'>Edit</a> | "
                        + "<a href='washStaffDelete?wsid=" + wsId + "' style='color:red;'>Delete</a></td>");
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
