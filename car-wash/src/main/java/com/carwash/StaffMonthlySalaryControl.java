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

@WebServlet("/staffMonthlySalary")
public class StaffMonthlySalaryControl extends HttpServlet {

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
            out.println("<body style='font-family: Arial; background:#f5f5f5; padding:20px;'>");

            out.println("<h1 style='color:#2c3e50;'>Welcome " + name + "</h1>");
            out.println("<a href='" + userType + "' style='margin-right:10px;'>Home</a>");
            out.println("<a href='logout' style='color:red;'>Logout</a><br><br>");

            out.println("<h2 style='color:#34495e;'>Assign Monthly Salary</h2>");
            out.println("<form method='post' action='staffMonthlySalaryInsert' style='margin-top:20px;'>");

            boolean hasStaff = false;

            out.println("Select Staff:<br>");
            pst = conn.prepareStatement("SELECT staff_id, full_name FROM staff_details");
            rs = pst.executeQuery();

            if (rs.next()) {
                hasStaff = true;
                out.println("<select name='staff_id' required style='padding:5px; width:300px; margin-top:5px;'>");
                out.println("<option disabled selected>-- Select Staff --</option>");
                do {
                    out.println("<option value='" + rs.getInt(1) + "'>" + rs.getString(2) + "</option>");
                } while (rs.next());
                out.println("</select><br><br>");
            } else {
                out.println("<a href='staff'>Add Staff</a><br><br>");
            }

            out.println("Salary Month:<br>");
            out.println("<input type='month' name='salary_month' required style='padding:5px; width:200px; margin-top:5px;'><br><br>");

            out.println("Salary Amount:<br>");
            out.println("<input type='number' name='salary' required style='padding:5px; width:200px; margin-top:5px;'><br><br>");

            if (hasStaff) {
                out.println("<input type='submit' value='Save Monthly Salary' style='padding:5px 10px; background:#27ae60; color:white; border:none; border-radius:5px;'>");
            } else {
                out.println("<input type='submit' value='Save Monthly Salary' disabled style='padding:5px 10px; background:#95a5a6; color:white;'>");
            }

            out.println("</form><br><hr>");

            out.println("<h2 style='color:#34495e;'>Existing Monthly Salaries</h2>");

            pst = conn.prepareStatement("SELECT * FROM staff_monthly_salary_details");
            rs = pst.executeQuery();

            out.println("<table border='1' style='border-collapse:collapse; width:80%; background:white;'>");
            out.println("<tr style='background:#ecf0f1;'>");
            out.println("<th style='padding:8px;'>Sl No</th>");
            out.println("<th style='padding:8px;'>Staff Name</th>");
            out.println("<th style='padding:8px;'>Month</th>");
            out.println("<th style='padding:8px;'>Salary</th>");
            out.println("<th style='padding:8px;'>Action</th>");
            out.println("</tr>");

            int i = 1;

            while (rs.next()) {
                int msId = rs.getInt(1);
                int staffId = rs.getInt(2);
                String month = rs.getString(3);
                double salary = rs.getDouble(4);

                PreparedStatement pstS = conn.prepareStatement("SELECT full_name FROM staff_details WHERE staff_id=?");
                pstS.setInt(1, staffId);
                ResultSet rsS = pstS.executeQuery();
                String staffName = "";
                if (rsS.next()) {
                    staffName = rsS.getString(1);
                }

                out.println("<tr style='text-align:center;'>");
                out.println("<td style='padding:8px;'>" + i + "</td>");
                out.println("<td style='padding:8px;'>" + staffName + "</td>");
                out.println("<td style='padding:8px;'>" + month + "</td>");
                out.println("<td style='padding:8px;'>" + salary + "</td>");
                out.println("<td style='padding:8px;'><a href='staffMonthlySalaryEdit?msid=" + msId + "'>Edit</a></td>");
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
