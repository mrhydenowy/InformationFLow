package com.pnowicki.pkpc.interfaces;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;

public interface ViewDocumentsResource {
	public Response openFile(String path) throws Exception;
	public JSONArray getFiles();
	public JSONArray getBooleans();
	public JSONArray getInts();
}
