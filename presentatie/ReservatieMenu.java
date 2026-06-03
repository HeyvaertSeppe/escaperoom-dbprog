package escaperoom.presentatie;

public class ReservatieMenu {
    public static void toonMenu() {
        boolean running = true;
        while (running) {
            IO.println("\n");
            IO.println("=== Reservatie ===");
            IO.println("\t1. Beschikbare tijdslots komende maand");
            IO.println("\t2. Maak nieuwe reervatie");
            IO.println("\t3. Toon reservatie op datum");
            IO.println("\t0. Terug");
            IO.print("Keuze: ");

            switch (IO.readln()) {
                case "1":

                    break;
                case "2":

                    break;
                case "3":

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
