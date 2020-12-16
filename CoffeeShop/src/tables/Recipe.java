package tables;

public class Recipe {

	private String recipe_id;
	private String ingredient_id;
	private int quantity;
	
	public Recipe(String r_id, String I_id, int quantity) {
		this.recipe_id = r_id;
		this.ingredient_id = I_id;
		this.quantity = quantity;
	}
	
	public String getRecipeID() {
		return this.recipe_id;
	}
	
	public String getIngredientID() {
		return this.ingredient_id;
	}
	
	public int getQuanitity() {
		return this.quantity;
	}
}
