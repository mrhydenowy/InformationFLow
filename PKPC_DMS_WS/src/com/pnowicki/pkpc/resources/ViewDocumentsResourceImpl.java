package com.pnowicki.pkpc.resources;

import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONArray;

import com.pnowicki.pkpc.interfaces.ViewDocumentsResource;
import com.sun.jersey.multipart.FormDataParam;

@Path("/viewdocuments")
@XmlRootElement
public class ViewDocumentsResourceImpl implements ViewDocumentsResource {
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;

	static JSONArray fileNameArray = new JSONArray();
	static JSONArray consideredArray = new JSONArray();
	static JSONArray documentIdArray = new JSONArray();

	@Override
	@PUT
	@Path("/openFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	//otwiera plik w domyslnym programie
	public Response openFile(@FormDataParam("path") String path)
			throws Exception {
		File dir = new File(path);
		Desktop.getDesktop().open(dir);
		return Response.ok().build();
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