package presentatie;

public class Main {
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            toonHoofdMenu();
            String keuze = IO.readln();
            switch (keuze) {
                case "1" -> TijdslotMenu.toonMenu();
                case "2" -> GroepMenu.toonMenu();
                case "3" -> ReservatieMenu.toonMenu();
                case "0" -> {
                    IO.println("Tot ziens!");
                    running = false;
                }
                default -> IO.println("Ongeldige keuze.");
            }
        }
    }

    private static void toonHoofdMenu() {
        IO.println("=== Escaperoom Beheersysteem ===");
        IO.println("\t1. Tijdslots beheren");
        IO.println("\t2. Deelnemers en groepen");
        IO.println("\t3. Reservaties");
        IO.println("\t0. Afsluiten");
        IO.print("Keuze: ");
    }
}
