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

@WebServlet("/staffSalaryUpdate")
public class StaffSalaryUpdate extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String URL = "jdbc:mysql://localhost:3306/car_wash";
    final String USER = "root";
    final String PASSWORD = "password";

    Connection conn = null;
    PreparedStatement pst = null;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Parameters
            int salaryId = Integer.parseInt(request.getParameter("salary_id"));
            int staffId = Integer.parseInt(request.getParameter("staff_id")); 
            double basicSalary = Double.parseDouble(request.getParameter("basic_salary"));

            
            pst = conn.prepareStatement("UPDATE staff_salary_details SET staff_id=?, basic_salary=? WHERE salary_id=?");
            pst.setInt(1, staffId);
            pst.setDouble(2, basicSalary);
            pst.setInt(3, salaryId);

            pst.executeUpdate();

            
            response.sendRedirect("staffSalary");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
