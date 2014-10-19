package com.pnowicki.pkpc.interfaces;

import javax.ws.rs.core.Response;

public interface ViewDocumentsResource {
	public Response openFile(String path) throws Exception;
	public String getFiles();
	public String getBooleans();
	public String getInts();
}
