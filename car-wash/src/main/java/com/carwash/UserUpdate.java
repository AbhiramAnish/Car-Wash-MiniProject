package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/userUpdate")
public class UserUpdate extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String URL = "jdbc:mysql://localhost:3306/car_wash";
    final String USER = "root";
    final String PASSWORD = "password";

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            int staffId = Integer.parseInt(request.getParameter("staffId"));
            int uid = Integer.parseInt(request.getParameter("uid"));
            String fullName = request.getParameter("fullname");
            String dobStr = request.getParameter("dob");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String aadhar = request.getParameter("aadhar");

            Date sqlDate = Date.valueOf(LocalDate.parse(dobStr)); 
          

            pst = conn.prepareStatement("UPDATE staff_details SET full_name=?, address=?, phone_no=?, dob=?, aadhar=? WHERE staff_id=?");
            pst.setString(1, fullName);
            pst.setString(2, address);
            pst.setString(3, phone);
            pst.setDate(4, sqlDate);
            pst.setString(5, aadhar);
            pst.setInt(6, staffId);

             pst.executeUpdate();

           
                response.sendRedirect("userDetails?uid="+uid);
            

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
