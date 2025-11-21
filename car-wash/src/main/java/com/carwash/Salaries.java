package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/salaries")
public class Salaries extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);
            String name = (String) session.getAttribute("user");

            out.println("<html>");
            out.println("<body style='font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;'>");

            out.println("<h1 style='color: #2c3e50;'>Welcome " + name + "</h1>");
            out.println("<div style='margin-bottom: 20px;'>");
            out.println("<a href='admin' style='margin-right: 10px; '>Home</a>");
            out.println("<a href='logout' style='color: red;'>Logout</a>");
            out.println("</div>");

            out.println("<h1 style='color: #2c3e50;'>Salaries of Staffs</h1>");
            out.println("<div style='margin-bottom: 10px;'><a href='staffSalary' style='text-decoration: none; color: #2980b9;'>Basic Salary</a></div>");
            out.println("<div><a href='staffMonthlySalary' style='text-decoration: none; color: #2980b9;'>Monthly Salary</a></div>");

            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
