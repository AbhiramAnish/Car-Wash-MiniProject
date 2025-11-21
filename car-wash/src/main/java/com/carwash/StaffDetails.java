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

@WebServlet("/staffDetails")
public class StaffDetails extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String URL = "jdbc:mysql://localhost:3306/car_wash";
    final String USER = "root";
    final String PASSWORD = "password";
    
    PreparedStatement pst=null;
    ResultSet rs=null;
    Connection conn=null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        int uid = Integer.parseInt(request.getParameter("uid")); 
        
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            pst = conn.prepareStatement("Select user_name from user_details where user_id=?");
            pst.setInt(1, uid);
            rs = pst.executeQuery();
            rs.next();
            
            out.println("<html>");
            out.println("<body style='font-family: Arial, sans-serif; background-color:#f9f9f9; padding:20px;'>");
            out.println("<h2 style='color:#2c3e50;'>Add Details for " + rs.getString(1) + "</h2>");
            out.println("<form method='post' action='staffDetailsInserting' style='margin-top:20px;'>");
            out.println("<input type='hidden' name='uid' value='" + uid + "'>");

            out.println("Full Name:<br>");
            out.println("<input type='text' name='full_name' required style='padding:5px; margin-top:5px; margin-bottom:10px; width:300px;'><br><br>");

            out.println("Date of Birth:<br>");
            out.println("<input type='date' name='dob' required style='padding:5px; margin-top:5px; margin-bottom:10px;'><br><br>");

            out.println("Address:<br>");
            out.println("<input type='text' name='address' required style='padding:5px; margin-top:5px; margin-bottom:10px; width:400px;'><br><br>");

            out.println("Phone Number:<br>");
            out.println("<input type='text' name='phone_no' required style='padding:5px; margin-top:5px; margin-bottom:10px; width:200px;'><br><br>");

            out.println("Aadhar Number:<br>");
            out.println("<input type='text' name='aadhar_no' required style='padding:5px; margin-top:5px; margin-bottom:10px; width:200px;'><br><br>");

            out.println("<input type='submit' value='Save' style='padding:5px 10px; background-color:#27ae60; color:white; border:none; border-radius:5px;'>");

            out.println("</form>");
            out.println("</body></html>");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
