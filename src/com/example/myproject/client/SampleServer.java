package com.example.myproject.client;

import com.example.myproject.shared.FieldVerifier;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sun.org.apache.xalan.internal.xsltc.DOM;

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

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	//private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//get the formpanel for the upload file section ready
		final VerticalPanel panel = new VerticalPanel();
		final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    form.setAction(GWT.getModuleBaseURL() + "greet");

	    final VerticalPanel panel2 = new VerticalPanel();
	    final FormPanel form2 = new FormPanel();
	    form2.setEncoding(FormPanel.ENCODING_URLENCODED);
	    form2.setMethod(FormPanel.METHOD_POST);
	    form2.setAction(GWT.getModuleBaseURL() + "dataParser");
	    
	    Button sendButton = new Button("Send to Server", new ClickHandler() {
			  @Override
		      public void onClick(ClickEvent event) {
			        form.submit();
			      }
			    });
	    
		form.setWidget(panel);
		form2.setWidget(panel2);
		panel.add(sendButton);
		
		//items for the first form field are set up from the start
	    final FileUpload nameField = new FileUpload();
		nameField.setName("file");
		panel.add(nameField);

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("FileUploadSpace").add(form);
		RootPanel.get("HeadersDataTypes").add(form2);
		
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	        @Override
	        public void onSubmitComplete(SubmitCompleteEvent event) {
	          // When the form submission is successfully completed, this event is
	          // fired. Assuming the service returned a response of type text/html,
	          // we can get the result text here (see the FormPanel documentation for
	          // further explanation).
	        	//change to the exterior div to make work
	        	panel2.clear();
	    	    panel2.add(new InlineHTML(event.getResults()));
	    	    //move this if possible
	        	panel2.add(new Button("UploadDatabase", new ClickHandler() {
	  			  @Override
	  		      public void onClick(ClickEvent event) {
	  			        form2.submit();
	  			      }
	  			    }));
	        }
	      });
	}

}
