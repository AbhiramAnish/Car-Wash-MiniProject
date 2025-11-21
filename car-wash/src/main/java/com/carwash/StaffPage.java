package com.carwash;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/staff")
public class StaffPage extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req,res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		try {
			HttpSession session=req.getSession(false);
			String name=(String) session.getAttribute("user");
			
			res.setContentType("text/html");
			PrintWriter out=res.getWriter();
			
			out.println("<html>");
			out.println("<body style='font-family: Arial; margin: 20px;'>");

			out.println("<h1 style='color: #333;'>Welcome " + name + "</h1><br><br>");

			out.println("<div style='margin-bottom: 20px;'>");
			
			out.println("<a href='flat' style='margin-right: 10px; text-decoration:none; color:blue;'>Flats</a>");
			out.println("<a href='customer' style='margin-right: 10px; text-decoration:none; color:blue;'>Customers</a>");
			out.println("<a href='vehicles' style='margin-right: 10px; text-decoration:none; color:blue;'>Vehicles</a>");
			out.println("<a href='carWash' style='margin-right: 10px; text-decoration:none; color:blue;'>Works</a>");
			out.println("<a href='payments' style='margin-right: 10px; text-decoration:none; color:blue;'>Payments</a>");
			out.println("<a href='logout' style='margin-right: 10px; text-decoration:none; color:red;'>Logout</a>");
			out.println("</div>");

			out.println("</body></html>");



		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
