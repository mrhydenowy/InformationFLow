package com.pnowicki.pkpc.resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.jersey.multipart.FormDataParam;

@Path("/login")
@XmlRootElement
public class LoginResource {
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	static String driverDataBase = new String();
	static String firstAddressDataBase = new String();
	static String userDataBase = new String();
	static String passwordDataBase = new String();
	static String finalPath = new String();

	@PUT
	@Path("/login")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String tmpUsers(@FormDataParam("userName") String userName,
			@FormDataParam("userPassword") String userPassword)
			throws Exception {
		readFile();
		String tmp = "";
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			Class.forName(driverDataBase);

			// connect =
			// DriverManager.getConnection("jdbc:mysql://localhost/PKPCargoDMS?"
			// + "user=" + userDataBase + "&password=" + passwordDataBase);
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

	@GET
	@Path("/getFinalPath")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String getFinalPath() {
		return finalPath;
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

	private void readFile() {
		try {
			FileReader fileReader = new FileReader(
					"C://Users/Nowy/Documents/workspace InformationFlow with JavaSimon/PKPC_DMS_WS/config.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String content;

			while ((content = bufferedReader.readLine()) != null) {
				if (content.equals("driver:"))
					driverDataBase = bufferedReader.readLine();
				else if (content.equals("firstAddressDataBase:"))
					firstAddressDataBase = bufferedReader.readLine();
				else if (content.equals("user:"))
					userDataBase = bufferedReader.readLine();
				else if (content.equals("password:"))
					passwordDataBase = bufferedReader.readLine();
				else if (content.equals("finalPath:"))
					finalPath = bufferedReader.readLine();
			}
			fileReader.close();

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}