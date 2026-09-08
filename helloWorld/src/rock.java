import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class rock {
	public static void main(String[] args) {

		int currentRound = 0;
		HashMap<Integer, String> choices = new HashMap<>();
		Random rng = new Random();
		int tie;
		int userWins;
		int computerWins;

		choices.put(1, "Rock");
		choices.put(2, "Paper");
		choices.put(3, "Scissors");
		System.out.println(choices);
		Scanner myScanner = new Scanner(System.in);

		while (true) {
			tie = 0;
			userWins = 0;
			computerWins = 0;
			System.out.println("How many rounds? ");
			int rounds = myScanner.nextInt();
			if (rounds < 1 || rounds > 10) {
				System.out.println("No. of rounds has to be between 1 and 10. ");
				System.exit(1);
			}
			for (int i=0; i < rounds; i++) {
				System.out.println("What do you choose?");
				int choice = 0;
				while (true) {
					choice = myScanner.nextInt();
					if (choice >= 1 && choice <= 3) {
						break;
					} else {
						System.out.println("Choose either 1, 2, or 3. ");
					}
				}
				String str = choices.get(choice);
				int randomNumber = rng.nextInt(3) + 1;
				String computerChoice = choices.get(randomNumber);
				System.out.println("User chose " + str + "\n Computer chose " + computerChoice);
				if (choice == randomNumber) {
					tie++;
				} else if (choice == 2 && randomNumber == 1) {
					userWins++;
				} else if (userWins == 1 && randomNumber == 3) {
					userWins++;
				} else if (userWins == 3 && randomNumber == 2){
					userWins++;
				} else {
					computerWins++;
				}

			}
			System.out.println("User won: " + userWins + " Computer won: " + computerWins + " No. of ties are: " + tie);
			System.out.println("Leave game? Y/N");
			String answer = myScanner.next();
			if (answer.equals("Y")) {
				break;
			}
		}
		System.out.println("Thank you for playing");

	}
}
