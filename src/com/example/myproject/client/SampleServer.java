package com.example.myproject.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sun.org.apache.xalan.internal.xsltc.DOM;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.myproject.shared.DatabaseConstants;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SampleServer implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";
	
	private static String[] dataTypes = {"String","Date","Number"};
	private static String[] databases = {"Postgres", "Oracle", "SQL"};
	private static String[] DBQueryFormElements = {"dbHost", "pg1-devel.compusult.net", "user", "dale_wesweb", "pwd", "wes", "dbSID", "dale"};
	private final RetrieveTableNameServiceAsync retrieveTableNameService = GWT.create(RetrieveTableNameService.class);
	private final DataRetrieveServiceAsync dataRetrieveService = GWT.create(DataRetrieveService.class);
	private FlexTable tableDatabaseQuery = new FlexTable();
	private List<String> tableNamesFromDB;
	//a table that displays the SELECTed data from the database
	private String HTML_Table;
	/**
	 * This is the entry point method.
	 */
	
	private void getSelectedData(List<String> params) {
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onSuccess(String s) {
				HTML_Table = s;
				updateRootPanel("SelectedData",HTML_Table);
			}
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("Failed");
			}
		};
		dataRetrieveService.getDataFromDB(params, callback);
	}
	
	private void getTablesFromDB(List<String> params) {
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onSuccess(List<String> fields) {
				//this is where the magic happens
				tableNamesFromDB = fields;
				appendTableNamesToTable();
			}
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("Failed");
			}
		};
		retrieveTableNameService.getTableNamesFromDB(params, callback);
	}
	
	public void updateRootPanel(String id, String text) {
		RootPanel.get(id).clear();
		RootPanel.get(id).add(new InlineHTML(text));
	}
	
	public void createDBQueryForm() {
		//creates the db query form
		ListBox lb = new ListBox();
		for (int i = 0; i < databases.length; i++) {
			lb.addItem(databases[i]);
		}
		tableDatabaseQuery.setWidget(0,0, new Label("Database Type: "));
		tableDatabaseQuery.setWidget(0,1, lb);
		for (int r = 0; r < DBQueryFormElements.length; r+=2) {
			tableDatabaseQuery.setWidget((r/2)+1,0,new Label(DBQueryFormElements[r])); 
			TextBox textbox = new TextBox();
			textbox.setText(DBQueryFormElements[r+1]);
			tableDatabaseQuery.setWidget((r/2)+1,1,textbox);
		}
	}
	
	public void appendTableNamesToTable() {
		//adds the table names to the database query form
		tableDatabaseQuery.setWidget(5,0,new Label("Table Names:"));
		ListBox l = new ListBox();
		l.setVisibleItemCount(1);	//make into dropdown
		for (int i = 0; i < tableNamesFromDB.size(); i++) {
			l.addItem(tableNamesFromDB.get(i));
		}
		tableDatabaseQuery.setWidget(5, 1, l);
	}

	public List<String> getParamsFromTable(){
		//gets the parameters for the database as input by the user in TextBox elements
		List<String> params = new ArrayList<String>();
	    for (int r = 0; r < tableDatabaseQuery.getRowCount(); r++) {
	    	if (tableDatabaseQuery.getWidget(r,1) instanceof TextBox) {
	    		params.add(((TextBox) tableDatabaseQuery.getWidget(r,1)).getText());
	    	}
	    	if (tableDatabaseQuery.getWidget(r,1) instanceof ListBox) {
	    		params.add(((ListBox) tableDatabaseQuery.getWidget(r,1)).getSelectedValue());
	    	}
	    }
	    return params;
	}
	
	public void onModuleLoad() {
		//get the formpanel for the upload file section ready
		final VerticalPanel panel = new VerticalPanel();
		final FormPanel formFileUpload = new FormPanel();
		formFileUpload.setEncoding(FormPanel.ENCODING_MULTIPART);
	    formFileUpload.setMethod(FormPanel.METHOD_POST);
	    formFileUpload.setAction(GWT.getModuleBaseURL() + "fileReadServlet");
	    final VerticalPanel panel2 = new VerticalPanel();
	    final FormPanel formUploadToDatabase = new FormPanel();
	    formUploadToDatabase.setEncoding(FormPanel.ENCODING_URLENCODED);
	    formUploadToDatabase.setMethod(FormPanel.METHOD_POST);
	    formUploadToDatabase.setAction(GWT.getModuleBaseURL() + "dataParser");
	    final VerticalPanel panel3 = new VerticalPanel();
		
		/*
		 * Create Buttons for the web page to use, with specific calls to servers for each
		 */
		final Button queryDatabase = new Button("Query the Database", new ClickHandler() {
			 /*
			  * When the button is pressed, the database is queried for the selected table
			  */
			  @Override
		      public void onClick(ClickEvent event) {
				  	List<String> params = getParamsFromTable();
				  	getSelectedData(params);
			      }
			    });
		final Button getTableNames = new Button("Get Tables from DB", new ClickHandler() {
			/*
			 * When the button is pressed, the app tries to connect and get the database table names.
			 */
			  @Override
		      public void onClick(ClickEvent event) {
				  	List<String> params = getParamsFromTable();
				  	getTablesFromDB(params);	//server call
				  	panel3.add(queryDatabase);
			      }
			    });
		createDBQueryForm();
	  	
		panel3.clear();
		panel3.add(new InlineHTML("<p align=\'center\'><b>Enter the credentials to access the database you want to use.</b></p>"));
	  	panel3.add(tableDatabaseQuery);
	  	panel3.add(getTableNames);
	    
		formFileUpload.setWidget(panel);
		formUploadToDatabase.setWidget(panel2);

		//items for the first form field are set up from the start
	    final FileUpload nameField = new FileUpload();
	    nameField.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				formFileUpload.submit();
				
			}
	    });
		nameField.setName("file");
		panel.add(new InlineHTML("<p><b>Please select a .csv file to load data from<br>or load data from a database.</b></p>"));
		panel.add(nameField);
		panel.add(new InlineHTML("<br>"));
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("FileUploadSpace").add(formFileUpload);
		RootPanel.get("HeadersDataTypes").add(formUploadToDatabase);
		RootPanel.get("DatabaseSelectForm").add(panel3);
		
		formFileUpload.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	        @Override
	        public void onSubmitComplete(SubmitCompleteEvent event) {
	        	panel2.clear();
	    	    panel2.add(new InlineHTML(event.getResults()));
	    	    //move this if possible
	        	panel2.add(new Button("Upload to Database", new ClickHandler() {
	  			  @Override
	  		      public void onClick(ClickEvent event) {
	  			        formUploadToDatabase.submit();
	  			      }
	  			    }));
	        }
	      });
		
		formUploadToDatabase.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			//this is the final step of the upload sequence, and the process will return a message when complete
			@Override
	        public void onSubmitComplete(SubmitCompleteEvent event) {
	        	panel2.add(new InlineHTML(event.getResults()));
	        }
	    });
	}

}
