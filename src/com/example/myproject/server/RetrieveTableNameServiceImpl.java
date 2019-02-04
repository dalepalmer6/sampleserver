package com.example.myproject.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.myproject.client.RetrieveTableNameService;
import com.example.myproject.shared.DatabaseConstants;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RetrieveTableNameServiceImpl extends RemoteServiceServlet implements RetrieveTableNameService {
	
	public List<String> getTableNamesFromDB(List<String> params) {
		//params comes in the order databasetype,hostname,user,pwd,dbSID
		
		String databaseType = params.get(0);
		String host = params.get(1);
		String user = params.get(2);
		String pwd = params.get(3);
		String databaseSID = params.get(4);
		
		System.out.println("Printing out the parameters");
		System.out.println(databaseType + host + user + pwd + databaseSID);
		//make connection to db like in the old one and query for the same things
		ArrayList tableNames = new ArrayList<String>();
		String SQL = "";
		switch(databaseType) {
			case "Oracle" : SQL = "SELECT table_name FROM user_tables WHERE TABLE_NAME LIKE \'%csv_to_db_%\'";
							break;
			default:		SQL = "SELECT table_name FROM information_schema.tables WHERE table_schema = \'csv_to_db\'";
		}
		
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
		for (int i = 0; i < tableNames.size(); i++) {
			System.out.println(tableNames.get(i));
		}
		
		return tableNames;
	}
}
