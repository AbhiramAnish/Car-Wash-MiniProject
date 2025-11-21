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

@WebServlet("/designationEdit")
public class DesignationEdit extends HttpServlet {

    private static final long serialVersionUID = 1L;

    final String DRIVER = "com.mysql.cj.jdbc.Driver";
    final String URL = "jdbc:mysql://localhost:3306/car_wash";
    final String USER = "root";
    final String PASSWORD = "password";

    Connection conn = null;
    PreparedStatement pst = null;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("did"));

        try {
            PrintWriter out = response.getWriter();
            response.setContentType("text/html");
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            pst = conn.prepareStatement("SELECT designation_name FROM designation_details WHERE designation_id=?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            out.println("<html>");
            out.println("<body style='font-family: Arial; padding: 20px; background: #f7f7f7;'>");

            out.println("<h2 style='text-align:center; color:#333;'>Edit Designation</h2>");
            out.println("<div style='margin-bottom:20px; text-align:center;'>");

			out.println("<a href='designation' style='margin-right:10px;'>Back</a>");
			out.println("<a href='logout' style='color:red;'>Logout</a>");
			out.println("</div>");

            if (rs.next()) {

                out.println("<div style='max-width:400px; margin:auto; padding:20px; border:1px solid #ccc; "
                        + "border-radius:5px; background:white;'>");

                out.println("<form action='designationUpdate' method='post'>");

                out.println("<input type='hidden' name='did' value='" + id + "'/>");

                out.println("<label style='display:block; margin-bottom:5px;'>Designation Name</label>");
                out.println("<input type='text' name='designation_name' value='" + rs.getString(1)
                        + "' required style='width:100%; padding:8px; margin-bottom:15px; "
                        + "border:1px solid #aaa; border-radius:4px;'/>");

                out.println("<input type='submit' value='Update' "
                        + "style='padding:10px 20px; background:#4CAF50; color:white; border:none; "
                        + "cursor:pointer; border-radius:4px;'/>");

                out.println("</form>");
                out.println("</div>");
            }

            out.println("</body></html>");

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
