package backend;

import tables.Product;

public class Cart {
	
	private Product[] products;
	private int[] quantity;
	
	public Cart() {
		products = new Product[10];
		quantity = new int[10];
	}
	
	public void addProduct(Product p, int q) {
		if (productArrayFull()) {
			increaseArraySize();
		}
		for (int i = 0; i < products.length; i++) {
			if (products[i] == null) {
				products[i] = p;
				quantity[i] = q;
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
		Product[] tempProd = new Product[products.length + 10];
		int[] tempQTY = new int[quantity.length + 10];
		for (int i = 0; i < products.length; i++) {
			tempProd[i] = products[i];
			tempQTY[i] = quantity[i];
		}
		products = tempProd;
		quantity = tempQTY;
	}
	
	public void viewCart() {
		for (int i = 0; i < products.length; i++) {
			if (products[i] != null ) {
				System.out.print(products[i].getName() + " ");
				System.out.println(quantity[i]);
			}
		}
	}
}
