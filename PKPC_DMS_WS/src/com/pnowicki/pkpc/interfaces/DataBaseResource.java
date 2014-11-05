package com.pnowicki.pkpc.interfaces;

import java.io.File;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;

public interface DataBaseResource {
	public Response addDocument(String fileName, String fileSignature, String filePath, File file, String endFile) throws Exception;
	public String getUserRole(String userName, String userPassword) throws Exception;
	public JSONArray getFiles();
	public JSONArray getBooleans();
	public JSONArray getInts();
	public Response setConsidered(String fileIndex);
}