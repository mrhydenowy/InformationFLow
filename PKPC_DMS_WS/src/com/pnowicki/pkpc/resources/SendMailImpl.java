package com.pnowicki.pkpc.resources;

import javax.mail.*;
import javax.mail.internet.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;

import com.pnowicki.pkpc.interfaces.mailResource;
import com.sun.jersey.multipart.FormDataParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

@Path("/sendmail")
@XmlRootElement
public class SendMailImpl implements mailResource {
	private String host;
	private int port;
	// Adres email osby która wysy³a maila
	private String userMail;
	// Has³o do konta osoby która wysy³a maila
	private String mailPassword;
	// Adresy email osob do których wysy³amy mail
	private String[] mails;
	// Temat wiadomoœci
	private String mailSubject = new String();
	// Treœæ wiadomoœci
	private String mailContent = new String();
	// Œcie¿ka do pliku
	private String PATH_FILE;
	private int fileIndex;
	private boolean resultDocumentCheckBox;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	@Override
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
			@FormDataParam("mailPassword") String mailPassword) {
		// this.tablica = tablica;

		int counter = 0;

		//zliczamy ilosc przecinkow i takiej wielkosci te¿ stworzymy tablice
		for (int i = 0; i < mailsString.length(); i++) {
			if (mailsString.charAt(i) == ',')
				counter++;
		}

		this.mails = new String[counter];

		//czyscimy tablice
		for (int i = 0; i < this.mails.length; i++) {
			this.mails[i] = "";
		}

		counter = 0;

		//przypisujemy do tablicy e-maile
		for (int i = 0; i < mailsString.length(); i++) {
			if (mailsString.charAt(i) != ',')
				this.mails[counter] += mailsString.charAt(i);
			else if (mailsString.charAt(i) == ',')
				counter++;
		}

		this.mailSubject = mailSubject;
		this.mailContent = mailContent;
		this.PATH_FILE = fileDir;
		this.fileIndex = Integer.parseInt(fileIndex);
		this.resultDocumentCheckBox = Boolean
				.parseBoolean(resultDocumentCheckBox);
		this.host = host;
		this.port = Integer.parseInt(port);
		this.userMail = userMail;
		this.mailPassword = mailPassword;

		try {
			send();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return Response.ok().build();
	}
	
	void send() throws MessagingException {
		//jezeli uzytkownik zaznaczyl, ¿e chce zatwierdzic pismo, wtedy aktualizujemy baze danych
		if (resultDocumentCheckBox) {
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
		}

		Properties props = new Properties();

		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.auth", "true");

		// Pobranie sesji
		Session mailSession = Session.getDefaultInstance(props, null);

		// Tworzenie wiadomoœci
		MimeMessage message = new MimeMessage(mailSession);
		message.setSubject(mailSubject);

		// Stworzenie czêœci wiadomosci z treœci¹
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent(mailContent, "text/html; charset=ISO-8859-2");

		// Stworzenie czêœci z za³acznikami
		MimeBodyPart attachFilePart = new MimeBodyPart();
		FileDataSource fds = new FileDataSource(PATH_FILE);
		attachFilePart.setDataHandler(new DataHandler(fds));
		attachFilePart.setFileName(fds.getName());

		// Zestawienie obydwu czêœci maila w jedn¹ wieloczêœciow¹
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(textPart);
		mp.addBodyPart(attachFilePart);

		// Dodanie treœci maila
		message.setContent(mp);

		for (int i = 0; i < mails.length; i++) {
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					mails[i]));
		}

		Transport transport = mailSession.getTransport();
		transport.connect(host, port, userMail, mailPassword);

		// Wys¹³nei maila
		transport.sendMessage(message,
				message.getRecipients(Message.RecipientType.TO));
		transport.close();
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