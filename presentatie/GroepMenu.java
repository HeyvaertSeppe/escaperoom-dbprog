package presentatie;

public class GroepMenu {
    public static void toonMenu() {
        boolean running = true;
        while (running) {
            IO.println("");
            IO.println("--- Deelnemers en groepen beheren ---");
            IO.println("");
            IO.println("\t1. Toon alle groepen");
            IO.println("");
            IO.println("\t2. Toon deelnemers van een groep");
            IO.println("");
            IO.println("\t3. Maak nieuwe groep aan");
            IO.println("");
            IO.println("\t0. Terug");
            IO.print("Keuze: ");

            switch (IO.readln()) {
                case "1" -> Helper.toonAlleGroepen();
                case "2" -> Helper.toonDeelnemersVanGroep();
                case "3" -> Helper.maakNieuweGroep();
                case "0" -> running = false;
                default -> IO.println("Ongeldige keuze.");
            }
        }
    }
}
