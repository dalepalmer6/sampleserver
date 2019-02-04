package com.example.myproject.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dataRetrieve")
public interface DataRetrieveService extends RemoteService {
	String getDataFromDB(List<String> params) throws IllegalArgumentException;
}
