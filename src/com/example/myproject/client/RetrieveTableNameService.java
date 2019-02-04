package com.example.myproject.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */

@RemoteServiceRelativePath("tableRetrieve")
public interface RetrieveTableNameService extends RemoteService {
	//the method that will be carried out by the server
	List<String> getTableNamesFromDB(List<String> params) throws IllegalArgumentException;
	
}