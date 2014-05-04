package com.pnowicki.pkpc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;


public class AddDocument extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private File file;
    private JFileChooser chooserFiles = new JFileChooser();
    private JButton addDocumentButton = new JButton("Dodaj pismo");
    private JButton chooseFileButton = new JButton("Wybierz plik");
    private JLabel chooseFileLabel = new JLabel();
    private JTextField nameDocumentField = new JTextField();
    private JLabel nameDocumentLabel = new JLabel("Tytu³ pisma:");
    private JTextField signatureField = new JTextField();
    private JLabel signatureLabel = new JLabel("Sygnatura:");
    
    private String userDataBase = new String();
    private String passwordDataBase = new String();
    private String driverDataBase = new String();
    private String firstAddressDataBase = new String();
    private String secondAddressDataBase = new String();
    private String finalPath = new String();
	
	public AddDocument(String driverDataBase, String firstAddressDataBase, String secondAddressDataBase, String userDataBase, String passwordDataBase, String finalPath) 
    {
        super("Dodawanie pism");
        
        this.setBounds(300, 300, 800, 250);
        
        this.setDefaultCloseOperation(2);
        
        this.driverDataBase = driverDataBase;
        this.firstAddressDataBase = firstAddressDataBase;
        this.secondAddressDataBase = secondAddressDataBase;
        this.userDataBase = userDataBase;
        this.passwordDataBase = passwordDataBase;
        this.finalPath = finalPath;
		
        initComponents();
    }

    private void initComponents() 
    {
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chooseFileButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chooseFileLabel))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(signatureLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(signatureField))
                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(nameDocumentLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(nameDocumentField, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(179, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addDocumentButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(chooseFileButton)
                    .addComponent(chooseFileLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameDocumentLabel)
                    .addComponent(nameDocumentField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(signatureLabel)
                    .addComponent(signatureField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(addDocumentButton)
                .addContainerGap())
        );
        
        chooserFilesListener();
        addDocumentButtonListener();
    }
    
    private void addDocumentButtonListener()
    {
    	addDocumentButton.addActionListener(new ActionListener()
    	{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String getNameDocumentField = nameDocumentField.getText();
				String getSignatureField = signatureField.getText();
				
				String filePath = file.getPath();
				
				String endFile = new String("");
				
				//String s = System.getProperty("file.separator");
				//char firstLetter = s.charAt(0);
				
				for(int i = 0; i < filePath.length(); i++)
				{
					if(filePath.charAt(i) == '.')
						endFile = "";
					else if(filePath.charAt(i) != '.')
						endFile += filePath.charAt(i);
				}
				
				//System.out.println(endOfString);

				ClientConfig config = new DefaultClientConfig();
				Client client = Client.create(config);
				client = Client.create(config);
				client.addFilter(new LoggingFilter());
				WebResource webResource = client.resource(secondAddressDataBase).path("documents");
				FormDataMultiPart fdmp = new FormDataMultiPart();
				File fileTmp = new File(filePath);
				System.out.println(file.getName());
				fdmp.bodyPart(new FileDataBodyPart("file", fileTmp, MediaType.APPLICATION_OCTET_STREAM_TYPE));
				fdmp.bodyPart(new FormDataBodyPart("name", getNameDocumentField));
				fdmp.bodyPart(new FormDataBodyPart("signature", getSignatureField));
				fdmp.bodyPart(new FormDataBodyPart("filePath", filePath));
				fdmp.bodyPart(new FormDataBodyPart("endFile", endFile));
				fdmp.bodyPart(new FormDataBodyPart("driverDataBase", driverDataBase));
				fdmp.bodyPart(new FormDataBodyPart("firstAddressDataBase", firstAddressDataBase));
				fdmp.bodyPart(new FormDataBodyPart("userDataBase", userDataBase));
				fdmp.bodyPart(new FormDataBodyPart("passwordDataBase", passwordDataBase));
				fdmp.bodyPart(new FormDataBodyPart("finalPath", finalPath));

				//ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, fdmp);
				webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, fdmp);
				//String string = response.getEntity(String.class);
			}
    	});
    }
    
    private void chooserFilesListener()
    {
    	chooserFiles.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
    	//chooserFiles.setMultiSelectionEnabled(true);
    	
    	chooseFileButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                //wywolujemy showOpenDialog bo bedziemy chcieli otworzyc plik. w nawiasie dajemy gdzie ma sie wyswietlic. RootPane wyswietli sie nam wzgledem ramki
                //przypisujemy zmienna int do tej funkcji i sprawdzamy jaka liczbe zwroci ta funkcji gdy nacisniemy Open albo Cancel
                int tmp = chooserFiles.showOpenDialog(rootPane);
                if(tmp == 0)//jezeli wybierze sie Open
                {
                	file = chooserFiles.getSelectedFile();
                	chooseFileLabel.setText(file.getPath());
                }
            }
        });
    }
}