package tables;

public class Product {

	private String id;
	private String name;
	private double retailPrice;
	private String recipe_id;
	
	public Product(String id, String name, double price, String recipe_id) {
		this.id = id;
		this.name = name;
		this.retailPrice = price;
		this.recipe_id = recipe_id;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public double getRetailPrice() {
		return this.retailPrice;
	}
	
	public String getRecipeID() {
		return this.recipe_id;
	}
}
