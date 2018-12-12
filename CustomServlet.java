package com.mainServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class CustomServlet extends HttpServlet {
	static Logger log = Logger.getLogger(CustomServlet.class.getName());

	public void init() {
		System.out.println("i am in init of CustomServlet");
		log.debug("i am in CustomServlet init method");
		try {
			super.init();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Context ctx = null;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/MyLocalDB");

			con =  ds.getConnection();
			stmt = con.createStatement();

			rs = stmt.executeQuery("select user_id,user_password from users_v3 where user_id= upper('e5570443')");

			PrintWriter out = response.getWriter();
			response.setContentType("text/html");
			out.print("<html><body><h2>Employee Details</h2>");
			out.print("<table border=\"1\" cellspacing=10 cellpadding=5>");
			out.print("<th>Employee ID</th>");
			out.print("<th>Employee Name</th>");

			while (rs.next()) {
				out.print("<tr>");
				out.print("<td>" + rs.getString("user_id") + "</td>");
				out.print("<td>" + rs.getString("user_password") + "</td>");
				out.print("</tr>");
			}
			out.print("</table></body><br/>");

			// lets print some DB information
			out.print("<h3>Database Details</h3>");
			out.print("Database Product: "
					+ con.getMetaData().getDatabaseProductName() + "<br/>");
			out.print("Database Driver: " + con.getMetaData().getDriverName());
			out.print("</html>");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
				ctx.close();
			} catch (SQLException e) {
				System.out.println("Exception in closing DB resources");
			} catch (NamingException e) {
				System.out.println("Exception in closing Context");
			}

		}
	}

}
