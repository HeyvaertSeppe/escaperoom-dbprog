package presentatie;

public class TijdslotMenu {
    public static void toonMenu() {
        boolean running = true;
        while (running) {
            IO.println("");
            IO.println("--- Tijdslots beheren ---");
            IO.println("");
            IO.println("\t1. Toon alle tijdslots");
            IO.println("");
            IO.println("\t2. Toon tijdslots op datum");
            IO.println("");
            IO.println("\t3. Voeg tijdslot toe");
            IO.println("");
            IO.println("\t4. Wijs gamemaster toe aan tijdslot");
            IO.println("");
            IO.println("\t0. Terug");
            IO.print("Keuze: ");

            switch (IO.readln()) {
                case "1" -> Helper.toonAlleTijdslots();
                case "2" -> Helper.toonTijdslotsOpDatum();
                case "3" -> Helper.voegTijdslotToe();
                case "4" -> Helper.wijsGamemasterToe();
                case "0" -> running = false;
                default -> IO.println("Ongeldige keuze.");
            }
        }
    }
}
