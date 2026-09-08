import java.util.Scanner;

public class Hello {
	public static void main(String[] args) {
		System.out.println("New branch!");
		System.out.println("Hello, World!");
		Scanner myScanner = new Scanner(System.in);
		String str1;

		boolean isValid = false;

		do {
			try {
				System.out.println("Please enter a whole number: ");
				String input = myScanner.nextLine();
				int number = Integer.parseInt(input);
				if (number >= 1 && number <= 10) {
					isValid = true;
				}
			} catch(NumberFormatException ex) {
				System.out.println("That was not a whole number!");
			}

		} while(!isValid);

		while (true) {
			System.out.println("Please enter the first number to be added");
			String input = myScanner.nextLine();
			if (input==null|| input.isEmpty()) {
				System.out.println("You did not enter anything");
			} else {
				break;
			}
		}



		String str2;
		System.out.println("Please enter the first number to be added");
		str1 = myScanner.nextLine();
		System.out.println("Please enter the second number to be added");
		str2 = myScanner.nextLine();
		int number1 = Integer.parseInt(str1);
		int number2 = Integer.parseInt(str2);
		int sum = number1+number2;
		System.out.println("Sum is " + sum);
	}
}
