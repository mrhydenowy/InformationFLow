package SendMail;

import javax.mail.*;

import javax.mail.internet.*;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import java.util.Properties;


public class Main 
{
    private static final String HOST = "smtp.gmail.com";

    private static final int PORT = 465;

    // Adres email osby która wysy³a maila

    private static final String FROM = "mrhydenowy@gmail.com";

    // Has³o do konta osoby która wysy³a maila

    private static final String PASSWORD = "gotik123";

    // Adresy email osob do których wysy³amy mail
    
    private String[] tablica = {"mrhydenowy@gmail.com", "nowickipawel.pl@gmail.com", "angelikadun@gmail.com"};

    // Temat wiadomoœci

    private static final String SUBJECT = "Hello World";

    // Treœæ wiadomoœci

    private static final String CONTENT = "To mój pierwszy mail wys³any za pomoc¹ JavaMailAPI. jo³³³³³³³³³³³³³³³³³³³³³³³³³";
    
	// Œcie¿ka do pliku

	private static final String PATH_FILE = "c:/mus_keep1.mp3";
	
	
	public static void main(String[] args) 
	{
		try 
        {
        	new Main().send();
       	} 
        catch (MessagingException e) 
        {
        	e.printStackTrace();
        }
	}
	
    void send() throws MessagingException 
    {
    	Properties props = new Properties();

    	props.put("mail.transport.protocol", "smtps");

    	props.put("mail.smtps.auth", "true");

    	 

    	// Pobranie sesji

    	Session mailSession = Session.getDefaultInstance(props, null);

    	 

    	// Tworzenie wiadomoœci

    	MimeMessage message = new MimeMessage(mailSession);

    	message.setSubject(SUBJECT);

    	 

    	// Stworzenie czêœci wiadomosci z treœci¹

    	MimeBodyPart textPart = new MimeBodyPart();

    	textPart.setContent(CONTENT, "text/html; charset=ISO-8859-2");

    	 

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
    	
    	for(int i = 0; i < tablica.length; i++)
    	{
    		message.addRecipient(Message.RecipientType.TO, new InternetAddress(tablica[i]));
    	}

    	 

    	Transport transport = mailSession.getTransport();

    	transport.connect(HOST, PORT, FROM, PASSWORD);

    	 

    	// Wys¹³nei maila

    	transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

    	transport.close();

    }
}