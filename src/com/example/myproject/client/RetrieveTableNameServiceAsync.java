package com.example.myproject.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RetrieveTableNameServiceAsync {
	void getTableNamesFromDB(List<String> params, AsyncCallback<List<String>> callback) throws IllegalArgumentException;
}
