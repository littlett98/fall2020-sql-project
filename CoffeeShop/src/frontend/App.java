package frontend;

import java.io.FilterInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import backend.*;
import tables.*;

public class App {
	
	private static Scanner input = new Scanner(System.in);
	private static Customer c;
	private static Database coffeeShop = new Database();
	private static Cart cart = new Cart();
	
	public static void main(String[] args) throws SQLException {
		boolean loggedIn = false;
		System.out.println("Welcome to Trevors Coffee Shop!");
		System.out.println("Would you like to log in or create a new account?");
		System.out.println("Enter 1 to login");
		System.out.println("Enter 2 to register");
		System.out.println("Enter 3 to view our products");
		int selection = input.nextInt();
		if (selection == 1) {
			login();
			loggedIn = true;
		}
		else if (selection == 2) {
			register();
			loggedIn = true;
		}
		else if (selection == 3) {
			viewProducts();
		}
		else {
			System.out.println("Invalid input");
		}
		if (loggedIn) {
			viewProductsLoggedIn();
		}
	}
	
	public static void register() {
		System.out.println("You are on the registration page");
		DatabaseSecurity userCreation = new DatabaseSecurity();
		System.out.println("Please enter a username");
		String username = input.next();
		System.out.println("Please enter a password");
		String password = input.next();
		try {
			userCreation.newUser(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("What is your address?");
		Scanner in = new Scanner(System.in);
		String address = in.nextLine();
		System.out.println("What is your phone number?");
		String phone = input.next();
		System.out.println("What is your email address?");
		String email = input.next();
		System.out.println("Have you been referred by anyone? 1. Yes 2. No");
		int selection = input.nextInt();
		if (selection == 1) {
			System.out.println("What is the email of the client who referred you?");
			String refer_id = coffeeShop.getCustIDByEmail(input.next());
			c = new Customer(username, address, phone, email, refer_id);
			try {
				coffeeShop.addCustomer(c);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if (selection == 2) {
			c = new Customer(username, address, phone, email, null);
			try {
				coffeeShop.addCustomer(c);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Invalid input");
		}
	}
	
	public static void login() {
		System.out.println("You are on the login page");
		DatabaseSecurity userCreation = new DatabaseSecurity();
		try {
			c = userCreation.login();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void viewProducts() throws SQLException {
		System.out.println("These are the current products we offer:");
		Product[] products = coffeeShop.getAllProducts();
		for (int i = 0; i < products.length; i++) {
			System.out.println((i+1) + ". " + products[i].getName() + " - $" + products[i].getRetailPrice());
		}
	}
	
	public static void viewProductsLoggedIn() throws SQLException {
		boolean shopping = true;
		Scanner reader;
		System.out.println("Welcome " + c.getUsername() + "!");
		while(shopping) {
			reader = new Scanner(new FilterInputStream(System.in) {
			    @Override
			    public void close() throws IOException {
			        // do nothing here ! 
			    }
			});
			viewProducts();
			System.out.println("Which products would you like to add to your cart?");
			String selection = reader.nextLine();
			String[] allSelections = selection.split(" ");
			int[] allSelectionsNum = new int[allSelections.length];
			for (int i = 0; i < allSelections.length; i++) {
				allSelectionsNum[i] = Integer.parseInt(allSelections[i]);
			}
			addToCart(allSelectionsNum);
			System.out.println("What would you like to do? 1. Continue shopping 2. Check Out");
			int next = reader.nextInt();
			reader.close();
			if (next == 1) {
				continue;
			}
			else if (next == 2) {
				shopping = false;
			}
		}
		cart.viewCart();
	}
	
	public static void addToCart(int[] allSelections) throws SQLException {
		Product[] allProducts = coffeeShop.getAllProducts();
		for (int i = 0; i < allSelections.length; i++) {
			cart.addProduct(allProducts[allSelections[i]-1]);
		}
	}
}
