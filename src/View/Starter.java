package View;

/* Controller will be implemented by Lina named DBWrapper */

import View.LogIn;
import View.ServerGUI;
import Controller.DBWrapper; 


public class Starter{

     public static void main (String[] args){
    	 
    	ServerGUI ser= new ServerGUI(1500);
 		ser.setVisible(true);
    	 LogIn log= new LogIn();
    	 log.setVisible(true);
    	 
    	 //System.out.println("???");
     }

}
