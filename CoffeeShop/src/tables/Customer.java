package tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import backend.*;

public class Customer {
	
	private Database coffeeShop = new Database();
	private String id;
	private String username;
	private String address;
	private String phone;
	private String email;
	private String referral_id;
	
	public Customer(String user, String add, String pNum, String email, String ref_id) {
		try {
			this.id = generateID();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.username = user;
		this.address = add;
		this.phone = pNum;
		this.email = email;
		this.referral_id = ref_id;
	}
	
	public Customer(String id, String user, String add, String pNum, String email, String ref_id) {
		this.id = id;
		this.username = user;
		this.address = add;
		this.phone = pNum;
		this.email = email;
		this.referral_id = ref_id;
	}
	
	public String generateID() throws SQLException {
		Connection conn = coffeeShop.getConnection();
		String count = "";
		try {
			conn.setAutoCommit(false);
			String custCount = ("SELECT COUNT(CUSTOMER_ID) FROM CUSTOMERS");
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
		int countNum = Integer.parseInt(count) + 1;
		if (countNum < 10) {
			return "C000" + countNum;
		}
		else if (countNum >= 10 && countNum < 100) {
			return "C00" + countNum;
		}
		else if (countNum >= 100 && countNum < 1000) {
			return "C0" + countNum;
		}
		else {
			return "C" + countNum;
		}
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getPhone() {
		return this.phone;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getReferralID() {
		return this.referral_id;
	}
}
