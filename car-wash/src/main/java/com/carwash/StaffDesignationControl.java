package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staffDesignation")
public class StaffDesignationControl extends HttpServlet {

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

            boolean hasStaff = false;
            boolean hasDesignation = false;

            HttpSession session = request.getSession(false);
            String name = (String) session.getAttribute("user");
            String userType = (String) session.getAttribute("userType");

            out.println("<html>");
            out.println("<body style='font-family:Arial; background-color:#f2f2f2; padding:20px;'>");

            out.println("<h1 style='color:#2c3e50;'>Welcome " + name + "</h1>");

            out.println("<div style='margin-bottom:20px;'>");
            out.println("<a href='" + userType + "' style='margin-right:10px;'>Home</a>");
            out.println("<a href='logout' style='color:red;'>Logout</a>");
            out.println("</div>");

            out.println("<h2 >Assign Designation to Staff</h2>");

            out.println("<form method='post' action='staffDesignationInsert' style='margin-bottom:20px;'>");

            out.println("Select Staff:<br>");
            pst = conn.prepareStatement("SELECT staff_id, full_name FROM staff_details");
            rs = pst.executeQuery();
            if (rs.next()) {
                hasStaff = true;
                out.println("<select name='staff_id' required style='padding:5px; margin-top:5px; margin-bottom:10px;'>");
                out.println("<option value='' disabled selected>-- Select Staff --</option>");
                do {
                    out.println("<option value='" + rs.getInt(1) + "'>" + rs.getString(2) + "</option>");
                } while (rs.next());
                out.println("</select><br><br>");
            } else {
                out.println("<a href='addUser' style='color:#2980b9;'>Add Staff</a><br><br>");
            }

            out.println("Select Designation:<br>");
            pst = conn.prepareStatement("SELECT designation_id, designation_name FROM designation_details");
            rs = pst.executeQuery();
            if (rs.next()) {
                hasDesignation = true;
                out.println("<select name='designation_id' required style='padding:5px; margin-top:5px; margin-bottom:10px;'>");
                out.println("<option value='' disabled selected>-- Select Designation --</option>");
                do {
                    out.println("<option value='" + rs.getInt(1) + "'>" + rs.getString(2) + "</option>");
                } while (rs.next());
                out.println("</select><br><br>");
            } else {
                out.println("<a href='designation' style='color:#2980b9;'>Add Designation</a><br><br>");
            }

            out.println("Joining Date:<br>");
            out.println("<input type='date' name='joining_date' required style='padding:5px; margin-top:5px; margin-bottom:10px;'><br><br>");

            if (hasStaff && hasDesignation) {
                out.println("<input type='submit' value='Assign Designation' style='padding:6px 12px; background:#27ae60; border:none; color:white; border-radius:4px;'>");
            } else {
                out.println("<input type='submit' value='Assign Designation' disabled style='padding:6px 12px; background:#95a5a6; border:none; color:white; border-radius:4px;'>");
            }

            out.println("</form>");

            out.println("<h2 style='color:#34495e;'>Existing Staff Designations</h2>");

            pst = conn.prepareStatement("SELECT * FROM staff_designation_details");
            rs = pst.executeQuery();

            out.println("<table border='1' style='width:100%; border-collapse:collapse; background:white;'>");
            out.println("<tr>");
            out.println("<th style='padding:8px;'>Sl No</th>");
            out.println("<th style='padding:8px;'>Staff Name</th>");
            out.println("<th style='padding:8px;'>Designation</th>");
            out.println("<th style='padding:8px;'>Joining Date</th>");
            out.println("<th style='padding:8px;'>Action</th>");
            out.println("</tr>");

            int i = 1;
            while (rs.next()) {

                PreparedStatement pstStaff = conn.prepareStatement("SELECT full_name FROM staff_details WHERE staff_id=?");
                pstStaff.setInt(1, rs.getInt(2));
                ResultSet rsStaff = pstStaff.executeQuery();
                String staffName = "";
                if (rsStaff.next()) staffName = rsStaff.getString(1);

                PreparedStatement pstDesig = conn.prepareStatement("SELECT designation_name FROM designation_details WHERE designation_id=?");
                pstDesig.setInt(1, rs.getInt(3));
                ResultSet rsDesig = pstDesig.executeQuery();
                String designationName = "";
                if (rsDesig.next()) designationName = rsDesig.getString(1);

                out.println("<tr style='text-align:center;'>");
                out.println("<td style='padding:8px;'>" + i + "</td>");
                out.println("<td style='padding:8px;'>" + staffName + "</td>");
                out.println("<td style='padding:8px;'>" + designationName + "</td>");
                out.println("<td style='padding:8px;'>" + rs.getDate(4) + "</td>");
                out.println("<td style='padding:8px;'><a href='staffDesignationEdit?sdid=" + rs.getInt(1) + "' >Edit</a> | <a href='staffDesignationDelete?sdid=" + rs.getInt(1) + "' style='color:red;'>Delete</a></td>");
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
