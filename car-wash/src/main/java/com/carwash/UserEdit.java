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

@WebServlet("/userEdit")
public class UserEdit extends HttpServlet {

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

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            int staffId = Integer.parseInt(request.getParameter("staffId"));

            pst = conn.prepareStatement("SELECT * FROM staff_details WHERE staff_id=?");
            pst.setInt(1, staffId);
            rs = pst.executeQuery();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            if(rs.next()) {
                out.println("<html><body style='font-family:Arial,sans-serif; padding:20px;'>");
                out.println("<h2 style='color:#333;'>Edit Staff Details</h2>");
                
				out.println("<a href='addUser' style='margin-right:15px;'>Back</a>");
				out.println("<a href='logout' style='color:red'>Logout</a>");
                out.println("<form action='userUpdate' method='post'>");
                out.println("<input type='hidden' name='staffId' value='" + staffId + "'>");
                out.println("<input type='hidden' name='uid' value='" + rs.getInt(2) + "'>");

                out.println("<p>Full Name:<br><input type='text' name='fullname' value='" + rs.getString(3) + "' required style='width:100%; padding:5px; margin-bottom:10px;'></p>");
                out.println("<p>Date of Birth (yyyy-MM-dd):<br><input type='date' name='dob' value='" + rs.getDate(6) + "' required style='width:100%; padding:5px; margin-bottom:10px;'></p>");
                out.println("<p>Address:<br><input type='text' name='address' value='" + rs.getString(4) + "' required style='width:100%; padding:5px; margin-bottom:10px;'></p>");
                out.println("<p>Phone No:<br><input type='text' name='phone' value='" + rs.getString(5) + "' required style='width:100%; padding:5px; margin-bottom:10px;'></p>");
                out.println("<p>Aadhar Number:<br><input type='text' name='aadhar' value='" + rs.getString(7) + "' required style='width:100%; padding:5px; margin-bottom:10px;'></p>");

                out.println("<input type='submit' value='Update Details' style='padding:8px 15px; background-color:#007BFF; color:white; border:none; border-radius:4px; cursor:pointer;'>");
                out.println("</form>");
                out.println("</body></html>");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
