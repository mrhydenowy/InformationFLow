package com.pnowicki.pkpc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.*;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

public class LogIn extends JFrame {
	private static final long serialVersionUID = 1L;

	private JButton okButton = new JButton("Ok");
	private JPasswordField passwordField = new JPasswordField();
	private JLabel passwordLabel = new JLabel("Has�o:");
	private JTextField userNameField = new JTextField();
	private JLabel userNameLabel = new JLabel("Nazwa u�ytkownika:");
	private String addressToLogicLayer = new String();
	private String[] departments;
	private String[] mails;
	private String host;
	private String port;
	private String userMail;
	private String mailPassword;

	public LogIn() {
		super("Logowanie");

		this.setBounds(300, 300, 600, 400);

		this.setDefaultCloseOperation(3);

		initComponents();
	}

	private void initComponents() {
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(280,
																		280,
																		280)
																.addComponent(
																		okButton))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(126,
																		126,
																		126)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING)
																				.addComponent(
																						userNameLabel)
																				.addComponent(
																						passwordLabel))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						passwordField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						111,
																						GroupLayout.PREFERRED_SIZE)
																				.addComponent(
																						userNameField,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						111,
																						GroupLayout.PREFERRED_SIZE))))
								.addContainerGap(210, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap(111, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(userNameLabel)
												.addComponent(
														userNameField,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														passwordField,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(passwordLabel))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(okButton).addGap(142, 142, 142)));

		okButtonListener();
		buttonsKeyListener();
	}

	private void buttonsKeyListener() {
		userNameField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					okButton.doClick();
				}
			}
		});

		passwordField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					okButton.doClick();
				}
			}
		});
	}

	private void okButtonListener() {
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (userNameField.getText().equals("sekretarka"))
					firstReadFile();
				else if (userNameField.getText().equals("dyrektor"))
					secondReadFile();

				String getName = userNameField.getText();
				String getPassword = String.valueOf(passwordField.getPassword());

				ClientConfig config = new DefaultClientConfig();
				Client client = Client.create(config);
				client = Client.create(config);
				client.addFilter(new LoggingFilter());
				WebResource webResource = client.resource(addressToLogicLayer)
						.path("login").path("login");

				FormDataMultiPart fdmp = new FormDataMultiPart();
				fdmp.bodyPart(new FormDataBodyPart("userName", getName));
				fdmp.bodyPart(new FormDataBodyPart("userPassword", getPassword));

				String response = webResource.type(
						MediaType.MULTIPART_FORM_DATA_TYPE).put(String.class,
						fdmp);

				WebResource webResourcePath = client
						.resource(addressToLogicLayer).path("login")
						.path("getFinalPath");

				String finalPath = webResourcePath.type(
						MediaType.MULTIPART_FORM_DATA_TYPE).get(String.class);

				if (response.equals("user"))
					new AddDocument(addressToLogicLayer).setVisible(true);
				else if (response.equals("admin"))
					new ViewDocuments(addressToLogicLayer, finalPath,
							departments, mails, host, port, userMail,
							mailPassword).setVisible(true);
			}
		});
	}

	private void firstReadFile() {
		try {
			FileReader fileReader = new FileReader("config.txt");
			BufferedReader bufferReader = new BufferedReader(fileReader);

			String content;

			while ((content = bufferReader.readLine()) != null) {
				if (content.equals("addressToLogicLayer:"))
					addressToLogicLayer = bufferReader.readLine();
			}
			fileReader.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	private void secondReadFile() {
		try {
			FileReader fileReader = new FileReader("config.txt");
			BufferedReader bufferReader = new BufferedReader(fileReader);

			String content;
			String stringDepartments = "";
			String departmentsWhile = "";
			String mailWhile = "";
			String stringMail = "";

			while ((content = bufferReader.readLine()) != null) {
				if (content.equals("addressToLogicLayer:"))
					addressToLogicLayer = bufferReader.readLine();
				else if (content.equals("departments:")) {
					while (!(departmentsWhile = bufferReader.readLine())
							.equals("departmentsEnd")) {
						stringDepartments += departmentsWhile + ",";
					}
				} else if (content.equals("departmentsmails:")) {
					while (!(mailWhile = bufferReader.readLine())
							.equals("departmentsmailsEnd")) {
						stringMail += mailWhile + ",";
					}
				} else if (content.equals("host:"))
					host = bufferReader.readLine();
				else if (content.equals("port:"))
					port = bufferReader.readLine();
				else if (content.equals("userMail:"))
					userMail = bufferReader.readLine();
				else if (content.equals("mailPassword:"))
					mailPassword = bufferReader.readLine();
			}
			fileReader.close();

			setDepartments(stringDepartments);
			setMails(stringMail);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	private void setDepartments(String stringDepartments) {
		int counter = 0;

		for (int i = 0; i < stringDepartments.length(); i++) {
			if (stringDepartments.charAt(i) == ',')
				counter++;
		}

		departments = new String[counter];
		for (int i = 0; i < departments.length; i++) {
			departments[i] = "";
		}

		counter = 0;
		for (int i = 0; i < stringDepartments.length(); i++) {
			if (stringDepartments.charAt(i) != ',')
				departments[counter] += stringDepartments.charAt(i);
			else if (stringDepartments.charAt(i) == ',')
				counter++;
		}
	}

	private void setMails(String stringMail) {
		int counter = 0;

		for (int i = 0; i < stringMail.length(); i++) {
			if (stringMail.charAt(i) == ',')
				counter++;
		}

		mails = new String[counter];
		for (int i = 0; i < mails.length; i++) {
			mails[i] = "";
		}

		counter = 0;
		for (int i = 0; i < stringMail.length(); i++) {
			if (stringMail.charAt(i) != ',')
				mails[counter] += stringMail.charAt(i);
			else if (stringMail.charAt(i) == ',')
				counter++;
		}
	}
}