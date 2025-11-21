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

@WebServlet("/addUser")
public class ManageUser extends HttpServlet {

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

            pst = conn.prepareStatement("select * from user_details");
            rs = pst.executeQuery();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            HttpSession session = request.getSession(false);
            String userType = (String) session.getAttribute("userType");

            out.println("<html>");
            out.println("<body style='font-family: Arial; background:#f4f4f4; padding:20px;'>");

            out.println("<h1 style='color:#333;'>Add User</h1>");
            out.println("<a href='" + userType + "' style='margin-right:10px;'>Home</a>");
            out.println("<a href='logout' style='color:red;' >Logout</a><br><br>");

            
            out.println("<div style='background:white; padding:20px; width:350px; "
                    + "border:1px solid #ccc; border-radius:5px;'>");

            out.println("<form name='userForm' method='post' action='insertUser'>");

            out.println("<label style='font-weight:bold;'>User Name:</label><br>");
            out.println("<input type='text' name='user_name' required "
                    + "style='width:100%; padding:8px; border:1px solid #aaa; margin-bottom:15px; border-radius:4px;'><br>");

            out.println("<label style='font-weight:bold;'>Set Password:</label><br>");
            out.println("<input type='password' id='password' name='password' required "
                    + "style='width:100%; padding:8px; border:1px solid #aaa; margin-bottom:15px; border-radius:4px;'><br>");

            out.println("<label style='font-weight:bold;'>Type Password Again:</label><br>");
            out.println("<input type='password' name='check_password' required "
                    + "style='width:100%; padding:8px; border:1px solid #aaa; border-radius:4px;'>");
            

            out.println("<label style='font-weight:bold;'>User Role:</label><br>");
            out.println("<select name='user_role' required "
                    + "style='width:100%; padding:8px; border-radius:4px; border:1px solid #aaa; margin-bottom:15px;'>");
            out.println("<option value='' disabled selected>-- Select Role --</option>");
            out.println("<option value='admin'>Admin</option>");
            out.println("<option value='staff'>Staff</option>");
            out.println("</select><br>");

            out.println("<input type='submit' value='Add User' "
                    + "style='padding:10px 20px; background:#28a745; color:white; border:none; "
                    + "cursor:pointer; border-radius:4px;'>");

            out.println("</form>");
            out.println("</div><br><br>");

            
            out.println("<table border='1' style='width:80%; border-collapse:collapse; background:white;'>");
            out.println("<tr style='background:#ddd;'>");
            out.println("<th style='padding:8px;'>Sl No</th>");
            out.println("<th style='padding:8px;'>User Name</th>");
            out.println("<th style='padding:8px;'>User Role</th>");
            out.println("<th style='padding:8px;'>User Status</th>");
            out.println("<th style='padding:8px;'>Action</th>");
            out.println("</tr>");

            while (rs.next()) {

                out.println("<tr>");
                out.println("<td style='padding:8px;'>" + i + "</td>");
                out.println("<td style='padding:8px;'>" + rs.getString(2) + "</td>");
                out.println("<td style='padding:8px;'>" + rs.getString(4) + "</td>");
                out.println("<td style='padding:8px;'>" + rs.getString(5) + "</td>");

                if (rs.getString(4).equals("staff")) {
                    out.println("<td style='padding:8px;'>"
                            + "<a href='userStatus?uid=" + rs.getInt(1) + "'>Change Status</a> | "
                            + "<a href='userDelete?uid=" + rs.getInt(1) + "' style='color:red'>Delete</a> | "
                            + "<a href='userDetails?uid=" + rs.getInt(1) + "'>More Details</a>"
                            + "</td>");
                } else {
                    out.println("<td style='padding:8px;'>No Action</td>");
                }

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
