package escaperoom.presentatie;
import escaperoom.data.DatabaseConnection;

public class TijdslotMenu {

    public static void toonMenu(){
        DatabaseConnection db = new DatabaseConnection();

        boolean running = true;
        while (running) {
            IO.println("\n");
            IO.println("--- Tijdslots beheren ---");
            IO.println("\t1. Toon alle tijdslots");
            IO.println("\t2. Toon tijdslots op datum");
            IO.println("\t3. Voeg tijdslot toe");
            IO.println("\t4. Wijs gamemaster toe aan tijdslot");
            IO.println("\t0. Terug");
            IO.print("Keuze: ");

            switch (IO.readln()) {
                case "1":
                    Helper.toonLijst("Tijdsloten");
                    break;
                case "2":
                    Helper.toonTijdslot_datum();
                    break;
                case "3":
                    Helper.voeTijdslotToe();
                    break;
                case "4":
                    Helper.voegGamemasterToe();
                    break;
                case "0":

                    running = false;
                    break;
                default:
                    IO.println("Ongeldige keuze.");
            }
        }
    }


}
