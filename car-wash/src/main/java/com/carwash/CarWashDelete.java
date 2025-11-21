package com.carwash;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/carWashDelete")
public class CarWashDelete extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String URL = "jdbc:mysql://localhost:3306/car_wash";
    final String USER = "root";
    final String PASSWORD = "password";

    Connection conn = null;
    PreparedStatement pst = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int wid = Integer.parseInt(request.getParameter("wid"));  

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            pst = conn.prepareStatement("DELETE FROM car_wash_details WHERE car_wash_id=?");
            pst.setInt(1, wid);

            pst.executeUpdate();

            response.sendRedirect("carWash");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
