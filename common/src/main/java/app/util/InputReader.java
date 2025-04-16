package app.util;

import java.util.Scanner;

/**
 * A utility for reading data from the console.
 */
public class InputReader {

    private Scanner scanner;

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public String prompt(String prompt) {
        System.out.print(prompt);
        return readLine();
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public InputReader cloneScanner() {
        return new InputReader(this.scanner);
    }
}