package com.pnowicki.pkpc.interfaces;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.FormDataParam;

public interface DocumentsResource {
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDocument(@FormDataParam("fileName") String fileName,
			@FormDataParam("fileSignature") String fileSignature,
			@FormDataParam("filePath") String filePath,
			@FormDataParam("file") File file,
			@FormDataParam("endFile") String endFile) throws Exception;
}