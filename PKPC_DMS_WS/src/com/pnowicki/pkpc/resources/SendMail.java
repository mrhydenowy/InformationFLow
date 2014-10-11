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






import com.sun.jersey.multipart.FormDataParam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


@Path("/sendmail")
@XmlRootElement
public class SendMail 
{
    private String host;
    private int port;
    
    // Adres email osby która wysy³a maila
    private String from;

    // Has³o do konta osoby która wysy³a maila
    private String password;

    // Adresy email osob do których wysy³amy mail
    private String[] mails;

    // Temat wiadomoœci
    private String subject = new String();

    // Treœæ wiadomoœci
    private String content = new String();
    
	// Œcie¿ka do pliku
	private String PATH_FILE;
	
	private int index;
	
	private boolean bool;
	
	
    private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendMail(@FormDataParam("subject") String subject, @FormDataParam("content") String content, @FormDataParam("mails") String mails, @FormDataParam("file") String file, 
							 @FormDataParam("index") String index, @FormDataParam("bool") String bool, @FormDataParam("host") String host, @FormDataParam("port") String port, 
							 @FormDataParam("userMail") String userMail, @FormDataParam("passwordMail") String passwordMail) 
	{
		//this.tablica = tablica;
		
		int tmp = 0;
		
		for(int i = 0; i < mails.length(); i++)
		{
			if(mails.charAt(i) == ',')
				tmp++;
		}
		
		this.mails = new String[tmp];
		
		for(int i = 0; i < this.mails.length; i++)
		{
			this.mails[i] = "";
		}
		
		tmp = 0;
		
		for(int i = 0; i < mails.length(); i++)
		{
			if(mails.charAt(i) != ',')
				this.mails[tmp] += mails.charAt(i);
			else if(mails.charAt(i) == ',')
				tmp++;
		}
		
		this.subject = subject;
		this.content = content;
		this.PATH_FILE = file;
		this.index = Integer.parseInt(index);
		this.bool = Boolean.parseBoolean(bool);
		this.host = host;
		this.port = Integer.parseInt(port);
		this.from = userMail;
		this.password = passwordMail;
		
		//System.out.println(this.PATH_FILE);
		
		
		try 
        {
        	send();
       	} 
        catch (MessagingException e) 
        {
        	e.printStackTrace();
        }
        
		
		return Response.ok().build(); 
	}
	
    void send() throws MessagingException 
    {
    	if(bool)
    	{
	    	try 
			{
	    		//Class.forName("com.mysql.jdbc.Driver");
				Class.forName(LoginResource.driverDataBase);

				//connect = DriverManager.getConnection("jdbc:mysql://localhost/PKPCargoDMS?" + "user=" + userDataBase + "&password=" + passwordDataBase);
				connect = DriverManager.getConnection(LoginResource.firstAddressDataBase + "user=" + LoginResource.userDataBase + "&password=" + LoginResource.passwordDataBase);
				
				statement = connect.createStatement();
	
				preparedStatement = connect.prepareStatement("UPDATE documents SET considered = '1', date_of_considered = CURRENT_TIMESTAMP WHERE document_id='" + index + "'");
				
				preparedStatement.executeUpdate();
			} 
			catch (Exception exc) 
			{
				exc.printStackTrace();
			}
			finally 
		    {
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
    	message.setSubject(subject);

    	// Stworzenie czêœci wiadomosci z treœci¹
    	MimeBodyPart textPart = new MimeBodyPart();
    	textPart.setContent(content, "text/html; charset=ISO-8859-2");

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
    	
    	for(int i = 0; i < mails.length; i++)
    	{
    		message.addRecipient(Message.RecipientType.TO, new InternetAddress(mails[i]));
    	}

    	Transport transport = mailSession.getTransport();
    	transport.connect(host, port, from, password);

    	// Wys¹³nei maila
    	transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
    	transport.close();
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