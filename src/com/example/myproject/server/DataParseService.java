package com.example.myproject.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataParseService extends HttpServlet {
	//This server's job is to parse the data as a query to the database to insert it.

	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//String url = "oracle11.compusult.net:devel11";
			String url = "jdbc:oracle11.compusult.net:devel11";
			Properties props = new Properties();
			props.setProperty("user", "dale_wesonline");
			props.setProperty("password", "wes");
			props.setProperty("ssl", "true");
			try {
				Connection conn = DriverManager.getConnection(url, props);
				PreparedStatement pstatement = conn.prepareStatement("");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
}
