package com.pnowicki.pkpc.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.FormDataParam;


public interface SendMail {
	
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMail(
			@FormDataParam("mailSubject") String mailSubject,
			@FormDataParam("mailContent") String mailContent,
			@FormDataParam("mailsString") String mailsString,
			@FormDataParam("fileDir") String fileDir,
			@FormDataParam("fileIndex") String fileIndex,
			@FormDataParam("resultDocumentCheckBox") String resultDocumentCheckBox,
			@FormDataParam("host") String host,
			@FormDataParam("port") String port,
			@FormDataParam("userMail") String userMail,
			@FormDataParam("mailPassword") String mailPassword);
}
