package escaperoom.presentatie;

import escaperoom.data.DatabaseConnection;
import escaperoom.logica.*;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    static DatabaseConnection db = new DatabaseConnection();
    static void toonLijst(String soort) {
        if(soort == "Tijdsloten"){
            IO.println("\nAlle "+soort+"en");
            List<Tijdslot> lijst = db.geefTijdsloten();
            for (Tijdslot bg : lijst) {
                IO.println(bg);
            }
        }

        else if (soort == "Groepen") {
            IO.println("\nAlle "+soort+"en");
            List<Groep> lijst = db.geefGroepen();
            for (Groep bg : lijst) {
                IO.println(bg);
            }
        }


        else if (soort == "Deelnemers") {
            toonLijst("Groepen");
            int input = Integer.parseInt(IO.readln("Groep nummer:"));

            IO.println("\nDeelnemers van "+ db.getGroepNaamByID(input)+":"  );
            List<Deelnemers> lijst = db.geefDeelnemers(input);
            for( Deelnemers bg : lijst) {
                IO.println(bg);
            }
        }

    }
    static void toonTijdslot_datum() {
        String input = IO.readln("\nDatum (dd/MM/yy): ");

        String dag = input.substring(0, 2);
        String maand = input.substring(3, 5);
        String jaar = input.substring(6, 8);

        String datum = jaar + "-" + maand + "-" + dag;

        IO.println("\nAlle tijdsloten op datum:");
        List<Tijdslot> lijst = db.geefTijdsloten_datum(datum);
        for (Tijdslot bg : lijst) {
            IO.println(bg);
        }
    }

    static void voeTijdslotToe(){
        int gamemaster_count = 0;
        int tijdslot_count = 0;
        String datum = IO.readln("\nDatum (dd/MM/yy of dd/MM/yyyy): ");

        String[] delen = datum.split("/");
        String dag = delen[0];
        String maand = delen[1];
        String jaar = delen[2];

        if (jaar.length() == 2) {
            jaar = "20" + jaar;
        }

        String startuur = IO.readln("\nStartuur (HH:mm): ");

        String datum_reform = jaar + "-" + maand + "-" + dag + " " + startuur + ":00";

        IO.println("\nKies een tarief:");
        List<Tarief> lijst_tf = db.getTarieven();
        for (Tarief bg : lijst_tf) {
            IO.println(bg);
            tijdslot_count++;
        }

     int keuze_tf = Integer.parseInt(IO.readln("\nKeuze (1-"+tijdslot_count+"): "));

     String gamemaster = IO.readln("\nGamemaster toewijzen? (j/n): ");
     String keuze_gm = "";
        if (gamemaster.equalsIgnoreCase("j")) {
         IO.println("\nKies een gamemaster:");

         List<Gamemaster> lijst_gm = db.getGamemasters();
         for (Gamemaster bg : lijst_gm) {
             IO.println(bg);
             gamemaster_count ++;
         }
         keuze_gm =IO.readln("\nKeuze (1-"+gamemaster_count+"): ");

         IO.println(db.voegTijdslotToe(datum_reform, keuze_tf,keuze_gm));
         IO.println("Gamemaster "+db.getGamemasterById(Integer.parseInt(keuze_gm))+" toegewezen");
     } else{
         keuze_gm = null;

         IO.println(db.voegTijdslotToe(datum_reform, keuze_tf,keuze_gm));
     }

    }

    static void voegGamemasterToe() {

        List<Tijdslot> beschikbareTijdsloten = db.geefTijdsloten();

        if (beschikbareTijdsloten.isEmpty()) {
            IO.println("Er zijn momenteel geen tijdsloten zonder gamemaster.");
            return;
        }


        IO.println("\nKies een tijdslot:");
        for (int i = 0; i < beschikbareTijdsloten.size(); i++)
            IO.println((i + 1) + ". " + beschikbareTijdsloten.get(i).toStringSimple());

        int keuze_tf = leesBereik("Keuze (1-" + beschikbareTijdsloten.size() + "): ", 1, beschikbareTijdsloten.size());
        if (keuze_tf == -1) return;


        List<Gamemaster> lijst_gm = db.getGamemasters();

        if (lijst_gm.isEmpty()) {
            IO.println("Er zijn geen gamemasters beschikbaar.");
            return;
        }


        IO.println("\nKies een gamemaster:");
        for (int i = 0; i < lijst_gm.size(); i++)
            IO.println((i + 1) + ". " + lijst_gm.get(i));

        int keuze_gm = leesBereik("Keuze (1-" + lijst_gm.size() + "): ", 1, lijst_gm.size());
        if (keuze_gm == -1) return;


        db.setGamemaster(beschikbareTijdsloten.get(keuze_tf - 1).getId(), lijst_gm.get(keuze_gm - 1).getId());
        IO.println("Gamemaster " + lijst_gm.get(keuze_gm - 1) + " toegewezen aan " + beschikbareTijdsloten.get(keuze_tf - 1).toStringSimple());
    }


    private static int leesBereik(String prompt, int min, int max) {
        try {
            int keuze = Integer.parseInt(IO.readln(prompt));
            if (keuze < min || keuze > max) {
                IO.println("Keuze buiten bereik (" + min + "-" + max + ").");
                return -1;
            }
            return keuze;
        } catch (NumberFormatException e) {
            IO.println("Ongeldige invoer. Voer een getal in.");
            return -1;
        }
    }


}
