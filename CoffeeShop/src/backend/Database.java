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
	
	/*public Customer getCustomer(String username) {
		
	}*/
	
	public void addCustomer(Customer c) throws SQLException {
		
		Connection conn = getConnection();
		 try {
				conn.setAutoCommit(false);
				String addCustomer = ("INSERT INTO CUSTOMERS VALUES(?,?,?,?,?,?,?)");
				PreparedStatement insertCust = conn.prepareStatement(addCustomer);
				insertCust.setString(1, c.getID());
				insertCust.setString(2, c.getUsername());
				insertCust.setString(3, c.getPassword());
				insertCust.setString(4, c.getAddress());
				insertCust.setString(5, c.getPhone());
				insertCust.setString(6, c.getEmail());
				insertCust.setString(7, c.getReferralID());
				insertCust.executeUpdate();
				conn.commit();
				System.out.println("Customer added succesfully");
				insertCust.close();
			}
			catch(SQLException e){
				conn.rollback();
			}
	}
	
}
