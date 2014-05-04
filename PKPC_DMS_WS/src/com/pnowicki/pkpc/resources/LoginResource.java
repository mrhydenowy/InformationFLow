package com.pnowicki.pkpc.resources;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.multipart.FormDataParam;


@Path("/login")
@XmlRootElement
public class LoginResource 
{
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String tmpUsers(@FormDataParam("name") String name, @FormDataParam("password") String password, @FormDataParam("driverDataBase") String driverDataBase, 
						   @FormDataParam("firstAddressDataBase") String firstAddressDataBase, @FormDataParam("userDataBase") String userDataBase,
						   @FormDataParam("passwordDataBase") String passwordDataBase) throws Exception
	{
		String tmp = "";
		try 
		{
			//Class.forName("com.mysql.jdbc.Driver");
			Class.forName(driverDataBase);

			//connect = DriverManager.getConnection("jdbc:mysql://localhost/PKPCargoDMS?" + "user=" + userDataBase + "&password=" + passwordDataBase);
			connect = DriverManager.getConnection(firstAddressDataBase + "user=" + userDataBase + "&password=" + passwordDataBase);
			
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement.executeQuery("select * from pkpcargodms.users");
			tmp = writeResultSet(resultSet, name, password);
		} 
		catch (Exception exc) 
		{
			exc.printStackTrace();
		}
		finally 
	    {
	    	close();
	    }
		

		return tmp;
	}
	
	private String writeResultSet(ResultSet resultSet, String name, String password) throws SQLException 
    {
		String tmp = "";

      while (resultSet.next()) 
      {
        String nameUser = resultSet.getString("name");
        String passwordUser = resultSet.getString("password");
        String roleUser = resultSet.getString("role");
        
        if(nameUser.equals(name) && passwordUser.equals(password))
        {
        	tmp = roleUser;
        	break;
        }
        else
        {
        	tmp = "";
        }
      }
      return tmp;
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