package model;


import java.net.*;
import java.io.*;
import java.util.*;

import View.ClientGUI;

/*
 * The Client can be run both as a console or as a GUI
 */

public class Client  {

	// for I/O
	private ObjectInputStream sInput;		// To read from the socket
	private ObjectOutputStream sOutput;		// To write on the socket
	private Socket socket;

	// GUI or not
	private ClientGUI cg;
	
	// Server, Port and Username
	private String server, username;
	private int port;

	/*
	 *  Constructor called by console mode
	 *  server: the server address
	 *  port: the port number
	 *  username: the username
	 */
	
	Client(String server, int port, String username) {
		// which calls the common constructor with the GUI set to null
		this(server, port, username, null);
	}

	/*
	 * Constructor call when used from a GUI
	 * in console mode the ClienGUI parameter is null
	 */
	
	public Client(String server, int port, String username, ClientGUI cg) {
		this.server = server;
		this.port = port;
		this.username = username;
		// Save if we are in GUI mode or not
		this.cg = cg;
	}
	
	/*
	 *  To run as a console application just open a console window and if the port number 
	 *  is not specified, 1500 is used.
	 *  
	 *  If the serverAddress is not specified, "localHost" is used
	 *  
	 *  If the username is not specified, "Anonymous" is used
	 * 
	 *  In console mode, if an error occurs the program simply stops when a GUI id used, 
	 *  the GUI is informed of the disconnection
	 */
	
	public static void main(String[] args) {
		// Default values
		int portNumber = 1500;
		String serverAddress = "localhost";
		String userName = "Anonymous";

		// Depending of the number of arguments provided we fall through
		switch(args.length) {
			// > javac Client username portNumber serverAddr
			case 3:
				serverAddress = args[2];
			// > javac Client username portNumber
			case 2:
				try {
					portNumber = Integer.parseInt(args[1]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
					return;
				}
			// > javac Client username
			case 1: 
				userName = args[0];
			// > java Client
			case 0:
				break;
			// Invalid number of arguments
			default:
				System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
			return;
		}
		// Creates the Client object
		Client client = new Client(serverAddress, portNumber, userName);
		// test if we can start the connection to the Server,
		// if it failed nothing we can do
		if(!client.start())
			return;
		
		// Waiting for messages from user
		Scanner scan = new Scanner(System.in);
		//Loop forever for message from the user
		while(true) {
			System.out.print("> ");
			// Read message from user
			String msg = scan.nextLine();
			// Logout if message is LOGOUT
			if(msg.equalsIgnoreCase("LOGOUT")) {
				client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
				// Break to do the disconnect
				break;
			}
			// Message, WhoIsIn
			else if(msg.equalsIgnoreCase("WHOISIN")) {
				client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));				
			}
			else {				// Default to ordinary message
				client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, msg));
			}
		}
		// Done, disconnect
		client.disconnect();	
	}	
	
}