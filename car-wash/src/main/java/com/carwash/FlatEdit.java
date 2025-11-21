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

@WebServlet("/flatEdit")
public class FlatEdit extends HttpServlet {

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

            int fid = Integer.parseInt(request.getParameter("fid"));

            pst = conn.prepareStatement("select flat_name,flat_address from flat_details where flat_id=?");
            pst.setInt(1, fid);
            rs = pst.executeQuery();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<body style='font-family: Arial; background:#f4f4f4; padding:20px;'>");

            out.println("<h1 style='color:#333;'>Update the Flat Details</h1><br>");
            out.println("<div style='margin-bottom:20px;'>");
            
            out.println("<a href='flat' style='margin-right:10px;'>Back</a>");
      
            out.println("<a href='logout' style='color:red;'>Logout</a>");
            out.println("</div>");

            while (rs.next()) {

                out.println("<div style='background:white; padding:20px; border:1px solid #ccc; "
                        + "border-radius:5px; width:350px;'>");

                out.println("<h2 style='color:#444;'>Flat Details</h2>");

                out.println("<form name='flat' method='post' action='flatUpdate'>");

                out.println("<input type='hidden' name='flat_id' value='" + fid + "'>");

                out.println("<label style='font-weight:bold;'>Flat Name:</label><br>");
                out.println("<input type='text' name='flat_name' value='" + rs.getString(1)
                        + "' style='width:100%; padding:8px; border:1px solid #aaa; "
                        + "border-radius:4px; margin-bottom:15px;'><br>");

                out.println("<label style='font-weight:bold;'>Flat Address:</label><br>");
                out.println("<input type='text' name='flat_address' value='" + rs.getString(2)
                        + "' style='width:100%; padding:8px; border:1px solid #aaa; "
                        + "border-radius:4px; margin-bottom:15px;'><br>");

                out.println("<input type='submit' value='Update' "
                        + "style='padding:10px 20px; background:#008CBA; color:white; border:none; "
                        + "cursor:pointer; border-radius:4px;'>");

                out.println("</form>");
                out.println("</div>");
            }

            out.println("</body>");
            out.println("</html>");

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
