package com.example.myproject.server;

import com.example.myproject.shared.CSV_Interpret;
import com.example.myproject.shared.DatabaseConstants;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class FileReadingServiceImpl extends HttpServlet {
	
	//creates the dropdown menu that displays the data types for the 
	public String inputDropDownMenuForHeaders(int fieldNumber) {
		String HTML = "<select name=\'datatype" + fieldNumber + "\'>";
		for (int i = 0; i < DatabaseConstants.getDataTypes().length; i++) {
			HTML += "   <option value=\'" + DatabaseConstants.getDataTypes()[i] + "\'>" + DatabaseConstants.getDataTypes()[i] + "</option>\n";
		}
		return HTML;
	}
	
	//returns the row as a table in column form (Useful for the headers)
	public String rowToColumnHTML(String[] l) {
		//TODO tidy up HTML output
		String HTML = "";
		for (int i = 0; i < l.length; i++) {
			//put in the dropdown menus for data types
			HTML += "\n   <tr>\n      <td>\n         " + l[i] + "</td>\n      <td>"
					+ inputDropDownMenuForHeaders(i) + "</td>\n   </tr>\n";
		}
		return HTML;
	}
	
	//create a form that can be used to receive the data types from the user
	public String getHeaderAsHTML(String[] l) {
		String HTML_table = "\n   <table>\n";
		HTML_table += rowToColumnHTML(l);
		return HTML_table + "   </table>\n";
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
			tempFile.delete();
			tempFile = new File(fileItemTemp.getName());
			fileItemTemp.write(tempFile);
			return tempFile;
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempFile;
	}
	
	public String returnResponseTable(File tempFile, HttpServletResponse res) throws IOException {
		System.out.println("0");
		String[] header = CSV_Interpret.getHeader(tempFile);
		String out = "<p><b>Select the appropriate data types for the fields:</b></p>" + getHeaderAsHTML(header) + "<p><b>Confirm the credentials to access and insert data to the database:</b></p>" + DatabaseConstants.getDatabaseFields(tempFile.getName());
		return out;
	}
	
	public String validateFileName(String fname) {
		System.out.println(FilenameUtils.getExtension(fname));
		if (FilenameUtils.getExtension(fname).equals("csv")) {
			return "";
		}
		else if (fname.equals("")) {
			return "No file selected.";
		}
		else {
			return "The application only supports files with the .csv extension!";
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
					PrintWriter out = res.getWriter();
					File tempFile = getFileFromRequest(req);
					if (validateFileName(tempFile.getName()).equals("")) {
						String output = returnResponseTable(tempFile, res);
						out.write(output);
						out.flush();
					}
					else {
						//return a 400 error, indicating the server was unable to complete the request
						res.sendError(400,validateFileName(tempFile.getName()));
					}
	}
}
