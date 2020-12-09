package backend;

import java.sql.*;
import tables.*;

public class Database {
	
	public Connection getConnection() {
		try {
			String url = "jdbc:oracle:thin:@198.168.52.73:1522/pdborad12c.dawsoncollege.qc.ca"; //local URL
			Connection conn = DriverManager.getConnection(url, "A1542960", "SQLpassword");
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// returns null if connection wasn't made
		return null;
	}

	public String getCustIDByEmail(String email) {
		//for now
		return "test";
	}
	
	/*public String getSalt() {
		
	}
	
	public byte[] hash(String password, String salt) {
		
	}*/
	
	public Customer getCustomer(String username) {
		
	}
	
}
