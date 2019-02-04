package com.example.myproject.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataRetrieveServiceAsync {
	void getDataFromDB(List<String> params, AsyncCallback<String> callback) throws IllegalArgumentException;
}
