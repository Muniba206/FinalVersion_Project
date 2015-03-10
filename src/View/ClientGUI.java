package View;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;


/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// to hold the Username and later on the messages
	public static JTextField tf;
	// to hold the server address an the port number
	private JTextField tfServer, tfPort;
	// to Logout and get the list of the users
	private JButton login, logout, whoIsIn;
	// for the chat room
	private JTextArea ta;
	// if it is for connection
	private boolean connected;
	// the Client object, needed to be implemented by Lina 
//	private Client client;
	// the default port number
	private int defaultPort;
	private String defaultHost;

	// Constructor connection receiving a socket number
	
	public ClientGUI(String host, int port) {

		super("Kea_Stud_Chatting");
		defaultPort = port;
		defaultHost = host;
		
		// The NorthPanel with:
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		// the server name anmd the port number
		JPanel serverAndPort = new JPanel();
		// the two JTextField with default value for server address and port number
		tfServer = new JTextField(host);
		tfServer.setBounds(120, 0, 119, 28);
		tfPort = new JTextField("" + port);
		tfPort.setBounds(360, 0, 119, 28);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);
		serverAndPort.setLayout(null);

		JLabel label_1 = new JLabel("Server Address:  ");
		label_1.setBounds(0, 0, 119, 28);
		serverAndPort.add(label_1);
		serverAndPort.add(tfServer);
		JLabel label_2 = new JLabel("Port Number:  ");
		label_2.setBounds(240, 0, 119, 28);
		serverAndPort.add(label_2);
		serverAndPort.add(tfPort);
		JLabel label_3 = new JLabel("");
		label_3.setBounds(480, 0, 119, 28);
		serverAndPort.add(label_3);
		// adds the Server an port field to the GUI
		northPanel.add(serverAndPort);
		tf = new JTextField("Anonymous");
		tf.setBackground(Color.WHITE);
		northPanel.add(tf);
		getContentPane().add(northPanel, BorderLayout.NORTH);
		
				// the 3 buttons
				login = new JButton("Start Chating");
				northPanel.add(login);
				login.addActionListener(this);

		// The CenterPanel which is the chat room
		ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(null);
		JScrollPane scrollPane = new JScrollPane(ta);
		scrollPane.setBounds(0, 0, 600, 455);
		centerPanel.add(scrollPane);
		ta.setEditable(false);
		getContentPane().add(centerPanel, BorderLayout.CENTER);
		whoIsIn = new JButton("Who is in");
		whoIsIn.addActionListener(this);
		whoIsIn.setEnabled(false);		// you have to login before being able to Who is in

		JPanel southPanel = new JPanel();
		southPanel.add(whoIsIn);
		getContentPane().add(southPanel, BorderLayout.SOUTH);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false);		// you have to login before being able to logout
		southPanel.add(logout);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();

	}

	// called by the Client to append text in the TextArea 
	public void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	
	public void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		whoIsIn.setEnabled(false);
//		label.setText("Enter your username below");
		tf.setText("Anonymous");
		// reset port number and host name as a construction time
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		// let the user change them
		tfServer.setEditable(false);
		tfPort.setEditable(false);
		// don't react to a <CR> after the username
		tf.removeActionListener(this);
		connected = false;
	}
		
	/*
	* Button or JTextField clicked
	*/
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
		if(o == logout) {
	//		client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
			return;
		}
		// if it the who is in button
		if(o == whoIsIn) {
	//		client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));				
			return;
		}

		// ok it is coming from the JTextField
		if(connected) {
			// just have to send the message
	//		client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, tf.getText()));				
			tf.setText("");
			return;
		}
		

		if(o == login) {
			// ok it is a connection request
			String username = tf.getText().trim();
			// empty username ignore it
			if(username.length() == 0)
				return;
			// empty serverAddress ignore it
			String server = tfServer.getText().trim();
			if(server.length() == 0)
				return;
			// empty or invalid port numer, ignore it
			String portNumber = tfPort.getText().trim();
			if(portNumber.length() == 0)
				return;
			int port = 0;
			Date date= new Date();
			try {
				port = Integer.parseInt(portNumber);
				System.out.println("Start Chating: "+date.toString());
			}
			catch(Exception en) {
				return;   // nothing I can do if port number is not valid
			}

			// try creating a new Client with GUI
	//		client = new Client(server, port, username, this);
//			 test if we can start the Client
	//		if(!client.start()) 
				return;
			tf.setText("");
//			label.setText("Enter your message below");
			Date date2= new Date();
			System.out.println("Started: "+date2.toString());
			connected = true;
			System.out.println(date2.getTime()-date.getTime()+" Millisecounds");
			// disable login button
			login.setEnabled(false);
			// enable the 2 buttons
			logout.setEnabled(true);
			whoIsIn.setEnabled(true);
			// disable the Server and Port JTextField
			tfServer.setEditable(false);
			tfPort.setEditable(false);
			// Action listener for when the user enter a message
			tf.addActionListener(this);
		}

	}

	// to start the whole thing the server
	public static void main(String[] args) {
		new ClientGUI("localhost", 1500);
	}

}


