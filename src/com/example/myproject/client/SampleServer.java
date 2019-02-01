package com.example.myproject.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sun.org.apache.xalan.internal.xsltc.DOM;

import java.util.ArrayList;
import java.util.List;

import com.example.myproject.client.GreetingService;
import com.example.myproject.client.GreetingServiceAsync;

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
	
	private ArrayList<String> msg = new ArrayList<String>();
	private final DBQueryFormServiceAsync dbQueryFormService = GWT.create(DBQueryFormService.class);
	private String[] receivedFields;
	/**
	 * This is the entry point method.
	 */
	
	private void getInfoFromServer() {
		AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
			public void onSuccess(String[] fields) {
				//this is where the magic happens
				receivedFields = fields;
			}
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("Failed");
			}
		};
		dbQueryFormService.getTable(callback);
	}
	
	public VerticalPanel createTable() {
		VerticalPanel p = new VerticalPanel();
		for (int i = 0; i < receivedFields.length; i+=2) {
			p.add(createRow(receivedFields[i],receivedFields[i+1]));
		}
		return p;
	}
	
	public HorizontalPanel createRow(String label, String text) {
		HorizontalPanel r = new HorizontalPanel();
		Label labelElement = new Label(label);
		TextBox textBoxElement = new TextBox();
		textBoxElement.setText(text);
		r.add(labelElement);
		r.add(textBoxElement);
		return r;
	}
	
	public void onModuleLoad() {
		//get the formpanel for the upload file section ready
		final VerticalPanel panel = new VerticalPanel();
		final FormPanel formFileUpload = new FormPanel();
		formFileUpload.setEncoding(FormPanel.ENCODING_MULTIPART);
	    formFileUpload.setMethod(FormPanel.METHOD_POST);
	    formFileUpload.setAction(GWT.getModuleBaseURL() + "greet");

	    final VerticalPanel panel2 = new VerticalPanel();
	    final FormPanel formUploadToDatabase = new FormPanel();
	    formUploadToDatabase.setEncoding(FormPanel.ENCODING_URLENCODED);
	    formUploadToDatabase.setMethod(FormPanel.METHOD_POST);
	    formUploadToDatabase.setAction(GWT.getModuleBaseURL() + "dataParser");
	    
	    final VerticalPanel panel3 = new VerticalPanel();
		
		final VerticalPanel panel4 = new VerticalPanel();
		final FormPanel formTableData = new FormPanel();
		formTableData.setEncoding(FormPanel.ENCODING_URLENCODED);
		formTableData.setMethod(FormPanel.METHOD_POST);
		formTableData.setAction(GWT.getModuleBaseURL() + "dataRetrieve");
		
		final VerticalPanel panel5 = new VerticalPanel();
		
	    Button sendButton = new Button("Confirm", new ClickHandler() {
			  @Override
		      public void onClick(ClickEvent event) {
			        formFileUpload.submit();
			      }
			    });
	    
	    
	    panel.add(new Button("Load Data From a Database:", new ClickHandler() {
			  @Override
		      public void onClick(ClickEvent event) {
				  	getInfoFromServer();
				  	panel3.clear();
				  	VerticalPanel table = createTable();
				  	panel3.add(table);
			      }
			    }));
	    
		formFileUpload.setWidget(panel);
		formUploadToDatabase.setWidget(panel2);
		formTableData.setWidget(panel4);

		//items for the first form field are set up from the start
	    final FileUpload nameField = new FileUpload();
		nameField.setName("file");
		panel.add(new InlineHTML("<p><b>Please select a .csv file to load data from<br>or load data from a database.</b></p>"));
		panel.add(nameField);
		panel.add(sendButton);
		panel.add(new InlineHTML("<br>"));
		
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("FileUploadSpace").add(formFileUpload);
		RootPanel.get("HeadersDataTypes").add(formUploadToDatabase);
		RootPanel.get("DatabaseSelectForm").add(panel3);
		RootPanel.get("DatabaseSelectForm").add(formTableData);
		RootPanel.get("SelectedData").add(panel5);
		
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
		//this is the final step of the upload sequence
		formUploadToDatabase.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
	        public void onSubmitComplete(SubmitCompleteEvent event) {
	        	panel2.add(new InlineHTML(event.getResults()));
	        	
	        }
	    });
		
//		formLoadFromDatabase.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
//	        @Override
//	        public void onSubmitComplete(SubmitCompleteEvent event) {
//	        	panel3.clear();
//	        	panel4.clear();
//	        	panel3.add(new InlineHTML(event.getResults()));
//	        	panel3.add(new Button("Load Available Tables", new ClickHandler() {
//		  			  @Override
//		  		      public void onClick(ClickEvent event) {
//		  				  //clicking this button should get the tables from the db 
//		  				  panel3.clear();
//		  				  getInfoFromServer();
//		  			      }
//		  			    }));
//	        }
//	    });
//		formGetTables.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
//	        @Override
//	        public void onSubmitComplete(SubmitCompleteEvent event) {
//	        	panel4.clear();
//	        	formGetTables.setWidget(panel3);
//	        	panel4.add(new InlineHTML(event.getResults()));
//	        	panel4.add(new Button("Query Database for Selected Table", new ClickHandler() {
//		  			  @Override
//		  		      public void onClick(ClickEvent event) {
//		  				  //clicking this button should get the tables from the db 
//		  				  formTableData.submit();
//		  				  	//Window.alert("Button pushed, load data from db");
//		  			      }
//		  			    }));
//	        }
//	    });
//		formTableData.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
//	        @Override
//	        public void onSubmitComplete(SubmitCompleteEvent event) {
//	        	panel5.clear();
//	        	formGetTables.setWidget(panel3);
//	        	panel5.add(new InlineHTML(event.getResults()));
//	        	/*panel5.add(new Button("Query Database for Selected Table", new ClickHandler() {
//		  			  @Override
//		  		      public void onClick(ClickEvent event) {
//		  				  //clicking this button should get the tables from the db 
//		  				  formTableData.submit();
//		  				  	//Window.alert("Button pushed, load data from db");
//		  			      }
//		  			    }));
//	        	 */
//	        }
//	    });
		
	}

}
