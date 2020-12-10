package backend;

import java.security.NoSuchAlgorithmException;

import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.math.BigInteger;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import tables.Customer;

public class DatabaseSecurity {
	
	private static SecureRandom random = new SecureRandom();
	private static Database coffeeShop = new Database();

	
	//Prompts the user to input a username and password, and creates an account for that user.
	 public void newUser(String uname, String pword) throws SQLException {

		 Connection conn = coffeeShop.getConnection();
		 try {
			 	String salt = getSalt();
			 	byte[] hash = hash(pword, salt);
				conn.setAutoCommit(false);
				String addUser = ("{call newUser (?,?,?)}");
				PreparedStatement insertUser = conn.prepareStatement(addUser);
				insertUser.setString(1, uname);
				insertUser.setString(2, salt);
				insertUser.setBytes(3, hash);
				insertUser.executeUpdate();
				conn.commit();
				System.out.println("User added succesfully");
				insertUser.close();
			}
			catch(SQLException e){
				conn.rollback();
			}
	 }
	
	
	//Prompts the user to input their login info, returns true if they are a valid user, false otherwise
	public Customer login() throws SQLException {
	Scanner reader = new Scanner(System.in);  
	System.out.println("Enter a username ");
	String uname = reader.nextLine();
	System.out.println("Enter a password ");
	String pword = reader.nextLine();
	reader.close();
		 
	Connection conn = coffeeShop.getConnection();
	Customer c = null;
	try {
		String salt = "";
		byte[] hash = null;
		conn.setAutoCommit(false);
		String getUser = "SELECT salt, hash FROM USERPASS WHERE username = \'" + uname +"\'";
		PreparedStatement getUserPass = conn.prepareStatement(getUser);
		//getUserPass.executeUpdate();
		ResultSet rs = getUserPass.executeQuery();
		while(rs.next()) {
			 salt = rs.getString(1);
			 hash = rs.getBytes(2);
		}
		byte[] compareHash = hash(pword, salt);
		if (Arrays.equals(hash, compareHash)) {
			System.out.println("Login as " + uname + " successful!");
			c = coffeeShop.getCustomer(uname);
		}
		getUserPass.close();
		}
		catch(SQLException e){
			System.out.println("Login failed!");
			conn.rollback();
		}
		return c;
	}
		
	
	//Helper Functions below:
	//getConnection() - obtains a connection
	//getSalt() - creates a randomly generated string 
	//hash() - takes a password and a salt as input and then computes their hash
	
	
	//Creates a randomly generated String
	public String getSalt(){
		return new BigInteger(140, random).toString(32);
	}
	
	//Takes a password and a salt a performs a one way hashing on them, returning an array of bytes.
	public byte[] hash(String password, String salt){
		try{
			SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
	        
			/*When defining the keyspec, in addition to passing in the password and salt, we also pass in
			a number of iterations (1024) and a key size (256). The number of iterations, 1024, is the
			number of times we perform our hashing function on the input. Normally, you could increase security
			further by using a different number of iterations for each user (in the same way you use a different
			salt for each user) and storing that number of iterations. Here, we just use a constant number of
			iterations. The key size is the number of bits we want in the output hash*/ 
			PBEKeySpec spec = new PBEKeySpec( password.toCharArray(), salt.getBytes(), 1024, 256 );

			SecretKey key = skf.generateSecret( spec );
	        byte[] hash = key.getEncoded( );
	        return hash;
        }catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
	}
}
