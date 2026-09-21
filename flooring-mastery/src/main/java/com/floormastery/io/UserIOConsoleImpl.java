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
		print(prompt);
		return scanner.nextLine();
	}


	public int readInt(String prompt) {
		int number;
		while (true) {
			try {
				print(prompt);
				String input = scanner.nextLine();
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
		while (true) {
			try {
				print(prompt);
				String input = scanner.nextLine();
				number = Integer.parseInt(input);
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
			} catch (NumberFormatException ex) {
				print("Input could not be parsed into an integer");
			}
		}
		return 0;
	}

}