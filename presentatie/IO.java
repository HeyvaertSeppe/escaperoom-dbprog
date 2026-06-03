package presentatie;

import java.io.InputStream;
import java.util.Scanner;

public final class IO {
    private static InputStream lastIn = System.in;
    private static Scanner scanner = new Scanner(System.in);

    private IO() {
    }

    private static void ensureScanner() {
        if (System.in != lastIn) {
            lastIn = System.in;
            scanner = new Scanner(System.in);
        }
    }

    public static String readln() {
        ensureScanner();
        return scanner.hasNextLine() ? scanner.nextLine() : "";
    }

    public static String readln(String prompt) {
        print(prompt);
        return readln();
    }

    public static void println(String text) {
        System.out.println(text);
    }

    public static void println(Object object) {
        System.out.println(object);
    }

    public static void print(String text) {
        System.out.print(text);
    }
}
