package com.example.myproject.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import org.apache.commons.io.FilenameUtils;

import com.example.myproject.shared.CSV_Interpret;
import com.example.myproject.shared.DatabaseConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataParseService extends HttpServlet {
	//This server's job is to parse the data as a query to the database to insert it.
	private Connection conn;
	private Map<Integer,String> dataTypes;
	private String databaseType;
	
	public void setConnection(Connection c) {
		this.conn = c;
	}
	
	public Connection getConnection(){
		return this.conn;
	}
	
	public void setDataTypes(Map<Integer,String> d) {
		this.dataTypes = d;
	}
	
	public Map<Integer, String> getDataTypes() {
		return this.dataTypes;
	}
	
	public String getSQLDataType(String userInputType) {
		String sql;
		switch (userInputType) {
			case "String":	sql = "VARCHAR(255)";
							break;
			case "Date":	sql = "DATE";
							break;
			case "Number":	sql = "INT";
					 		break;
			default:		sql = "";
							System.out.println("No such SQL in current code for " + userInputType);
							break;
		}
		return sql;
	}
	
	public Map<Integer,String> receiveDataTypes(HttpServletRequest req) {
		Enumeration<String> en = req.getParameterNames();
		Map<Integer,String> dataTypes = new HashMap<Integer,String>(); 
		while (en.hasMoreElements()) {
			String param = (String) en.nextElement();
			if (param.startsWith("datatype")) {
				int i = Integer.parseInt(param.substring(8));
				dataTypes.put(i,req.getParameter(param));
			}
		}
		return dataTypes;
	}
	
	public File openSavedFile(String fname) {
		File openedFile = new File(fname);
		return openedFile;
	}
	
	public String createSchema(String name) {
		return "CREATE SCHEMA " + name;
	}
	
	public String generateDropQuery(String tableName) {
		//will append the IF EXISTS if the boolean ifExists is set.
		//also checks to utilize the right syntax for the db
		String SQL = "";
		switch(this.databaseType) {
		case "Postgres": 	SQL += "DROP TABLE IF EXISTS ";
							SQL += tableName +  ";";
							break;
		case "SQL":			SQL += "IF OBJECT_ID(\'" + tableName + "\', \'U\') IS NOT NULL\n"
								+  "  DROP TABLE " + tableName + ";";
							break;
		case "Oracle":		SQL += "DECLARE\nnot_exist EXCEPTION; "
							+ "PRAGMA EXCEPTION_INIT(not_exist, -942); "
							+ "BEGIN\n BEGIN EXECUTE IMMEDIATE \'DROP TABLE " + tableName + "\';"
							+ "EXCEPTION WHEN not_exist THEN NULL; END; END; ";
							break;
		default: 			SQL = "";
							break;
		}
		return SQL;
	}
	
	public String generateCreateQuery(String tableName, String[] headers) {
		//make headers and datatypes private to cut the need for passing
		Map<Integer,String> dataTypes = getDataTypes();
		String SQL_createTable = "CREATE TABLE " + tableName + " (";
		for (int i = 0; i < headers.length; i++) {
			SQL_createTable += "\"" + headers[i] + "\" " + getSQLDataType(dataTypes.get(i)) + " NOT NULL";
			if (i == headers.length-1) {
				SQL_createTable += ")";
			}
			else {
				SQL_createTable += ",";
			}
		}
		System.out.println(SQL_createTable);
		return SQL_createTable;
	}
	
	public boolean isEmptyLine(String l) {
		if (l.matches("[\\s*" + CSV_Interpret.getDelimiter() +"\\s*]*")) {
			return true;
		}
		else return false;
	}
	
	public void generateInsertQuery(String pathToTable, String fname) throws SQLException, FileNotFoundException {
		File f = openSavedFile(fname);
		Scanner fileIn = new Scanner(f);
		String line;
		String[] currentRow;
		fileIn.nextLine(); //skip the header line
		while (fileIn.hasNextLine()) {
			String SQL = "INSERT INTO " + pathToTable + " values ";
			line = fileIn.nextLine();
			if (isEmptyLine(line)) {
				//Skips over blank lines
				continue;
			}
			currentRow = CSV_Interpret.splitLineWithDelimiter(line);
			SQL += generateSQLData(currentRow);
			System.out.println(SQL);
				//populate the query
			insertIntoDatabase(SQL,currentRow);

		}
		fileIn.close();
	}
	
	public String generateSQLData(String[] currentLine){
			String SQL_insert = "(";
			for (int j = 0; j < currentLine.length; j++) {
				SQL_insert += "?";
				if (j == currentLine.length-1) {
					SQL_insert += ")";
				}
				else {
					SQL_insert += ",";
				}
			}
			return SQL_insert;
	}
	
	public String getPathToTable(String dbType, String fileNameNoExt) {
		String path;
		switch(dbType) {
			case "Oracle": 		path = "\"csv_to_db_" + fileNameNoExt + "\"";
								break;
			case "Postgres":	path = "csv_to_db.\"" + fileNameNoExt + "\"";
								break;
			case "SQL":			path = "csv_to_db_sql.\"" + fileNameNoExt + "\"";
								break;
			default:			path = "csv_to_db.\"" + fileNameNoExt + "\"";
								break;
		}
		return path;
	}
	
	public void insertIntoDatabase(String SQL, String[] currentRow) throws SQLException, FileNotFoundException {
		PreparedStatement p = getConnection().prepareStatement(SQL);
		Map<Integer,String> dataTypes = getDataTypes();
		for (int i = 0; i < currentRow.length; i++) {
				//use the appropriate setXXX() method for the String (datatype)
				String currType = dataTypes.get(i);
				//rewrite as Switch/Case
				if (currType.equals("String")) {
					p.setString(1+i, currentRow[i]);
				}
				else if (currType.equals("Number")) {
					//parse the String to an integer
					int x = Integer.parseInt(currentRow[i]);
					p.setInt(1+i, x);
				}
				else if (currType.equals("Date")) {
					//parse the String to a Date
					Date x = Date.valueOf(currentRow[i]);
					p.setDate(1+i, x);
				}
		}
		p.execute();
		p.close();
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
		//TODO SET UP ALL THE METHODS THAT THIS WILL USE i.e. cleanup
			PrintWriter output = res.getWriter();
			setDataTypes(receiveDataTypes(req));
			File f = new File(req.getParameter("fileName"));
			String databaseType = req.getParameter("databaseType");
			this.databaseType = databaseType;
			String host = req.getParameter("db_host");
			String user = req.getParameter("user");
			String pwd = req.getParameter("pwd");
			String databaseSID = req.getParameter("dbSID");
			String tableName = req.getParameter("pathToTable");
			String[] headers = CSV_Interpret.getHeader(f);
			//String fileNameNoExt = FilenameUtils.removeExtension(req.getParameter("fileName"));
			String pathToTable = getPathToTable(databaseType, tableName);
			
			try {
				String JDBC_URL = DatabaseConstants.getDatabaseJDBC(databaseType,host,databaseSID);
				//should pass in valid username and password from form post as well, refer to wesonline
				Connection conn = DatabaseConstants.makeConnection(JDBC_URL,user,pwd);//make global
				setConnection(conn);
				PreparedStatement p = this.conn.prepareStatement(generateDropQuery(pathToTable));
				System.out.println(generateDropQuery(pathToTable));
				System.out.println("Trying to run the drop table query");
				p.execute();
				p.close();
				p = this.conn.prepareStatement(generateCreateQuery(pathToTable,headers));
				System.out.println("Table dropped");
				System.out.println("Trying to run the create table query");
				p.execute();
				p.close();
				System.out.println("Table created");
				output.println("Running insert queries");
				generateInsertQuery(pathToTable, req.getParameter("fileName"));
				output.println("Successfully inserted all data");
				

			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
