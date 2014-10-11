package com.pnowicki.pkpc.resources;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.multipart.FormDataParam;

@Path("/documents")
@XmlRootElement
public class DocumentsResource
{
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDocument(@FormDataParam("name") String name, @FormDataParam("signature") String signature, @FormDataParam("filePath") String filePath, 
								@FormDataParam("file") File file, @FormDataParam("endFile") String endFile) throws Exception
	{
		try 
		{
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName(LoginResource.driverDataBase);

			//connect = DriverManager.getConnection("jdbc:mysql://localhost/PKPCargoDMS?" + "user=" + userDataBase + "&password=" + passwordDataBase);
			connect = DriverManager.getConnection(LoginResource.firstAddressDataBase + "user=" + LoginResource.userDataBase + "&password=" + LoginResource.passwordDataBase);

			preparedStatement = connect.prepareStatement("insert into  PKPCargoDMS.documents values (default, ?, ?, default, ?, ?, null)");
		    preparedStatement.setString(1, name);
		    preparedStatement.setString(2, signature);
		    preparedStatement.setString(3, filePath);
		    preparedStatement.setBoolean(4, false);
		    preparedStatement.executeUpdate();
		      
		    transferFiles(file, name + "+" + signature + "." + endFile, LoginResource.finalPath);
		} 
		catch (Exception exc) 
		{
			exc.printStackTrace();
		}
		finally 
	    {
	    	close();
	    }
		
		return Response.ok().build(); 
	}
	
	private void transferFiles(File file, String fileName, String finalPath) throws Exception
    {
		FileInputStream in = new FileInputStream(file);
    	FileOutputStream out = new FileOutputStream(finalPath + fileName);
    	byte[] buffer = new byte[1024];
    	int read;
    	while ((read = in.read(buffer)) != -1) 
    	{
    	    out.write(buffer, 0, read);
    	}
    	in.close();
    	out.close();
    }
	
	private void close() 
    {
		try 
		{
			if(resultSet != null) 
				resultSet.close();

			if(statement != null) 
				statement.close();

			if(connect != null) 
				connect.close();
		} 
		catch(Exception e) 
		{
    	  
		}
    }
}