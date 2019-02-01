package com.example.myproject.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.myproject.shared.DatabaseConstants;

public class RetrieveTableNameService extends HttpServlet {
	//accesses the database and queries the data to a usable format
	private Connection conn;
	private List<String> tableNames = new ArrayList<String>();
	private String host,user,pwd,databaseType,databaseSID;
	
	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
	public String createDropDownTableNamesHTML() {
		String HTML = "<select name=\'pathToTable" + "\'>";
		for (int i = 0; i < tableNames.size(); i++) {
			HTML += "<option value=\'" + tableNames.get(i) + "\'>" + tableNames.get(i) + "</option>";	
		}
		return HTML;
	}
	
	public String getTable() {
		String HTML = "<p><b>Enter the database information that you wish to view:</b></p><br><table>"
				+ "<tr><td><label>Select a Database to Connect to: </td><td>" + DatabaseConstants.inputDropDownMenuDatabases(databaseType) + "</td></tr>"
				+ "<tr><td><label>Name of Table: </td><td>" + createDropDownTableNamesHTML() + "</td></tr><br>"
				+ "<tr><td><label>Database Host: </label></td><td><input id=\'db_host\' name=\'db_host\' value=\'" + host + "\'" + "\'></td></tr><br>"
				+ "<tr><td><label>Username: </label></td><td><input id=\'user\' name=\'user\' value=\'" + user + "\'" + "\'></td></tr>"
				+ "<tr><td><label>Password: </label></td><td><input id=\'pwd\' type=\'password\' name=\'pwd\' value=\'" + pwd + "\'" + "\'></td></tr>"
				+ "<tr><td><label>Database/SID: </label></td><td><input id=\'dbSID\' name=\'dbSID\' value=\'" + databaseSID + "\'" + "\'></td></tr>"
				+ "</table>";
		return HTML;
	}
	
	public String createDataElement(String field) {
		return "<td>" + field + "</td>";
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
			//clear the tableNames
			tableNames = new ArrayList<String>();
			host = req.getParameter("db_host");
			user = req.getParameter("user");
			pwd = req.getParameter("pwd");
			databaseSID = req.getParameter("dbSID");
			databaseType = req.getParameter("databaseType");
			String SQL = "select table_name from information_schema.tables where table_schema = \'csv_to_db\'";
			
				//returns all the tablenames make a function?
				try {
					String JDBC_URL = DatabaseConstants.getDatabaseJDBC(databaseType,host,databaseSID);
					Connection conn = DatabaseConstants.makeConnection(JDBC_URL,user,pwd);
					PreparedStatement p = conn.prepareStatement(SQL);
					ResultSet r = p.executeQuery();
					while (r.next()) {
						tableNames.add(r.getString(1));
					}
					p.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			PrintWriter out = res.getWriter();
			out.println(getTable());
	}
}
