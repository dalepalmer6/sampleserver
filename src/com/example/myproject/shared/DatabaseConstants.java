package com.example.myproject.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

public class DatabaseConstants {
	//All data types that can be input for the data headers
	private static String[] dataTypes = {"String","Date","Number"};
	private static String[] databases = {"Postgres", "Oracle", "SQL"};
	
	public static String getDatabaseJDBC(String databaseType, String host, String database) throws ClassNotFoundException {
		String JDBC_URL;
		System.out.println(databaseType + host + database);
		switch (databaseType) {
			case "Oracle": 	JDBC_URL = "jdbc:oracle:thin:@" + host + ":1521:" + database;
							Class.forName("oracle.jdbc.driver.OracleDriver");
							break;
			case "Postgres":JDBC_URL = "jdbc:postgresql_postGIS://" + host + ":5432/" + database;
							Class.forName("org.postgis.DriverWrapper");
							break;
			case "SQL": 	JDBC_URL = "jdbc:sqlserver://" + host + ":1433;databaseName=" + database;
							Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
							break;
			default:		JDBC_URL = "";
							break;
		}
		return JDBC_URL;
	}
	
	public static Connection makeConnection(String URL, String user, String pass) throws SQLException {
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", pass);
		System.out.println("Trying connection at " + URL);
		Connection conn = DriverManager.getConnection(URL, props);
		return conn;
	}
	
	public static String inputDropDownMenuDatabases(String selectedDatabase) {
		//VerticalPanel databaseInfo = new VerticalPanel();
		String HTML = "<select id=\'dbType\' name=\'databaseType\'>\n";
		for (int i = 0; i < DatabaseConstants.getDatabases().length; i++) {
			HTML += "   <option ";
			if (selectedDatabase.equals(DatabaseConstants.getDatabases()[i])) {
				HTML += "selected";
			}
			HTML += "value=\'" + DatabaseConstants.getDatabases()[i] + "\'>" + DatabaseConstants.getDatabases()[i] + "</option>\n";
		}
		return HTML;
	}
	
	public static String getDatabaseFields(String fname) {
		String HTML = "<input name=\'fileName\' type=\'hidden\' value=\'" + fname + "\'>"
					+ "<table>"
					+ "<tr><td><label>Select a Database to Connect to: </td><td>" + inputDropDownMenuDatabases("") + "</td></tr>"
					+ "<tr><td><label>Name of Table: </td><td><input id=\'pathToTable\' name=\'pathToTable\' value=\'" + FilenameUtils.removeExtension(fname) + "\'></td></tr><br>"
					+ "<tr><td><label>Database Host: </label></td><td><input id=\'db_host\' name=\'db_host\' value=\'pg1-devel.compusult.net\'" + "\'></td></tr><br>"
					+ "<tr><td><label>Username: </label></td><td><input id=\'user\' name=\'user\' value=\'dale_wesweb\'" + "\'></td></tr>"
					+ "<tr><td><label>Password: </label></td><td><input id=\'pwd\' type=\'password\' name=\'pwd\' value=\'wes\'" + "\'></td></tr>"
					+ "<tr><td><label>Database/SID: </label></td><td><input id=\'dbSID\' name=\'dbSID\' value=\'dale\'" + "\'></td></tr>"
					+ "</table>";
		return HTML;
	}
	
	public static String[] getDatabases() {
		return databases;
	}
	
	public static String[] getDataTypes() {
		return dataTypes;
	}
	
}
