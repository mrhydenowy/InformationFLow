package com.pnowicki.pkpc.resources;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;

import com.pnowicki.pkpc.interfaces.DataBaseResource;
import com.sun.jersey.multipart.FormDataParam;

public class DataBaseResourceImpl implements DataBaseResource{

	static String driverDataBase = new String();
	static String firstAddressDataBase = new String();
	static String userDataBase = new String();
	static String passwordDataBase = new String();
	static String finalPath = new String();
	
	static JSONArray fileNameArray = new JSONArray();
	static JSONArray consideredArray = new JSONArray();
	static JSONArray documentIdArray = new JSONArray();
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	@Override
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDocument(@FormDataParam("fileName") String fileName,
			@FormDataParam("fileSignature") String fileSignature,
			@FormDataParam("filePath") String filePath,
			@FormDataParam("file") File file,
			@FormDataParam("endFile") String endFile) throws Exception {
		try {
			Class.forName(LoginResourceImpl.driverDataBase);

			connect = DriverManager
					.getConnection(LoginResourceImpl.firstAddressDataBase + "user="
							+ LoginResourceImpl.userDataBase + "&password="
							+ LoginResourceImpl.passwordDataBase);

			preparedStatement = connect
					.prepareStatement("insert into  PKPCargoDMS.documents (document_id, name, signature, create_date, file_path, considered, date_of_considered) values (default, ?, ?, default, ?, ?, null)");
			preparedStatement.setString(1, fileName);
			preparedStatement.setString(2, fileSignature);
			preparedStatement.setString(3, filePath);
			preparedStatement.setBoolean(4, false);
			preparedStatement.executeUpdate();

		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close();
		}

		return Response.ok().build();
	}

	@Override
	@PUT
	@Path("/login")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String getUserRole(@FormDataParam("userName") String userName,
			@FormDataParam("userPassword") String userPassword)
			throws Exception {
		String tmp = "";
		try {
			Class.forName(driverDataBase);

			connect = DriverManager.getConnection(firstAddressDataBase
					+ "user=" + userDataBase + "&password=" + passwordDataBase);

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from pkpcargodms.users");
			tmp = writeResultSet(resultSet, userName, userPassword);
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close();
		}

		return tmp;
	}
	
	private String writeResultSet(ResultSet resultSet, String userName,
			String userPassword) throws SQLException {
		String tmp = "";

		while (resultSet.next()) {
			String nameUser = resultSet.getString("name");
			String passwordUser = resultSet.getString("password");
			String roleUser = resultSet.getString("role");

			if (nameUser.equals(userName) && passwordUser.equals(userPassword)) {
				tmp = roleUser;
				break;
			} else {
				tmp = "";
			}
		}
		return tmp;
	}

	@Override
	@GET
	@Path("/getFiles")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getFiles() {
		try {
			Class.forName(LoginResourceImpl.driverDataBase);

			connect = DriverManager
					.getConnection(LoginResourceImpl.firstAddressDataBase + "user="
							+ LoginResourceImpl.userDataBase + "&password="
							+ LoginResourceImpl.passwordDataBase);

			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select file_path, name, signature, considered, document_id from PKPCargoDMS.documents");

			while (resultSet.next()) {
				String file_path = resultSet.getString("file_path");
				String name = resultSet.getString("name");
				String signature = resultSet.getString("signature");
				boolean considered = resultSet.getBoolean("considered");
				int documentId = resultSet.getInt("document_id");
				
				String endFile = new String("");

				for (int i = 0; i < file_path.length(); i++) {
					if (file_path.charAt(i) == '.')
						endFile = "";
					else if (file_path.charAt(i) != '.')
						endFile += file_path.charAt(i);
				}

				//przypisujemy do tablic odpowiednie dane.
				fileNameArray.put(name + "+" + signature + "." + endFile);
				consideredArray.put(considered);
				documentIdArray.put(documentId);

			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close();
		}
		
		return fileNameArray;
	}

	@Override
	@GET
	@Path("/getBooleans")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getBooleans() {
		return consideredArray;
	}

	@Override
	@GET
	@Path("/getInts")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getInts() {
		return documentIdArray;
	}

	@Override
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response setConsidered(
			@FormDataParam("fileIndex") String fileIndex) {
		try {
			Class.forName(LoginResourceImpl.driverDataBase);

			connect = DriverManager
					.getConnection(LoginResourceImpl.firstAddressDataBase
							+ "user=" + LoginResourceImpl.userDataBase
							+ "&password=" + LoginResourceImpl.passwordDataBase);

			statement = connect.createStatement();

			preparedStatement = connect
					.prepareStatement("UPDATE documents SET considered = '1', date_of_considered = CURRENT_TIMESTAMP WHERE document_id='"
							+ fileIndex + "'");

			preparedStatement.executeUpdate();
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close();
		}

		return Response.ok().build();
	}
	
		
	private void close() {
		try {
			if (resultSet != null)
				resultSet.close();

			if (statement != null)
				statement.close();

			if (connect != null)
				connect.close();
		} catch (Exception e) {

		}
	}
}
