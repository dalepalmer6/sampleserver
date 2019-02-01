package com.example.myproject.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import com.example.myproject.client.DBQueryFormService;
import com.example.myproject.shared.DatabaseConstants;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DBQueryFormServiceImpl extends RemoteServiceServlet implements DBQueryFormService {
	private Connection conn;
	
	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
	
	
	public String[] getTable() {
		//return the parameters that will be made into a table by the client
		String HTML = "<p>Enter the database information that you wish to view:</p><br><table>"
				+ "<tr><td><label>Select a Database to Connect to: </td><td>" + DatabaseConstants.inputDropDownMenuDatabases("") + "</td></tr>"
				+ "<tr><td><label>Database Host: </label></td><td><input id=\'db_host\' name=\'db_host\' value=\'pg1-devel.compusult.net\'" + "></td></tr><br>"
				+ "<tr><td><label>Username: </label></td><td><input id=\'user\' name=\'user\' value=\'dale_wesweb\'" + "></td></tr>"
				+ "<tr><td><label>Password: </label></td><td><input id=\'pwd\' type=\'password\' name=\'pwd\' value=\'wes" + "\'></td></tr>"
				+ "<tr><td><label>Database/SID: </label></td><td><input id=\'dbSID\' name=\'dbSID\' value=\'dale\'" + "></td></tr>"
				+ "</table>";
//		VerticalPanel table = new VerticalPanel();
//		table.add(createRow("Database Host:","pg1-devel.compusult.net"));
//		table.add(createRow("Username:",""));
//		table.add(createRow("Password",""));
//		table.add(createRow("Database/SID",""));
		String[] tableElements = {"dbHost", "", "user", "", "pwd", "", "dbSID", ""};
		return tableElements;
	}
	
}
