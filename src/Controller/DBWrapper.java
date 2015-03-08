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
 * Database wrapper class contains all the metods to connect, etabling connection, creat tables and passing data in and out from the database
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
					return true;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
			return false;
		}
	
	public static boolean  connect2()	
	{
		try {
			String databaseName = "NewDatabase";
		

	        String url = "jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull";
	        Connection connection = DriverManager.getConnection(url,"root", "");
	
	        String sql = "CREATE DATABASE  IF NOT EXISTS " + databaseName;
	
	        Statement statement = connection.createStatement();
	        statement.executeUpdate(sql);
	        statement.close();
	        JOptionPane.showMessageDialog(null, databaseName + " Database has been created successfully", "System Message", JOptionPane.INFORMATION_MESSAGE);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	}
		return false;
}
//		Creating the Database schema 
//	public static boolean connect2()  {
//	
//	try {
//        String databaseName = "Chatting";
//        String userName = "root";
//        String password = "";
//
//        String url = "jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull";
//        Connection connection = DriverManager.getConnection(url,userName, password);
//
//        String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
//
//        Statement statement = connection.createStatement();
//        statement.executeUpdate(sql);
//        statement.close();
//        JOptionPane.showMessageDialog(null, databaseName + " Database has been created successfully", "System Message", JOptionPane.INFORMATION_MESSAGE);
//
//    } catch (Exception e) 
//	{
//        e.printStackTrace();
//	}
//	return false;
//}
//	
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
				System.out.println("User found yeah :)" + sqlString);	
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
	
	
// Creating databases tables
	public static void createTables() {
         
		try {
			connect();
			stmt = con.createStatement();

			
			sqlString = "CREATE TABLE  IF NOT EXISTS user "
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
	
//	this method is responsible for saving new user
	public boolean saveUser(String name, String mail, String password)
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
			 
		return true;
			  
		}

		catch (java.sql.SQLException e) {
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(null, e.toString());

			JOptionPane.showMessageDialog(null, " This username is not available! Try again!");	
			
			
		}// end catch
		return false;
	}

	
	
	
	

	
	
	
	
	

	
	
	
	

	

	
	

}