package tables;

public class Order {
	
	private String id;
	private String customer_id;
	private String address;
	private String order_placed;
	private String order_complete;
	
	public Order(String id, String cust_id, String address, String order_placed, String order_complete) {
		this.id = id;
		this.customer_id = cust_id;
		this.address = address;
		this.order_placed = order_placed;
		this.order_complete = order_complete;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getCustomerID() {
		return this.customer_id;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public String getDatePlaced() {
		return this.order_placed;
	}
	
	public String getDateComplete() {
		return this.order_complete;
	}
}
