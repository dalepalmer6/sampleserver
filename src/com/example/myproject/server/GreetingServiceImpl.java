package com.example.myproject.server;

import com.example.myproject.client.GreetingService;
import com.example.myproject.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
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
		File tempFile = new File("tmpFile");
		ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> fileList;
		try {
			fileList = servletFileUpload.parseRequest(req);
			Iterator<FileItem> it = fileList.iterator();
			FileItem fileItemTemp = (FileItem) it.next();
			fileItemTemp.write(tempFile);
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
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) 
			throws ServletException, IOException {
					File tempFile = getFileFromRequest(req);
					List<String> fileLines = getLinesFromFile(tempFile);
					List<List<String>> rows = splitLinesWithDelimiter(",", fileLines);
					printAllRows(rows);
	}
}
