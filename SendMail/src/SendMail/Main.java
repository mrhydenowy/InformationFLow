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

    // Adres email osby kt�ra wysy�a maila

    private static final String FROM = "mrhydenowy@gmail.com";

    // Has�o do konta osoby kt�ra wysy�a maila

    private static final String PASSWORD = "gotik123";

    // Adresy email osob do kt�rych wysy�amy mail
    
    private String[] tablica = {"mrhydenowy@gmail.com", "nowickipawel.pl@gmail.com", "angelikadun@gmail.com"};

    // Temat wiadomo�ci

    private static final String SUBJECT = "Hello World";

    // Tre�� wiadomo�ci

    private static final String CONTENT = "To m�j pierwszy mail wys�any za pomoc� JavaMailAPI. jo�������������������������";
    
	// �cie�ka do pliku

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

    	 

    	// Tworzenie wiadomo�ci

    	MimeMessage message = new MimeMessage(mailSession);

    	message.setSubject(SUBJECT);

    	 

    	// Stworzenie cz�ci wiadomosci z tre�ci�

    	MimeBodyPart textPart = new MimeBodyPart();

    	textPart.setContent(CONTENT, "text/html; charset=ISO-8859-2");

    	 

    	// Stworzenie cz�ci z za�acznikami

    	MimeBodyPart attachFilePart = new MimeBodyPart();

    	FileDataSource fds = new FileDataSource(PATH_FILE);

    	attachFilePart.setDataHandler(new DataHandler(fds));

    	attachFilePart.setFileName(fds.getName());

    	 

    	// Zestawienie obydwu cz�ci maila w jedn� wielocz�ciow�

    	Multipart mp = new MimeMultipart();
    	

    	mp.addBodyPart(textPart);

    	mp.addBodyPart(attachFilePart);

    	 

    	// Dodanie tre�ci maila

    	message.setContent(mp);
    	
    	for(int i = 0; i < tablica.length; i++)
    	{
    		message.addRecipient(Message.RecipientType.TO, new InternetAddress(tablica[i]));
    	}

    	 

    	Transport transport = mailSession.getTransport();

    	transport.connect(HOST, PORT, FROM, PASSWORD);

    	 

    	// Wys��nei maila

    	transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));

    	transport.close();

    }
}