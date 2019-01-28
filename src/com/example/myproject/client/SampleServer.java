package com.example.myproject.client;

import com.example.myproject.shared.FieldVerifier;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
//	public void onModuleLoad() {}
	public void onModuleLoad() {
		final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);
	    form.setAction(GWT.getModuleBaseURL() + "greet");
	    
//	    final VerticalPanel panel = new VerticalPanel();
//	    form.setWidget(panel);
	    // submit button
		final Button sendButton = new Button("Send to Server", new ClickHandler() {
		      public void onClick(ClickEvent event) {
			        form.submit();
			      }
			    });
		final FileUpload nameField = new FileUpload();
		nameField.setName("file");

		
		form.add(nameField);
		form.add(sendButton);
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("FileUploadSpace").add(form);
	}

}
