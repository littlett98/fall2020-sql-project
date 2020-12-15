package frontend;

import java.io.FilterInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import backend.*;
import tables.*;

public class App {
	
	private static Scanner input;
	private static Customer c;
	private static Database coffeeShop = new Database();
	private static Cart cart = new Cart();
	private static Product[] allProducts;
	
	public static void main(String[] args) throws SQLException {
		input = new Scanner(new FilterInputStream(System.in) {
		    @Override
		    public void close() throws IOException {
		        // do nothing here ! 
		    }
		});
		boolean loggedIn = false;
		System.out.println("Welcome to Trevors Coffee Shop!");
		while (!loggedIn) {
			try {
				System.out.println("Would you like to log in, create a new account, or view a list of our products?");
				System.out.println("Enter 1 to log in");
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
			}
			catch (InputMismatchException e) {
				input.next();
				System.out.println("Please input a different selection. Please note it should be a number.");
				continue;
			}
		}
		if (loggedIn) {
			viewProductsLoggedIn();
			input.close();
		}
	}
	
	public static void register() {
		boolean invalid = true;
		System.out.println("You are on the registration page");
		DatabaseSecurity userCreation = new DatabaseSecurity();
		String username = "";
		String password = "";
		while (invalid) {
			System.out.println("Please enter a username (Must be at least 3 characters long)");
			username = stringSplitter(input.next());
			System.out.println("Please enter a password (Must be at least 3 characters long)");
			password = stringSplitter(input.next());
			if (password.length() >= 3 && username.length() >= 3) {
				invalid = false;
			}
			else {
				System.out.println("Invalid inputs, please retry. Make sure to never uses spaces in the username or password!");
			}
		}
		System.out.println("What is your address?");
		String address = input.nextLine();
		while (address.length() < 3) {
			System.out.println("Address must be longer than 3 characters");
			address = input.nextLine();
		}
		System.out.println("What is your phone number?");
		String phone = stringSplitter(input.next());
		while (phone.length() != 10) {
			System.out.println("Phone number must be exactly 10 digits");
			phone = input.nextLine();
		}
		System.out.println("What is your email address?");
		String email = stringSplitter(input.next());
		while (email.indexOf("@") == -1 || email.indexOf(".") == -1) {
			System.out.println("Please input a valid email");
			email = input.nextLine();
		}
		System.out.println("Have you been referred by anyone? 1. Yes 2. No");
		int selection = 0;
		while (selection != 1 && selection != 2) {
			try {
				selection = input.nextInt();
			}
			catch (InputMismatchException e) {
				input.nextLine();
				System.out.println("Invalid selection, please input: 1. Yes 2. No");
				continue;
			}
			if (selection != 1 && selection != 2) {
				System.out.println("Please input either 1. Yes or 2. No");
				continue;
			}
		}
		if (selection == 1) {
			System.out.println("What is the email of the client who referred you?");
			String refer_id = coffeeShop.getCustIDByEmail(input.next());
			c = new Customer(username, address, phone, email, refer_id);
			try {
				userCreation.newUser(username, password);
				coffeeShop.addCustomer(c);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if (selection == 2) {
			c = new Customer(username, address, phone, email, null);
			try {
				userCreation.newUser(username, password);
				coffeeShop.addCustomer(c);
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
		if (allProducts == null) {
			allProducts = coffeeShop.getAllProducts();
		}
		for (int i = 0; i < allProducts.length; i++) {
			System.out.println((i+1) + ". " + allProducts[i].getName() + " - $" + allProducts[i].getRetailPrice());
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
			System.out.println("Which product would you like to add to your cart?");
			int selection = 0;
			while (selection < 1 || selection > allProducts.length) {
				try {
					selection = reader.nextInt();
					if (selection < 1 || selection > allProducts.length) {
						System.out.println("Invalid input, please make sure you type a number associated to a product");
					}
				}
				catch (InputMismatchException e) {
					reader.nextLine();
					System.out.println("Invalid input, please input a number");
					continue;
				}
			}
			System.out.println("How many " + allProducts[selection-1].getName() + "s would you like?");
			int quantity = 0;
			while (quantity < 1) {
				try {
					quantity = reader.nextInt();
					if (quantity < 1) {
						System.out.println("Invalid Input, please make sure your quanitity is higher than 0");
					}
				}
				catch (InputMismatchException e) {
					reader.nextLine();
					System.out.println("Not a number, please input the quantity of " + allProducts[selection-1].getName() + "s you want");
					continue;
				}
			}
			addToCart(selection, quantity);
			cart.viewCartWithTotal();
			
			System.out.println("What would you like to do? 1. Continue shopping 2. Check Out");
			int next = 0;
			while (next != 1 && next != 2) {
				try {
					next = reader.nextInt();
					if (next != 1 && next != 2) {
						System.out.println("Invalid input, Please try again. 1. Continue shopping 2. Check Out");
					}
				}
				catch (InputMismatchException e) {
					reader.nextLine();
					System.out.println("Please input a different selection. Please note it should be a number.");
					continue;
				}
			}
			reader.close();
			if (next == 1) {
				continue;
			}
			else if (next == 2) {
				shopping = false;
			}
			cart.checkout();
		}
	}
	
	
	public static void addToCart(int selection, int quantity) throws SQLException {
		cart.addProduct(allProducts[selection - 1], quantity);
	}
	
	public Customer getCustomer() {
		return c;
	}
	
	public static String stringSplitter(String word) {
		String[] firstWord = word.split(" ");
		input.nextLine();
		return firstWord[0];
	}
}
