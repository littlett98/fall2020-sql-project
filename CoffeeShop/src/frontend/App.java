package frontend;

import java.sql.SQLException;
import java.util.Scanner;

import backend.*;
import tables.*;

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
			login();
		}
		else if (selection == 2) {
			register();
		}
		else {
			System.out.println("Invalid input");
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
			try {
				coffeeShop.addCustomer(c);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if (selection == 2) {
			c = new Customer(username, password, address, phone, email, null);
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
			System.out.println(userCreation.login());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
