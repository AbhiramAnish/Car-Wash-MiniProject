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

@WebServlet("/washStaffEdit")
public class WashStaffEdit extends HttpServlet {

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

        int wsid = Integer.parseInt(request.getParameter("wsid")); 

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {

            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            pst = conn.prepareStatement(
                    "SELECT car_wash_id, staff_id FROM car_wash_staff WHERE car_wash_staff_id=?"
            );
            pst.setInt(1, wsid);
            rs = pst.executeQuery();

            if (rs.next()) {

                int selectedCarWashId = rs.getInt(1);
                int selectedStaffId = rs.getInt(2);

                out.println("<html><body style='font-family:Arial,sans-serif; padding:20px;'>");
                out.println("<h2 style='color:#333;'>Edit Wash Staff Allocation</h2>");
                
                out.println("<a href='washStaff'>Back </a> | ");
                out.println("<a href='logout' style='color:red;'>Logout</a> <br><br>");
                out.println("<form method='post' action='washStaffUpdate'>");

                out.println("<input type='hidden' name='wsid' value='" + wsid + "'>");

                out.println("Select Car Wash ID:<br>");
                PreparedStatement pstDropDownList = conn.prepareStatement( "SELECT car_wash_id FROM car_wash_details" );
                ResultSet rsCarWash = pstDropDownList.executeQuery();
                
                out.println("<select name='car_wash_id' required style='width:100%; padding:5px; margin-bottom:10px;'>");
                while (rsCarWash.next()) {
                    int cwId = rsCarWash.getInt(1);

                    if (cwId == selectedCarWashId) {
                        out.println("<option value='" + cwId + "' selected>Wash ID: " +
                                cwId +   "</option>");
                    } else {
                        out.println("<option value='" + cwId + "'>Wash ID: " + cwId +  "</option>");
                    }
                }
                out.println("</select><br><br>");

                out.println("Select Staff:<br>");
                pstDropDownList = conn.prepareStatement("SELECT staff_id, full_name FROM staff_details");
                rsCarWash = pstDropDownList.executeQuery();

                out.println("<select name='staff_id' required style='width:100%; padding:5px; margin-bottom:10px;'>");
                while (rsCarWash.next()) {
                    int staffId = rsCarWash.getInt(1);

                    if (staffId == selectedStaffId) {
                        out.println("<option value='" + staffId + "' selected>" +
                        		rsCarWash.getString(2) + "</option>");
                    } else {
                        out.println("<option value='" + staffId + "'>" +
                        		rsCarWash.getString(2) + "</option>");
                    }
                }
                out.println("</select><br><br>");

                out.println("<input type='submit' value='Update' style='padding:8px 15px; background-color:#007BFF; color:white; border:none; border-radius:4px; cursor:pointer;'>");
                out.println("</form>");
                out.println("</body></html>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
