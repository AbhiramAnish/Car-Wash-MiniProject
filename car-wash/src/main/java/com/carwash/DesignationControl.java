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
import javax.servlet.http.HttpSession;

@WebServlet("/designation")
public class DesignationControl extends HttpServlet {

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

        int i = 1;

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            pst = conn.prepareStatement("SELECT * FROM designation_details");
            rs = pst.executeQuery();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            HttpSession session = request.getSession(false);
            String name = (String) session.getAttribute("user");
            String userType = (String) session.getAttribute("userType");

            out.println("<html>");
            out.println("<body style='font-family: Arial; margin: 20px;'>");

            out.println("<h1 style='color:#333;'>Welcome " + name + "</h1>");
            out.println("<a href='" + userType + "' style='margin-right:10px; '>Home</a>");
            out.println("<a href='logout' style='color:red;'>Logout</a>");
            out.println("<br><br>");

            
            out.println("<h2 style='color:#444;'>Add Designation</h2>");

            out.println("<form method='post' action='designationInsert' "
                    + "style='padding:20px; width:350px;'>");

            out.println("<label>Designation Name:</label><br>");
            out.println("<input type='text' name='designation_name' required "
                    + "style='padding:6px; width:100%; margin-top:5px; margin-bottom:15px;'><br>");

            out.println("<input type='submit' value='Save' "
                    + "style='padding:8px 15px; background:#4CAF50; color:white; border:none; border-radius:5px; cursor:pointer;'>");

            out.println("</form>");

            out.println("<br><br>");

           
            out.println("<h2 style='color:#444;'>Designation List</h2>");

            out.println("<table border='1' cellspacing='0' cellpadding='8' "
                    + "style='border-collapse:collapse; width:60%;'>");

            out.println("<tr style='background:#f2f2f2;'>");
            out.println("<th>Sl No</th>");
            out.println("<th>Designation Name</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + i + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");

                out.println("<td>"
                        + "<a href='designationEdit?did=" + rs.getInt(1) + "' style='margin-right:10px;'>Edit</a>"
                        + "<a href='designationDelete?did=" + rs.getInt(1) + "' style='color:red;'>Delete</a>"
                        + "</td>");

                out.println("</tr>");
                i++;
            }

            out.println("</table>");

            out.println("</body></html>");

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
