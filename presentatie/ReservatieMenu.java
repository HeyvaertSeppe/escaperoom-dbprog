package presentatie;

public class ReservatieMenu {
    public static void toonMenu() {
        boolean running = true;
        while (running) {
            IO.println("");
            IO.println("--- Reservaties ---");
            IO.println("\t1. Beschikbare tijdslots komende maand");
            IO.println("\t2. Maak nieuwe reservatie");
            IO.println("\t3. Toon reservaties op datum");
            IO.println("\t0. Terug");
            IO.print("Keuze: ");

            switch (IO.readln()) {
                case "1" -> Helper.toonBeschikbareTijdslotsKomendeMaand();
                case "2" -> Helper.maakNieuweReservatie();
                case "3" -> Helper.toonReservatiesOpDatum();
                case "0" -> running = false;
                default -> IO.println("Ongeldige keuze.");
            }
        }
    }
}
