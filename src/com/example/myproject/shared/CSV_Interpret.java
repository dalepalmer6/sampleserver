package com.example.myproject.shared;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSV_Interpret {
	//test method that outputs a string to a file "output.txt"
	private static String del = ",";
	
	public static void setDelimiter(String d) {
		del = d;
	}
	
	public static String getDelimiter() {
		return del;
	}
	
	//test method that outputs a string to a file "output.txt"
	public static void outputStringToFile(String s) throws FileNotFoundException {
		File output = new File("output.txt");
		PrintWriter out = new PrintWriter(output);
		out.print(s);
		out.flush();
		out.close();
	}
	
		
		//used to get the headers only
		public static String[] getHeader(File f) throws FileNotFoundException {
			Scanner fileIn = new Scanner(f);
			String[] header;
			if (fileIn.hasNextLine()) {
				String line = fileIn.nextLine();
				header = splitLineWithDelimiter(line);
			}
			else {
				header = new String[0];
			}
			return header;
		}
		
		
		public static List<String[]> splitLinesWithDelimiter(List<String> fileLines) {
			List<String[]> rows = new ArrayList<String[]>();
			System.out.println("1.1");
			for (int l = 0; l < fileLines.size(); l++) {	
				if (l % 200 == 0) {
					System.out.println("1.1." + l);
				}
				rows.add(splitLineWithDelimiter(fileLines.get(l)));
			}
			System.out.println("1.2");
			return rows;
		}
		
		public static String[] splitLineWithDelimiter(String line) {
			String[] row = line.split("\\s*" + "," + "\\s*");
			return row;
		}
		
		//returns row i from the List, note that headers are i=0
		public static String[] getRow(List<String[]> l, int i) {
			return l.get(i);
		}	
}
