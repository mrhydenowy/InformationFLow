package com.pnowicki.pkpc.resources;

import java.io.File;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class test
{
	public static void main(String[] args, Object logger) 
	{
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client = Client.create(config);
		client.addFilter(new LoggingFilter());
		WebResource webResource = client.resource("http://localhost:8080/webapp/resources").path("ingredient");
		FormDataMultiPart fdmp = new FormDataMultiPart();
		File file = null;
		if (file != null) {
		    fdmp.bodyPart(new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE));
		}
		
		fdmp.bodyPart(new FormDataBodyPart("name", "TestName"));
		fdmp.bodyPart(new FormDataBodyPart("description", "TestDescription"));

		ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);
		String string = response.getEntity(String.class);
		//logger.log(Level.INFO, "response: {0}", string);
	}
}