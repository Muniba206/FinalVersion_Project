package model;

import java.io.*;

/*
 * This class defines the different type of messages that will be exchanged between the 
 * Clients and the Server.
 */

public class ChatMessage implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    // The different types of message sent by the Client

    // WHOISIN: Receiving the list of the users connected to the KEA_STUD LAN Chat Messenger

    // MESSAGE: An ordinary message

    // LOGOUT: Disconnect from the Server

    public static final int WHOISIN = 0;

	public static final int MESSAGE = 1;

	public static final int LOGOUT = 2;

    private int type;

    private String message;

     
    // constructor

    public ChatMessage(int type, String message) {
    	
        this.type = type;
        this.message = message;
        
    }

     

    // getters

    int getType() {

        return type;

    }

    String getMessage() {

        return message;

    }

}
