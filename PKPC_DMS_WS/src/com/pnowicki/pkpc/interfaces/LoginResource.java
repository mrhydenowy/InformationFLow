package com.pnowicki.pkpc.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.multipart.FormDataParam;

public interface LoginResource {
	@PUT
	@Path("/login")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String tmpUsers(@FormDataParam("userName") String userName,
			@FormDataParam("userPassword") String userPassword)
			throws Exception;
	
	@GET
	@Path("/getFinalPath")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String getFinalPath();
}
