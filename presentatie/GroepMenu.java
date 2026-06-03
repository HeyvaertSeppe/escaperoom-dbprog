package escaperoom.presentatie;

public class GroepMenu {
    public static void toonMenu() {
        boolean running = true;
        while (running) {
            IO.println("\n");
            IO.println("--- Deelnemers en groepen beheren ---");
            IO.println("\t1. Toon alle groepen");
            IO.println("\t2. Toon deelnemers van een groep");
            IO.println("\t3. Maak nieuwe groep aan");
            IO.println("\t0. Terug");
            IO.print("Keuze: ");

            switch (IO.readln()) {
                case "1":
                    Helper.toonLijst("Groepen");
                    break;
                case "2":
                    Helper.toonLijst("Deelnemers");
                    break;
                case "3":
                    ReservatieMenu.toonMenu();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Ongeldige keuze.");
            }
        }
    }
}
