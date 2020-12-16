package tables;

public class Ingredient {

	private String id;
	private String name;
	private double restockPrice;
	private int stock;
	private String units;
	
	public Ingredient(String id, String name, double price, int stock, String units) {
		this.id = id;
		this.name = name;
		this.restockPrice = price;
		this.stock = stock;
		this.units = units;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getRestockPrice() {
		return this.restockPrice;
	}
	
	public int getStock() {
		return this.stock;
	}
	
	public String getUnits() {
		return this.units;
	}
}
