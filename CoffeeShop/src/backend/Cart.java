package backend;

import tables.Product;

public class Cart {
	
	private Product[] products;
	
	public Cart() {
		products = new Product[10];
	}
	
	public void addProduct(Product p) {
		if (productArrayFull()) {
			increaseArraySize();
		}
		for (int i = 0; i < products.length; i++) {
			if (products[i] == null) {
				products[i] = p;
				break;
			}
		}
	}
	
	public boolean productArrayFull() {
		for (int i = 0; i < products.length; i++) {
			if (products[i] == null) {
				return false;
			}
		}
		return true;
	}
	
	public void increaseArraySize() {
		Product[] temp = new Product[products.length + 10];
		for (int i = 0; i < products.length; i++) {
			temp[i] = products[i];
		}
		products = temp;
	}
}
