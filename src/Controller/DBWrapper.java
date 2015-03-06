package Controller;
import java.sql.*;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.*;

import model.User;
import View.ClientGUI;
import View.ServerGUI;
import View.Register;


/**
 * Database wrapper klasse som indeholder alle metoder der fungerer som
 * forbindelse mellem applikationen og databasen
 */
public class DBWrapper implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Connection con;
	public static Statement stmt;
	public static ResultSet rs;
	public static String sqlString;
//	public static ServerGUI host;
	

	public static boolean connect()  {
	
			
			
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					con= DriverManager.getConnection("jdbc:mysql://localhost:3306/Kea Chat","root", "");
					System.out.println("worked!");

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
			return false;
		}
// log on method where we check if the username and password matches.
	public static boolean authenticate(String username, String enteredPass) {
		try {
			connect();
			
			System.out.println("JNDI connection  created in authenticate: " + con.toString());
			stmt = con.createStatement();
			
//			sqlString = "SELECT * FROM user WHERE username='"+ username + 
//					"AND 'passHash' = "+ enteredPass + "'";
			String sqlString = "select username, passHash from user where username ='"+username+"' and passHash= '"+enteredPass+"'";
		
//			sqlString = "SELECT  user WHERE username='"+ user.getName() + "AND passHash =" + user.getPassword() + "'";
			rs = stmt.executeQuery(sqlString);	
//			host.setVisible(true);
			
			
			
			
			while (rs.next()) {
				System.out.println("User found cikyl:" + sqlString);	
				 con.close();
				return true;
			}	
			    con.close();
				return false;	
			}			
	    	catch (java.sql.SQLException e) {
			JOptionPane.showMessageDialog(null,"Wrong username or password!");
			return false;
		   }
	    
	}
	
	

	public static void createTables() {
         
		try {
			connect();
			stmt = con.createStatement();

			
			sqlString = "CREATE TABLE IF NOT EXISTS user "
					+ "(username varchar(30) NOT NULL,"
					+ "passHash varchar(32) not null, "		
					+ "email varchar(30) NOT NULL,"	
				    + "PRIMARY KEY(uid),"
				    + "uid INT NOT NULL auto_increment,"
				    + "unique(username) )";

			

			stmt.executeUpdate(sqlString);
			
			
		
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.toString());

		}// end catch
	}



	/** making new raw in user tabellen  with the given attributer */
	
	
	public void saveUser(String name, String mail, String password)
		 {
		try {
			connect();
			
			System.out.println("JNDI connection  created in saveUser: " + con.toString());
			stmt = con.createStatement();
			sqlString = "INSERT INTO user(username,passHash, email) VALUES('"
					+ name
					+ "', '"
					+ password
					+ "', '"
 					+ "' )";
			
			stmt.executeUpdate(sqlString);
			 con.close();
			 JOptionPane.showMessageDialog(null, "Welcome to KEA Chat "+ name);
			 ClientGUI cGui= new ClientGUI("localhost", 1500);
				cGui.setVisible(true);
				cGui.tf.setText(Register.textName.getText());
			 
//			
			  
		}

		catch (java.sql.SQLException e) {
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(null, e.toString());

			JOptionPane.showMessageDialog(null, " This username is not available! Try again!");	
			
			
		}// end catch
	}

	
	
	
	

	
	
	
	
	
//	public static boolean authenticate(String enteredPass, String username) {
//		try {
//			connect();
//			System.out.println("JNDI connection  created in authenticate: " + con.toString());
//			stmt = con.createStatement();
//			
//			sqlString = "SELECT * FROM users WHERE username='"+ username + "' AND passHash=MD5('" + enteredPass + "')";
//		
//			rs = stmt.executeQuery(sqlString);		
//			while (rs.next()) {
//				System.out.println("User found cikyl:" + sqlString);	
//				 con.close();
//				return true;
//			}	
//			    con.close();
//				return false;	
//			}			
//	    	catch (java.sql.SQLException e) {
//			System.out.println("Wrong username or password!");
//			return false;
//		   }
//	    
//	}
	
	
	
	
	

	

	
	

}