package com.floormastery.io;

import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserIOConsoleImpl implements UserIO {
	private final Scanner scanner = new Scanner(System.in);

	public void print(String msg) {
		System.out.println(msg);
	}

	public String readString(String prompt) {
		// Print prompt and return user input
		print(prompt);
		return scanner.nextLine();
	}


	public int readInt(String prompt) {
		// Print prompt
		int number;

		// Keep asking user until a number is inputted
		while (true) {

			try {
				print(prompt);
				String input = scanner.nextLine();
				// Parse input to integer
				number = Integer.parseInt(input);
				break;

			} catch (NumberFormatException ex) {
				print("Input could not be parsed into an integer");
			}

		}
		return number;
	}

	public int readInt(String prompt, int min, int max) {
		int number;

		// Keep looping unitl a number between min and max is given
		while (true) {

			try {
				print(prompt);
				String input = scanner.nextLine();
				// Parse input to int
				number = Integer.parseInt(input);

				// Only break loop if input is between the min and max value
				if (number >= min && number <= max) {
					break;
				}

			} catch (NumberFormatException ex) {
				print("Input could not be parsed into an integer");
			}

		}
		return number;
	}

	@Override
	public int readIntForEdit(String prompt) {
		// Asks user for an int, but can also accept blank inputs
		int number;

		while (true) {

			try {
				print(prompt);
				String input = scanner.nextLine();

				if (!input.isBlank()) {
					number = Integer.parseInt(input);
					return number;
				}

				break;
			}
			catch (NumberFormatException ex) {
				print("Input could not be parsed into an integer");
			}

		}
		return 0;
	}

}