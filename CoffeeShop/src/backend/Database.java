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
				String addCustomer = ("{call addCustomer (?,?,?,?,?,?)}");
				PreparedStatement insertCust = conn.prepareStatement(addCustomer);
				insertCust.setString(1, c.getID());
				insertCust.setString(2, c.getUsername());
				insertCust.setString(3, c.getAddress());
				insertCust.setString(4, c.getPhone());
				insertCust.setString(5, c.getEmail());
				insertCust.setString(6, c.getReferralID());
				insertCust.executeUpdate();
				conn.commit();
				System.out.println("Customer added succesfully");
				insertCust.close();
			}
			catch(SQLException e){
				System.out.println(e);
				conn.rollback();
			}
	}
	
	public Product[] getAllProducts() throws SQLException {
		Product[] products = new Product[getAmountOfProducts()];
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			String custCount = ("SELECT * FROM PRODUCTS");
			PreparedStatement getCustCount = conn.prepareStatement(custCount);
			ResultSet rs = getCustCount.executeQuery();
			int i = 0;
			while(rs.next()) {
				products[i] = new Product(rs.getString(1), rs.getString(2), Double.parseDouble(rs.getString(3)), rs.getString(4));
				i++;
			}
			conn.commit();
			getCustCount.close();
		}
		catch(SQLException e){
			conn.rollback();
		}
		return products;
	}
	
	public int getAmountOfProducts() throws SQLException {
		
		Connection conn = getConnection();
		int amountProducts = 0;
		try {
			conn.setAutoCommit(false);
			String custCount = ("SELECT COUNT(PRODUCT_ID) FROM PRODUCTS");
			PreparedStatement getCustCount = conn.prepareStatement(custCount);
			ResultSet rs = getCustCount.executeQuery();
			while(rs.next()) {
				amountProducts = Integer.parseInt(rs.getString(1));
			}
			getCustCount.close();
		}
		catch(SQLException e){
			conn.rollback();
		}
		return amountProducts;
	}
	
	public Customer getCustomer(String username) throws SQLException {
		Customer c = null;
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			String currentCustomer = ("SELECT * FROM CUSTOMERS WHERE USERNAME LIKE ?");
			PreparedStatement getCurrentCustomer = conn.prepareStatement(currentCustomer);
			getCurrentCustomer.setString(1, username);
			ResultSet rs = getCurrentCustomer.executeQuery();
			while(rs.next()) {
				c = new Customer(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
			}
			conn.commit();
			getCurrentCustomer.close();
		}
		catch(SQLException e){
			conn.rollback();
		}
		return c;
	}
}
