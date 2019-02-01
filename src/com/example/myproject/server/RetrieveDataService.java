package com.example.myproject.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.myproject.shared.DatabaseConstants;

public class RetrieveDataService extends HttpServlet{

	
	public String createDataElement(String field) {
		return "<td>" + field + "</td>";
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
			//parse this request using async service
			AsyncContext asyncContext = req.startAsync(req,res);
			PrintWriter out = res.getWriter();
			out.println("Running");
			
			asyncContext.complete();
//			String host = req.getParameter("db_host");
//			String user = req.getParameter("user");
//			String pwd = req.getParameter("pwd");
//			String databaseSID = req.getParameter("dbSID");
//			String tableName = req.getParameter("pathToTable");
//			String pathToTable = "csv_to_db.\"" +tableName+ "\"";
//			String databaseType = req.getParameter("databaseType");
//			String SQL = "select table_name from information_schema.tables where table_schema = \'csv_to_db\'";
//			System.out.println(tableName);
//			
//			//actually receive all data from the table in question
//			SQL = "select * from " + pathToTable;
//			System.out.println(SQL);
//			String table = "<table border=\'1\'>";
//			try {
//				String JDBC_URL = DatabaseConstants.getDatabaseJDBC(databaseType,host,databaseSID);
//				Connection conn = DatabaseConstants.makeConnection(JDBC_URL,user,pwd);
//				PreparedStatement p = conn.prepareStatement(SQL);
//				ResultSet r = p.executeQuery();
//				ResultSetMetaData r_meta = r.getMetaData();
//				int colCount = r_meta.getColumnCount();
//				table += "<tr>";
//				for (int i = 0; i < colCount; i++) {
//					table += "<td><b>" + r_meta.getColumnName(i+1) + "</b></td>";
//				}
//				table += "</tr>";
//				while (r.next()) {
//					table += "<tr>";
//					for (int i = 0; i < colCount; i++) {
//						switch(r_meta.getColumnType(i+1)) {
//						}
//						table += createDataElement(r.getString(i+1)); //create a table out of these values and return as the response!
//					}
//					table += "</tr>";
//				}
//				table += "</table>";
//				p.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			PrintWriter out = res.getWriter();
//			out.println(table);
	}
	
	
}
