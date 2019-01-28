package com.example.myproject.server;

import com.example.myproject.client.GreetingService;
import com.example.myproject.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.servlet.ServletException;


/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends HttpServlet {
	//All data types that can be input for the data headers
	private String[] dataTypes = {"Number","Date","String"};
	
	//Returns the row at index from the list rows
	public List<String> getRow(List<List<String>> l, int index) {
		return l.get(index);
	}
	
	//Creates a table that will be populated with the data
	public String createTable(List<List<String>> l) {
		String HTML_table = "<html><table>\n";
		for (int i = 0; i < l.size(); i++) {
			//for each row
			List<String> row = getRow(l, i);
			HTML_table += getRowAsHTML(row);
		}
		HTML_table += "</table></html>\n";
		return HTML_table;
	}
	
	//Forms a row for a table out of a row of values
	public String getRowAsHTML(List<String> l) {
		String HTML = "   <tr>\n";
		for (int i = 0; i < l.size(); i++) {
			HTML += "      <td>" + l.get(i) + "</td>\n";
		}
		return "   </tr>\n" + HTML;
	}
	
	public String inputDropDownMenu(int fieldNumber) {
		String HTML = "<select name=\'datatype" + fieldNumber + "\'>\n";
		for (int i = 0; i < this.dataTypes.length; i++) {
			HTML += "   <option value=\'" + dataTypes[i] + "\'>" + dataTypes[i] + "</option>\n";
		}
		return HTML;
	}
	
	public String rowToColumnHTML(List<String> l) {
		//TODO tidy up HTML output
		String HTML = "";
		for (int i = 0; i < l.size(); i++) {
			//put in the dropdown menus for data types
			HTML += "/n   <tr>\n      <td>\n         " + l.get(i) + "</td>\n      <td>"
					+ inputDropDownMenu(i) + "</td>\n   </tr>\n";
		}
		return HTML + "";
	}
	
	//create a form that can be used to receive the data types from the user
	public String getHeaderAsHTML(List<String> l) {
		String HTML_table = "\n   <table>\n";
		HTML_table += rowToColumnHTML(l);
		return HTML_table + "   </table>\n";
	}
	
	public void printAllRows(List<List<String>> l) {
		System.out.println("TRY");
		for (int i = 0; i < l.size(); i++) {
			String s = l.get(i).toString();
			System.out.println(s);
		}
	}
	
	public List<List<String>> splitLinesWithDelimiter(String del, List<String> fileLines) {
		List<List<String>> rows = new ArrayList<List<String>>();
		for (int l = 0; l < fileLines.size(); l++) {
			List<String> currentRow = new ArrayList<String>();
			//create a Scanner object which reads each value out of the csv fileline
			Scanner readValue = new Scanner(fileLines.get(l)).useDelimiter("\\s*" + del + "\\s*");
			while (readValue.hasNext()) {
				currentRow.add(readValue.next());
			}
			readValue.close();
			rows.add(currentRow);
		}
		return rows;
	}
	
	public File getFileFromRequest(HttpServletRequest req) {
		File tempFile = new File("temp_file_not_found");
		ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> fileList;
		try {
			fileList = servletFileUpload.parseRequest(req);
			Iterator<FileItem> it = fileList.iterator();
			FileItem fileItemTemp = (FileItem) it.next();
			tempFile = new File(fileItemTemp.getName());
			//TODO - if the file exists, alert the user that the file will be overwritten in the server
			fileItemTemp.write(tempFile);
			return tempFile;
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempFile;
	}
	
	public List<String> getLinesFromFile(File f) throws FileNotFoundException {
		Scanner fileIn = new Scanner(f);
		List<String> fileLines = new ArrayList<String>();
		while (fileIn.hasNextLine()) {
			fileLines.add(fileIn.nextLine());
		}
		return fileLines;
	}
	
	public void outputStringToFile(String s) throws FileNotFoundException {
		File output = new File("output.txt");
		PrintWriter out = new PrintWriter(output);
		out.print(s);
		out.flush();
		out.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
					File tempFile = getFileFromRequest(req);
					List<String> fileLines = getLinesFromFile(tempFile);
					List<List<String>> rows = splitLinesWithDelimiter(",", fileLines);
//					//print all rows to the server console
//					printAllRows(rows);
					//outputStringToFile(getHeaderAsHTML(rows.get(0)));
					PrintWriter out = res.getWriter();
					out.write(getHeaderAsHTML(rows.get(0)));
					out.flush();
	}
}
