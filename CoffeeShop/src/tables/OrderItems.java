package tables;

public class OrderItems {
	
	private String order_id;
	private String product_id;
	private int quantity;
	
	public OrderItems(String o_id, String p_id, int quantity) {
		this.order_id = o_id;
		this.product_id = p_id;
		this.quantity = quantity;
	}
	
	public String getOrderID() {
		return this.order_id;
	}
	
	public String getProductID() {
		return this.product_id;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
}
