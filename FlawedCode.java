import java.util.Scanner;

public class FlawedCode {

    static long timer;

    public static void main(String[] args) {
        System.out.println("Press enter to stop the timer");
        timer = System.currentTimeMillis();

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        timer = System.currentTimeMillis() - timer;
        System.out.println(timer + " milliseconds");

        scanner.close();
    }
}
