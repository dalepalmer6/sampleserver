package com.example.myproject.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import com.example.myproject.client.DataRetrieveService;
import com.example.myproject.client.RetrieveTableNameService;
import com.example.myproject.shared.DatabaseConstants;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DataRetrieveServiceImpl extends RemoteServiceServlet implements DataRetrieveService {
	
	public String createDataCell(String field) {
		return "<td>" + field + "</td>";
	}
	
	public String getDataFromDB(List<String> params) {
		String databaseType = params.get(0);
		String host = params.get(1);
		String user = params.get(2);
		String pwd = params.get(3);
		String databaseSID = params.get(4);
		String tableName = params.get(5);
		String pathToTable;
		switch(databaseType) {
		 	case "Oracle" : pathToTable = "\"" + tableName + "\"";
		 					break;
		 	default:		pathToTable = "csv_to_db.\"" +tableName+ "\"";
		 					break;
		}
		
		//String SQL = "select table_name from information_schema.tables where table_schema = \'csv_to_db\'";
		System.out.println(tableName);
		
		//actually receive all data from the table in question
		String SQL = "select * from " + pathToTable;
		System.out.println(SQL);
		String table = "<h1 align=\'center\'>" + tableName + "</h1><br><table align=\'center\'border=\'1\'>";
		try {
			String JDBC_URL = DatabaseConstants.getDatabaseJDBC(databaseType,host,databaseSID);
			Connection conn = DatabaseConstants.makeConnection(JDBC_URL,user,pwd);
			PreparedStatement p = conn.prepareStatement(SQL);
			ResultSet r = p.executeQuery();
			ResultSetMetaData r_meta = r.getMetaData();
			int colCount = r_meta.getColumnCount();
			table += "<tr>";
			for (int i = 0; i < colCount; i++) {
				table += "<td><b>" + r_meta.getColumnName(i+1) + "</b></td>";
			}
			table += "</tr>";
			while (r.next()) {
				table += "<tr>";
				for (int i = 0; i < colCount; i++) {
					switch(r_meta.getColumnType(i+1)) {
					}
					table += createDataCell(r.getString(i+1)); //create a table out of these values and return as the response!
				}
				table += "</tr>";
			}
			table += "</table>";
			p.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return table;
	}
	
}
