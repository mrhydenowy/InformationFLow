package com.pnowicki.pkpc.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

public class ViewDocuments extends JFrame {
	private static final long serialVersionUID = 1L;

	private JComboBox<String> jComboBox1 = new JComboBox<String>();

	private String addressToLogicLayer = new String();
	private String finalPath = new String();
	private String[] departments;
	private String[] mails;
	private String host;
	private String port;
	private String userMail;
	private String mailPassword;

	int[] theLastInt;

	public ViewDocuments(String addressToLogicLayer, String finalPath,
			String[] departments, String[] mails, String host, String port,
			String userMail, String mailPassword) throws JSONException {
		super("Dokumenty");

		this.setBounds(300, 300, 600, 400);

		this.setDefaultCloseOperation(2);

		this.addressToLogicLayer = addressToLogicLayer;
		this.finalPath = finalPath;
		this.departments = departments;
		this.mails = mails;
		this.host = host;
		this.port = port;
		this.userMail = userMail;
		this.mailPassword = mailPassword;

		initComponents();
	}

	private void initComponents() throws JSONException {
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(465, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(329, Short.MAX_VALUE)));

		pack();

		getNamesDocuments();

		addDocumentButtonListener();
	}

	private void getNamesDocuments() throws JSONException {
		//wybór klasy i metody w warstwie logiki
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client = Client.create(config);
		client.addFilter(new LoggingFilter());
		WebResource webResource = client.resource(addressToLogicLayer)
				.path("viewdocuments").path("getFiles");
		
		//wywo³anie metody z warstwy logiki. Tutaj nie tworzymy zadnego zasobu, bo wywo³ujemy metodê get
		JSONArray responseFileNames = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.get(JSONArray.class);

		//wybór klasy i metody w warstwie logiki oraz wywo³anie metody z warstwy logiki. Tutaj nie tworzymy zadnego zasobu, bo wywo³ujemy metodê get
		webResource = client.resource(addressToLogicLayer)
				.path("viewdocuments").path("getBooleans");
		JSONArray responseBooleans = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).get(
				JSONArray.class);

		//wybór klasy i metody w warstwie logiki oraz wywo³anie metody z warstwy logiki. Tutaj nie tworzymy zadnego zasobu, bo wywo³ujemy metodê get
		webResource = client.resource(addressToLogicLayer)
				.path("viewdocuments").path("getInts");
		JSONArray responseDocumentId = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).get(
				JSONArray.class);

		//elementy z powy¿szej listy przyisujemy do listy globalnej
		theLastInt = new int[responseDocumentId.length()];

		for (int i = 0; i < responseDocumentId.length(); i++) {
			theLastInt[i] = responseDocumentId.getInt(i);
		}

		for (int i = 0; i < responseFileNames.length(); i++) {
			jComboBox1.addItem(responseFileNames.getString(i));
		}

		ComboBoxRenderer renderer = new ComboBoxRenderer(jComboBox1);

		//wyswietlamy liste na podstawie nazw plikow i ich wartosci true i false
		renderer.setStrings(responseFileNames);
		renderer.setBoolean(responseBooleans);

		jComboBox1.setRenderer(renderer);
	}

	private void addDocumentButtonListener() {
		jComboBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = new String(finalPath);
				try {
					String stringSelectedIndex = jComboBox1.getSelectedItem()
							.toString();

					int selectedIndex = jComboBox1.getSelectedIndex();

					String fileName = new String("");

					//file.separator to nic innego jak /. Uzywam file.separator a nie /, bo jezeli aplikacja zostanie uruchomiona w innym systemie operacyjnym, wtedy file.separator zmieni siê automatycznie
					String separator = System.getProperty("file.separator");
					char firstLetter = separator.charAt(0);

					//zapisuje do zmiennej nazwe pliku
					for (int i = 0; i < stringSelectedIndex.length(); i++) {
						if (stringSelectedIndex.charAt(i) == firstLetter)
							fileName = "";
						else if (stringSelectedIndex.charAt(i) != firstLetter)
							fileName += stringSelectedIndex.charAt(i);
					}

					//wybór klasy i metody w warstwie logiki
					ClientConfig config = new DefaultClientConfig();
					Client client = Client.create(config);
					client = Client.create(config);
					client.addFilter(new LoggingFilter());

					//tworzenie zasobu, który bêdzie wys³any do metody
					WebResource webResource = client
							.resource(addressToLogicLayer)
							.path("viewdocuments").path("openFile");
					FormDataMultiPart fdmp = new FormDataMultiPart();
					fdmp.bodyPart(new FormDataBodyPart("path", path
							+ fileName));
					
					//wywo³anie metody z warstwy logiki
					webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).put(
							ClientResponse.class, fdmp);

					new CheckDocument(addressToLogicLayer, path + fileName,
							theLastInt[selectedIndex], departments, mails,
							host, port, userMail, mailPassword)
							.setVisible(true);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		});
	}
}

class ComboBoxRenderer extends JPanel implements ListCellRenderer {
	private static final long serialVersionUID = -1L;
	private JSONArray strings;
	private JSONArray bools;

	JPanel textPanel;
	JLabel text;

	public ComboBoxRenderer(JComboBox combo) {
		textPanel = new JPanel();
		textPanel.add(this);
		text = new JLabel();
		text.setOpaque(true);
		text.setFont(combo.getFont());
		textPanel.add(text);
	}

	public void setStrings(JSONArray str) {
		strings = str;
	}

	public void setBoolean(JSONArray bool) {
		bools = bool;
	}

	public JSONArray getStrings() {
		return strings;
	}

	public JSONArray getBooleans() {
		return bools;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
		} else {
			setBackground(Color.WHITE);
		}

		text.setBackground(getBackground());

		text.setText(value.toString());
		if (index > -1) {
			try {
				if (bools.getString(index).equals("false")) {
					text.setForeground(Color.RED);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (bools.getString(index).equals("true")) {
					text.setForeground(Color.GREEN);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return text;
	}
}