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
	
	public void start() {
		keepGoing = true;
		// Create socket server and wait for connection requests //
		try 
		{
			// The socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);

			// Infinite loop to wait for connections
			while(keepGoing) 
			{
				// Format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");
				
				Socket socket = serverSocket.accept();  	// Accept connection
				// if asked to stop
				if(!keepGoing)
					break;
				ClientThread t = new ClientThread(socket);  // Make a thread of it
				al.add(t);									// Save it in the ArrayList
				t.start();
			}
			// Asked to stop
			try {
				serverSocket.close();
				for(int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
					tc.sInput.close();
					tc.sOutput.close();
					tc.socket.close();
					}
					catch(IOException ioE) {
						// not much can be done
					}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// Something went wrong
		catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}	
	
    /*
     * For the GUI to stop the server
     */
	
	public void stop() {
		keepGoing = false;
		// Connect to myself as Client to exit statement 
		// Socket socket = serverSocket.accept();
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			// nothing can be done
		}
	}
	
	/*
	 * Display an event (not a message) to the console or the GUI
	 */
	
	private void display(String msg) {
		String time = sdf.format(new Date()) + " " + msg;
		if(sg == null)
			System.out.println(time);
		else
			sg.appendEvent(time + "\n");
	}
	
	/*
	 *  To broadcast a message to all Clients
	 */
	
	private synchronized void broadcast(String message) {
		// Add HH:mm:ss and \n to the message
		String time = sdf.format(new Date());
		String messageLf = time + " " + message + "\n";
		// Display message on console or GUI
		if(sg == null)
			System.out.print(messageLf);
		else
			sg.appendRoom(messageLf);     // Append in the room window
		
		// We loop in reverse order, in case we would have to remove a Client
		// because of disconnection
		for(int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);
			// Try to write to the Client, if it fails remove it from the list
			if(!ct.writeMsg(messageLf)) {
				al.remove(i);
				display("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}

	// For a client who logged off using the LOGOUT message
	synchronized void remove(int id) {
		// Scan the array list until we find the id
		for(int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);
			// Found
			if(ct.id == id) {
				al.remove(i);
				return;
			}
		}
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
	
	/** One instance of this thread will run for each client */
	class ClientThread extends Thread {
		// The socket in order to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		// Unique id (easier for disconnection)
		int id;
		// The Username of the Client
		String username;
		// The only type of message which will be received
		ChatMessage cm;
		// Date of connection
		String date;

		// Constructor
		ClientThread(Socket socket) {
			// A unique id
			id = ++uniqueId;
			this.socket = socket;
			// Creating both Data Streams //
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				// Create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				// Read the username
				username = (String) sInput.readObject();
				display(username + " just connected.");
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			// Have to catch ClassNotFoundException
			// but I read a String, I am sure it will work
			catch (ClassNotFoundException e) {
			}
            date = new Date().toString() + "\n";
		}

		// What will run forever
		public void run() {
			// To loop until LOGOUT
			boolean keepGoing = true;
			while(keepGoing) {
				// Read a String (which is an object)
				try {
					cm = (ChatMessage) sInput.readObject();
				}
				catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				// The message part of the ChatMessage
				String message = cm.getMessage();

				// Switch on the type of message received
				switch(cm.getType()) {

				case ChatMessage.MESSAGE:
					broadcast(username + ": " + message);
					break;
				case ChatMessage.LOGOUT:
					display(username + " disconnected with a LOGOUT message.");
					keepGoing = false;
					break;
				case ChatMessage.WHOISIN:
					writeMsg("List of the users connected at " + sdf.format(new Date()) + "\n");
					// Scan all the users connected
					for(int i = 0; i < al.size(); ++i) {
						ClientThread ct = al.get(i);
						writeMsg((i+1) + ") " + ct.username + " since " + ct.date);
					}
					break;
				}
			}
			// Remove myself from the arrayList which contains the list of the
			// connected Clients
			remove(id);
			close();
		}
		
		// Try to close everything
		private void close() {
			// Try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		/*
		 * Write a String to the Client output stream
		 */
		
		private boolean writeMsg(String msg) {
			// If Client is still connected, send a message to it
			if(!socket.isConnected()) {
				close();
				return false;
			}
			// Write the message to the stream
			try {
				sOutput.writeObject(msg);
			}
			// If an error occurs, do not abort just inform the user
			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}	
}