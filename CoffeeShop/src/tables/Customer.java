package tables;

public class Customer {

	private String id;
	private String username;
	private String password;
	private String address;
	private String phone;
	private String email;
	private String referral_id;
	
	public Customer(String user, String pw, String add, String pNum, String email, String ref_id) {
		this.id = generateID();
		this.username = user;
		this.password = pw;
		this.address = add;
		this.phone = pNum;
		this.email = email;
		this.referral_id = ref_id;
	}
	
	public String generateID() {
		return "c001";
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
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
