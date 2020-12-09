package frontend;

import java.util.Scanner;

import backend.Database;
import tables.Customer;

public class App {
	
	private static Scanner input = new Scanner(System.in);
	private static Customer c;
	private static Database coffeeShop = new Database();
	
	public static void main(String[] args) {
		System.out.println("Welcome to Trevors Coffee Shop!");
		System.out.println("Would you like to log in or create a new account?");
		System.out.println("Enter 1 to login");
		System.out.println("Enter 2 to register");
		int selection = input.nextInt();
		if (selection == 1) {
			register();
		}
		else if (selection == 2) {
			login();
		}
		else {
			System.out.println("Invalid input");
		}
	}
	
	public static void register() {
		System.out.println("You are on the registration page");
		System.out.println("Please enter a username");
		String username = input.next();
		System.out.println("Please enter a password");
		String password = input.next();
		System.out.println("What is your address?");
		String address = input.next();
		System.out.println("What is your phone number?");
		String phone = input.next();
		System.out.println("What is your email address?");
		String email = input.next();
		System.out.println("Have you been referred by anyone? 1. Yes 2. No");
		int selection = input.nextInt();
		if (selection == 1) {
			System.out.println("What is the email of the client who referred you?");
			String refer_id = coffeeShop.getCustIDByEmail(input.next());
			c = new Customer(username, password, address, phone, email, refer_id);
		}
		else if (selection == 2) {
			c = new Customer(username, password, address, phone, email, null);
		}
		else {
			System.out.println("Invalid input");
		}
	}
	
	public static void login() {
		System.out.println("You are on the login page");
		System.out.println("Please enter a username");
		String username = input.next();
		System.out.println("Please enter a password");
		String password = input.next();
		if (/*password is ok*/) {
			coffeeShop.getCustomer(username);
		}
	}

}
