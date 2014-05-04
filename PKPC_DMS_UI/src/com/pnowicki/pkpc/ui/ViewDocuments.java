package com.pnowicki.pkpc.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
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

public class ViewDocuments extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JComboBox<String> jComboBox1 = new JComboBox<String>();
	
    private String userDataBase = new String();
    private String passwordDataBase = new String();
    private String driverDataBase = new String();
    private String firstAddressDataBase = new String();
    private String secondAddressDataBase = new String();
    private String finalPath = new String();
    private String[] departments;
    private String[] mails;
    private String host;
    private String port;
    private String userMail;
    private String passwordMail;
	
	int[] intOstatni;
	
	public ViewDocuments(String driverDataBase, String firstAddressDataBase, String secondAddressDataBase, String userDataBase, String passwordDataBase, String finalPath, String[] departments,
						 String[] mails, String host, String port, String userMail, String passwordMail)
    {
        super("Pisma");
        
        this.setBounds(300, 300, 600, 400);
        
        this.setDefaultCloseOperation(2);
        
        this.driverDataBase = driverDataBase;
        this.firstAddressDataBase = firstAddressDataBase;
        this.secondAddressDataBase = secondAddressDataBase;
        this.userDataBase = userDataBase;
        this.passwordDataBase = passwordDataBase;
        this.finalPath = finalPath;
        this.departments = departments;
        this.mails = mails;
        this.host = host;
        this.port = port;
        this.userMail = userMail;
        this.passwordMail = passwordMail;
		
        initComponents();
    }
	
	private void initComponents() 
	{
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(465, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(329, Short.MAX_VALUE))
        );
        
        pack();
        
        getNamesDocuments();
        
        addDocumentButtonListener();
    }
	
	private void getNamesDocuments()
	{
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client = Client.create(config);
		client.addFilter(new LoggingFilter());
		
		WebResource webResource = client.resource(secondAddressDataBase).path("viewdocuments").path("addDocument");
		FormDataMultiPart fdmp = new FormDataMultiPart();
		fdmp.bodyPart(new FormDataBodyPart("driverDataBase", driverDataBase));
		fdmp.bodyPart(new FormDataBodyPart("firstAddressDataBase", firstAddressDataBase));
		fdmp.bodyPart(new FormDataBodyPart("userDataBase", userDataBase));
		fdmp.bodyPart(new FormDataBodyPart("passwordDataBase", passwordDataBase));
		
		//ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, fdmp);
		webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, fdmp);
		
		
		webResource = client.resource(secondAddressDataBase).path("viewdocuments").path("getFiles");
		
		String response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).get(String.class);
		
		//System.out.println(response);
		int counter = 1;
		
		for(int i = 0; i < response.length(); i++)
		{
			if(response.charAt(i) == ',')
				counter++;
		}
		
		String[] arrayStrings = new String[counter];
		
		for(int i = 0; i < arrayStrings.length; i++)
			arrayStrings[i] = "";
		
		int tmpTest = 0;
		
		for(int i = 0; i < response.length(); i++)
		{
			if(response.charAt(i) != ',')
				arrayStrings[tmpTest] += response.charAt(i);
			else if(response.charAt(i) == ',')
				tmpTest++;
		}
		
		webResource = client.resource(secondAddressDataBase).path("viewdocuments").path("getBooleans");
		response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).get(String.class);
		
		counter = 1;
		
		for(int i = 0; i < response.length(); i++)
		{
			if(response.charAt(i) == ',')
				counter++;
		}
		
		String[] arrayBooleans = new String[counter];
		
		for(int i = 0; i < arrayBooleans.length; i++)
			arrayBooleans[i] = "";
		
		tmpTest = 0;
		
		for(int i = 0; i < response.length(); i++)
		{
			if(response.charAt(i) != ',')
				arrayBooleans[tmpTest] += response.charAt(i);
			else if(response.charAt(i) == ',')
				tmpTest++;
		}
		
		webResource = client.resource(secondAddressDataBase).path("viewdocuments").path("getInts");
		response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).get(String.class);
		
		counter = 1;
		
		for(int i = 0; i < response.length(); i++)
		{
			if(response.charAt(i) == ',')
				counter++;
		}
		
		String[] arrayInts = new String[counter];
		
		for(int i = 0; i < arrayInts.length; i++)
			arrayInts[i] = "";
		
		tmpTest = 0;
		
		for(int i = 0; i < response.length(); i++)
		{
			if(response.charAt(i) != ',')
				arrayInts[tmpTest] += response.charAt(i);
			else if(response.charAt(i) == ',')
				tmpTest++;
		}
		
		intOstatni = new int[arrayInts.length];
		
		for(int i = 0; i < arrayInts.length; i++)
		{
			intOstatni[i] = Integer.parseInt(arrayInts[i]);
		}
		
		for(int i = 0; i < arrayStrings.length; i++)
		{
			jComboBox1.addItem(arrayStrings[i]);
		}

			ComboBoxRenderer renderer = new ComboBoxRenderer(jComboBox1);

	        renderer.setStrings(arrayStrings);
	        renderer.setBoolean(arrayBooleans);

	        jComboBox1.setRenderer(renderer);
	}
	
	private void addDocumentButtonListener()
    {
		jComboBox1.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String path = new String(finalPath);
				try 
        		{
					String stringSelectedIndex = jComboBox1.getSelectedItem().toString();
					
					int selectedIndex = jComboBox1.getSelectedIndex();

					String endOfString = new String("");
					
					String s = System.getProperty("file.separator");
					char firstLetter = s.charAt(0);
					
					for(int i = 0; i < stringSelectedIndex.length(); i++)
					{
						if(stringSelectedIndex.charAt(i) == firstLetter)
							endOfString = "";
						else if(stringSelectedIndex.charAt(i) != firstLetter)
							endOfString += stringSelectedIndex.charAt(i);
					}
					
					ClientConfig config = new DefaultClientConfig();
					Client client = Client.create(config);
					client = Client.create(config);
					client.addFilter(new LoggingFilter());
					
					WebResource webResource = client.resource(secondAddressDataBase).path("viewdocuments").path("openFile");
					FormDataMultiPart fdmp = new FormDataMultiPart();
					fdmp.bodyPart(new FormDataBodyPart("path", path + endOfString));
					
					//ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, fdmp);
					webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(ClientResponse.class, fdmp);
					
					new CheckDocument(driverDataBase, firstAddressDataBase, secondAddressDataBase, userDataBase, passwordDataBase, path + endOfString, intOstatni[selectedIndex], departments, mails,
							host, port, userMail, passwordMail).setVisible(true);
				} 
        		catch (Exception exc) 
        		{
        			exc.printStackTrace();
				}
			}
		});
    }
}

class ComboBoxRenderer extends JPanel implements ListCellRenderer
{
    private static final long serialVersionUID = -1L;
    private String[] strings;
    private String[] bools;

    JPanel textPanel;
    JLabel text;

    public ComboBoxRenderer(JComboBox combo) 
    {
        textPanel = new JPanel();
        textPanel.add(this);
        text = new JLabel();
        text.setOpaque(true);
        text.setFont(combo.getFont());
        textPanel.add(text);
    }


    public void setStrings(String[] str)
    {
        strings = str;
    }
    
    public void setBoolean(String[] bool)
    {
        bools = bool;
    }

    public String[] getStrings()
    {
        return strings;
    }
    
    public String[] getBooleans()
    {
        return bools;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
    {
        if (isSelected)
        {
            setBackground(list.getSelectionBackground());
        }
        else
        {
            setBackground(Color.WHITE);
        }

        text.setBackground(getBackground());

        text.setText(value.toString());
        if(index > -1)
        {
	        if (bools[index].equals("false"))
	        {
	            text.setForeground(Color.RED);
	        }
	        if (bools[index].equals("true"))
	        {
	            text.setForeground(Color.GREEN);
	        }
        }
        return text;
    }
}