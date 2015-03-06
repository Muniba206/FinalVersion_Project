package model;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JOptionPane;

import View.ServerGUI;

/*
 * The server that can be run both as a console application or a GUI
 */

public class Server {
	// Unique ID for each connection
	private static int uniqueId;
	// ArrayList to keep the list of the Client
	private ArrayList<ClientThread> al;
	// If in a GUI
	private ServerGUI sg;
	// Display time
	private SimpleDateFormat sdf;
	// The port number to listen for a connection
	private int port;
	// The boolean that will be turned off in order to stop the server
	private boolean keepGoing;
	

	/*
	 *  Server constructor that will receive the port to listen for a connection as a parameter
	 *  in console
	 */
	
	public Server(int port) {
		this(port, null);
	}
	
	public Server(int port, ServerGUI sg) {
		// GUI or not
		this.sg = sg;
		// Port
		this.port = port;
		// To display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
		al = new ArrayList<ClientThread>();
	}
	
	/*
	 *  To run as a console application just open a console window and if the port number 
	 *  is not specified, 1500 is used
	 */ 
	
	public static void main(String[] args) {
		// Start server on port 1500 unless a PortNumber is specified 
		int portNumber = 1500;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
				
		}
		
		// Creates a server object and starts it
		Server server = new Server(portNumber);
		server.start();
	}
	
	
	
	
}