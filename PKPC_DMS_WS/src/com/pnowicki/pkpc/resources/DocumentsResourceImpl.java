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

import com.pnowicki.pkpc.interfaces.DocumentsResource;
import com.sun.jersey.multipart.FormDataParam;

@Path("/documents")
@XmlRootElement
public class DocumentsResourceImpl implements DocumentsResource {
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

			transferFiles(file, fileName + "+" + fileSignature + "." + endFile,
					LoginResourceImpl.finalPath);
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close();
		}

		return Response.ok().build();
	}

	//metoda przenosi plik do wskazanej œcie¿ki
	private void transferFiles(File file, String fileName, String finalPath)
			throws Exception {
		FileInputStream fileInputStream = new FileInputStream(file);
		FileOutputStream fileOutputStream = new FileOutputStream(finalPath
				+ fileName);
		byte[] buffer = new byte[1024];
		int read;
		while ((read = fileInputStream.read(buffer)) != -1) {
			fileOutputStream.write(buffer, 0, read);
		}
		fileInputStream.close();
		fileOutputStream.close();
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