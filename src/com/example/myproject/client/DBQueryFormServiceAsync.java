package com.example.myproject.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBQueryFormServiceAsync {
	void getTable(AsyncCallback<String[]> callback) throws IllegalArgumentException;
}
