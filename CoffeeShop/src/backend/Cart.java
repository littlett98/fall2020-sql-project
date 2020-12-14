package backend;

import java.io.FilterInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import frontend.App;
import tables.Customer;
import tables.Product;

public class Cart {
	
	private Product[] products;
	private int[] quantity;
	Database coffeeShop;
	
	public Cart() {
		
		coffeeShop = new Database();
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
				System.out.println("The Products you've selected so far: ");
				System.out.print(quantity[i] + " ");
				System.out.println(products[i].getName() + "s");
			}
		}
	}
	
	public void viewCartWithTotal() throws SQLException {
		viewCart();
		double total = coffeeShop.getCartTotalCost(products, quantity);
		System.out.println("Total cost so far: $" + total);
	}
	
	public void checkout() throws SQLException {
		Scanner reader = new Scanner(new FilterInputStream(System.in) {
		    @Override
		    public void close() throws IOException {
		        // do nothing here ! 
		    }
		});
		System.out.println("The Products you've selected so far: ");
		for (int i = 0; i < products.length; i++) {
			if (products[i] != null ) {
				System.out.println(quantity[i] + " " + products[i].getName() + "s - " + products[i].getRetailPrice() + " each");
			}
		}
		double total = coffeeShop.getCartTotalCost(products, quantity);
		System.out.println("The total cost for your order is: $" + total);
		App app = new App();
		Customer c = app.getCustomer();
		System.out.println("Your current address is: " + c.getAddress());
		System.out.println("Would you like to change your address? 1. Yes 2. No");
		int choice = reader.nextInt();
		if (choice == 1) {
			System.out.println("What is your new address?");
			reader.nextLine();
			String newAddress = reader.nextLine();
			c = coffeeShop.updateAddress(c, newAddress);
		}
		System.out.println("Would you like to check out now or cancel your order? 1. Check Out 2. Close");
		choice = reader.nextInt();
		if (choice == 1) {
			String orderID = coffeeShop.generateOrderID();
			coffeeShop.newOrder(orderID, c);
			coffeeShop.addOrderItems(orderID, products, quantity);
			System.out.println("Thank you for your business! Have a great day!");
		}
		else if (choice == 2) {
			System.out.println("Sorry to see you go, have a wonderful day!");
		}
		reader.close();
	}
}
