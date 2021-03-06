package backend;

import java.sql.*;
import java.util.InputMismatchException;

import frontend.App;
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

	public String getCustIDByUsername(String username) throws SQLException {
		Connection conn = getConnection();
		String cust_id = null;
		boolean invalid = true;
		while (invalid) {
			try {
				conn.setAutoCommit(false);
				String findCust = ("SELECT CUSTOMER_ID FROM CUSTOMERS WHERE USERNAME LIKE ?");
				PreparedStatement findCuststmt = conn.prepareStatement(findCust);
				findCuststmt.setString(1, username);
				ResultSet rs = findCuststmt.executeQuery();
				while(rs.next()) {
					cust_id = rs.getString(1);
				}
				conn.commit();
				findCuststmt.close();
				invalid = false;
			}
			catch(SQLException e){
				System.out.println("Username does not exist, would you like to try another username? 1. Yes 2. No");
				int retry = 0;
				while (retry != 1 && retry != 2) {
					try {
						retry = App.input.nextInt();
						if (retry == 1) {
							System.out.println("What is the username of the client who referred you?");
							username = App.stringSplitter(App.input.next());
						}
					}
					catch (InputMismatchException ex) {
						App.input.nextLine();
						System.out.println("Invalid input, please input a number");
						continue;
					}
				}
				conn.rollback();
			}
			finally {
				conn.close();
			}
		}
		return cust_id;
	}
	
	/*public Customer getCustomer(String username) {
		
	}*/
	
	public void addCustomer(Customer c) throws SQLException {
		
		Connection conn = getConnection();
		 try {
				conn.setAutoCommit(false);
				String addCustomer = ("{call addCustomer (?,?,?,?,?,?)}");
				CallableStatement insertCust = conn.prepareCall(addCustomer);
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
		 finally {
				conn.close();
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
		finally {
			conn.close();
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
		finally {
			conn.close();
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
		finally {
			conn.close();
		}
		return c;
	}
	
	public Product getProduct(int i) throws SQLException {
		Product[] p = getAllProducts();
		return p[i];
	}
	
	public double getCartTotalCost(Product[] p, int[] quantity) throws SQLException {
		double total = 0;
		Connection conn = getConnection();
		for (int i = 0; i < p.length; i++) {
			if (p[i] != null) {
				try {
					conn.setAutoCommit(false);
					String costQuery = ("{? = call CALCULATECOST (?, ?, ?)}");
					CallableStatement getTotalCost = conn.prepareCall(costQuery);
					getTotalCost.registerOutParameter(1,Types.DOUBLE);
					getTotalCost.setString(2, p[i].getName());
					getTotalCost.setInt(3, quantity[i]);
					getTotalCost.registerOutParameter(4,Types.DOUBLE);
					getTotalCost.executeUpdate();
					total += getTotalCost.getDouble(1);
					getTotalCost.close();
				}
				catch(SQLException e){
					System.out.println(e);
					conn.rollback();
				}
			}
		}
		return total;
	}

	public Customer updateAddress(Customer c, String newAddress) throws SQLException {
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			String addCustomer = ("{call updateAddress (?, ?)}");
			CallableStatement insertCust = conn.prepareCall(addCustomer);
			insertCust.setString(1, c.getID());
			insertCust.setString(2, newAddress);
			insertCust.executeUpdate();
			conn.commit();
			c.setAddress(newAddress);
			System.out.println("Address updated succesfully ");
			insertCust.close();
		}
		catch(SQLException e){
			System.out.println(e);
			conn.rollback();
		}
		finally {
			conn.close();
		}
		return c;
	}
	
	public void newOrder(String orderID, Customer c, boolean discount) throws SQLException {
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			String newOrder = ("{call NEWORDER (?, ?, ?)}");
			CallableStatement insertOrder = conn.prepareCall(newOrder);
			insertOrder.setString(1, orderID);
			insertOrder.setString(2, c.getID());
			insertOrder.setString(3, c.getAddress());
			insertOrder.executeUpdate();
			conn.commit();
			System.out.println("Order Completed Successfully");
			insertOrder.close();
			if (discount) {
				applyDiscount(c.getID());
			}
		}
		catch(SQLException e) {
			conn.rollback();
		}
		finally {
			conn.close();
		}
	}
	
	private void applyDiscount(String CustID) throws SQLException {
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			String removeCredit = ("{call updateCreditsRemaining (?)}");
			CallableStatement removeCreditstmt = conn.prepareCall(removeCredit);
			removeCreditstmt.setString(1, CustID);
			removeCreditstmt.executeUpdate();
			conn.commit();
			removeCreditstmt.close();
		}
		catch (SQLException e) {
			conn.rollback();
		}
		finally {
			conn.close();
		}
	}

	public String generateOrderID() throws SQLException {
		Connection conn = getConnection();
		String count = "";
		try {
			conn.setAutoCommit(false);
			String custCount = ("SELECT COUNT(ORDER_ID) FROM ORDERS_COFFEE");
			PreparedStatement getCustCount = conn.prepareStatement(custCount);
			ResultSet rs = getCustCount.executeQuery();
			conn.commit();
			while(rs.next()) {
				count = rs.getString(1);
			}
			getCustCount.close();
		}
		catch(SQLException e){
			conn.rollback();
		}
		finally {
			conn.close();
		}
		int countNum = Integer.parseInt(count) + 1;
		if (countNum < 10) {
			return "O000" + countNum;
		}
		else if (countNum >= 10 && countNum < 100) {
			return "O00" + countNum;
		}
		else if (countNum >= 100 && countNum < 1000) {
			return "O0" + countNum;
		}
		else {
			return "O" + countNum;
		}
	}
	
	public void addOrderItems(String orderID, Product[] products, int[] quantity) throws SQLException {
		Connection conn = getConnection();
		for (int i = 0; i < products.length; i++) {
			if (products[i] != null) {
				try {
					conn.setAutoCommit(false);
					String newOrder = ("{call ADDORDERITEM (?, ?, ?)}");
					CallableStatement insertOrder = conn.prepareCall(newOrder);
					insertOrder.setString(1, orderID);
					insertOrder.setString(2, products[i].getID());
					insertOrder.setInt(3, quantity[i]);
					insertOrder.executeUpdate();
					conn.commit();
					insertOrder.close();
				}
				catch(SQLException e) {
					conn.rollback();
				}
			}
		}
		System.out.println("Order Items Added Successfully");
		conn.close();
	}
	
	public void checkStock(Product[] p, int[] q) throws SQLException {
		Connection conn = getConnection();
		for (int i = 0; i < p.length; i++) {
			if (p[i] != null) {
				try {
					conn.setAutoCommit(false);
					String check = ("{call checkProductInStock (?, ?)}");
					CallableStatement checkProduct = conn.prepareCall(check);
					checkProduct.setString(1, p[i].getID());
					checkProduct.setInt(2, q[i]);
					checkProduct.executeUpdate();
					conn.commit();
					checkProduct.close();
				}
				catch(SQLException e) {
					System.out.println("We do not have enough stock to make " + q[i] + " " + p[i].getName() + "s.");
					System.out.println("We have removed them from your order.");
					App.getCart().removeProduct(i);
					conn.rollback();
				}
			}
		}
		conn.close();
	}

	public boolean getDiscount(String username) throws SQLException {
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			String check = ("SELECT REFERRAL_CREDITS_REMAINING FROM USERSREFERRAL WHERE USERNAME LIKE ?");
			PreparedStatement checkRef = conn.prepareStatement(check);
			checkRef.setString(1, username);
			ResultSet rs = checkRef.executeQuery();
			while(rs.next()) {
				if (Integer.parseInt(rs.getString(1)) > 0) {
					return true;
				}
			}
			conn.commit();
			checkRef.close();
		}
		catch(SQLException e) {
			conn.rollback();
		}
		finally {
			conn.close();
		}
		return false;
	}
}
