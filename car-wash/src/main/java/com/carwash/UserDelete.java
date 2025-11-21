package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/userDelete")
public class UserDelete extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String URL = "jdbc:mysql://localhost:3306/car_wash";
    final String USER = "root";
    final String PASSWORD = "password";

    Connection conn = null;
    PreparedStatement pst = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            int uid = Integer.parseInt(request.getParameter("uid"));

            pst = conn.prepareStatement("DELETE FROM user_details WHERE user_id = ?");
            pst.setInt(1, uid);
            pst.executeUpdate();

            
            response.sendRedirect("addUser");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } 
    }
}
