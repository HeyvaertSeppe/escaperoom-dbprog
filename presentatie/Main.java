package escaperoom.presentatie;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            IO.println("=== Escaperoom Beheersysteem ===");
            IO.println("\t1. Tijdslots beheren");
            IO.println("\t2. Deelnemers en groepen");
            IO.println("\t3. Reservaties");
            IO.println("\t0. Afsluiten");
            IO.print("Keuze: ");

            switch (IO.readln()) {
                case "1":
                    TijdslotMenu.toonMenu();
                    break;
                case "2":
                    GroepMenu.toonMenu();
                    break;
                case "3":
                    ReservatieMenu.toonMenu();
                    break;
                case "0":
                    IO.println("Tot ziens!");
                    running = false;
                    break;
                default:
                    System.out.println("Ongeldige keuze.");
            }
        }
    }
}