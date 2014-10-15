package com.pnowicki.pkpc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;


public class CheckDocument  extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JLabel contentLabel = new JLabel("Treœæ wiadomoœci:");
    private JTextArea contentTextArea = new JTextArea(5, 20);
    private JScrollPane contentScrollPane = new JScrollPane(contentTextArea);
    private JLabel labelList = new JLabel("Aby wybraæ kilka dzia³ów, wystarczy przytrzymaæ klawisz \"Ctrl\"");
    private JLabel subjectLabel = new JLabel("Temat wiadomoœci:");
    private JTextField subjectTextField = new JTextField();
	
	private JCheckBox documentCheckBox = new JCheckBox("Zatwierdzenie dokumentu");
    private JButton okButton = new JButton("Ok");

    private JList<String> departmentsList;
        
    private String[] departmentsEmails;
    
    private String dir;
    private int index;
    
    private String addressToLogicLayer = new String();
    private String[] departments;
    //private String[] mails;
    
    private String host;
    private String port;
    private String userMail;
    private String passwordMail;
	
	public CheckDocument(String addressToLogicLayer, String file, int index, String[] departments, String[] mails, String host, String port, String userMail, String passwordMail)
	{
		this.setBounds(300, 300, 600, 400);
	    
	    this.setDefaultCloseOperation(2);
	    
	    this.dir = file;
	    this.index = index;
	    this.departments = departments;
	    this.departmentsEmails = mails;
	    
        this.addressToLogicLayer = addressToLogicLayer;
	    departmentsList = new JList<String>(departments);
	    
        this.host = host;
        this.port = port;
        this.userMail = userMail;
        this.passwordMail = passwordMail;
		
	    initComponents();
	}
 
    private void initComponents()
    {
    	contentTextArea.setLineWrap(true);
    	
    	departmentsList.setModel(new AbstractListModel() 
        {
            //String[] strings = { "Dzia³ marketingu", "Dzia³ logistyki", "Dzia³ informatyków"};
            public int getSize() { return departments.length; }
            public Object getElementAt(int i) { return departments[i]; }
        });
    	
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(subjectLabel)
                    .addComponent(contentLabel)
                    .addComponent(contentScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(subjectTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(documentCheckBox)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(okButton)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(departmentsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(156, Short.MAX_VALUE)
                .addComponent(labelList)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(labelList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(departmentsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(documentCheckBox))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(subjectLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subjectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(contentLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contentScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
        
        okButtonAcctionListener();
    }
    
    private void okButtonAcctionListener()
    {
    	okButton.addActionListener(new ActionListener() 
    	{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(departmentsList.isSelectionEmpty())
				{
					JOptionPane.showMessageDialog(rootPane, "Nie zaznaczono ¿adnego dzia³u");
				}
				else
				{
					int[] intSelectedDepartments = departmentsList.getSelectedIndices();
					String[] stringSelectedDepartments = new String[intSelectedDepartments.length];
					int intTmp = 0;
	
					String stringArrayStrings = new String("");
					
					for(int q = 0; q < intSelectedDepartments.length; q++)
					{
						for(int i = 0; i < departmentsEmails.length; i++)
						{
							if(intSelectedDepartments[q] == i)
							{
								stringSelectedDepartments[intTmp] = departmentsEmails[i];
								intTmp++;
							}
						}
					}
					
					for(int i = 0; i < stringSelectedDepartments.length; i++)
					{
						stringArrayStrings += stringSelectedDepartments[i] + ",";
					}

					boolean bool = documentCheckBox.isSelected();
					
					ClientConfig config = new DefaultClientConfig();
					Client client = Client.create(config);
					client = Client.create(config);
					client.addFilter(new LoggingFilter());
					WebResource webResource = client.resource(addressToLogicLayer).path("sendmail");
					FormDataMultiPart fdmp = new FormDataMultiPart();
					fdmp.bodyPart(new FormDataBodyPart("subject", subjectTextField.getText()));
					fdmp.bodyPart(new FormDataBodyPart("content", contentTextArea.getText()));
					fdmp.bodyPart(new FormDataBodyPart("mails", stringArrayStrings));
					fdmp.bodyPart(new FormDataBodyPart("file", dir));
					fdmp.bodyPart(new FormDataBodyPart("index", "" + index));
					fdmp.bodyPart(new FormDataBodyPart("bool", "" + bool));
					fdmp.bodyPart(new FormDataBodyPart("host", host));
					fdmp.bodyPart(new FormDataBodyPart("port", port));
					fdmp.bodyPart(new FormDataBodyPart("userMail", userMail));
					fdmp.bodyPart(new FormDataBodyPart("passwordMail", passwordMail));
	
	
					ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, fdmp);
				}
			}
		});
    }
}
