package presentatie;

import data.DatabaseConnection;
import logica.Deelnemers;
import logica.Gamemaster;
import logica.Groep;
import logica.Tarief;
import logica.Tijdslot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class Helper {
    private static final DateTimeFormatter DATUM_INPUT = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ROOT);
    private static final DateTimeFormatter UUR_INPUT = DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT);
    private static final DatabaseConnection db = new DatabaseConnection();

    private Helper() {
    }

    public static void toonAlleTijdslots() {
        List<Tijdslot> tijdslots = db.geefAlleTijdslots();
        for (Tijdslot tijdslot : tijdslots) {
            IO.println(tijdslot.formatVolledig());
        }
    }

    public static void toonTijdslotsOpDatum() {
        LocalDate datum = vraagDatum("Datum (dd/MM/yy): ");
        if (datum == null) {
            return;
        }
        List<Tijdslot> tijdslots = db.geefTijdslotsOpDatum(datum);
        for (Tijdslot tijdslot : tijdslots) {
            IO.println(tijdslot.formatVolledig());
        }
    }

    public static void voegTijdslotToe() {
        LocalDate datum = vraagDatum("Datum (dd/MM/yy): ");
        if (datum == null) {
            return;
        }

        LocalTime startuur = vraagUur("Startuur (HH:mm): ");
        if (startuur == null) {
            return;
        }

        IO.println("Kies een tarief:");
        List<Tarief> tarieven = db.geefTarieven();
        for (Tarief tarief : tarieven) {
            IO.println("\t" + tarief);
        }

        int tariefIndex = leesKeuzeBereik("Keuze (1-" + tarieven.size() + "): ", 1, tarieven.size());
        if (tariefIndex == -1) {
            return;
        }

        Integer gamemasterId = null;
        String gamemasterToewijzen = IO.readln("Gamemaster toewijzen? (j/n): ");
        if ("j".equalsIgnoreCase(gamemasterToewijzen)) {
            List<Gamemaster> gamemasters = db.geefGamemasters();
            IO.println("Kies een gamemaster:");
            for (int i = 0; i < gamemasters.size(); i++) {
                IO.println("\t" + (i + 1) + ". " + gamemasters.get(i).getVolledigeNaam());
            }
            int gmIndex = leesKeuzeBereik("Keuze (1-" + gamemasters.size() + "): ", 1, gamemasters.size());
            if (gmIndex == -1) {
                return;
            }
            gamemasterId = gamemasters.get(gmIndex - 1).getId();
        }

        Tijdslot nieuw = db.voegTijdslotToe(datum, startuur, tarieven.get(tariefIndex - 1).getId(), gamemasterId);
        IO.println("Tijdslot aangemaakt (id " + nieuw.getId() + "): " + nieuw.formatMetPrijs());

        if (gamemasterId != null) {
            IO.println("Gamemaster " + nieuw.formatGamemaster() + " toegewezen");
        }
    }

    public static void wijsGamemasterToe() {
        List<Tijdslot> tijdslots = db.geefVrijeTijdslotsZonderGamemaster();
        if (tijdslots.isEmpty()) {
            IO.println("Geen vrije tijdslots zonder gamemaster.");
            return;
        }

        IO.println("Kies een tijdslot:");
        for (int i = 0; i < tijdslots.size(); i++) {
            IO.println("\t" + (i + 1) + ". " + tijdslots.get(i).formatZonderPrijs());
        }

        int tijdslotIndex = leesKeuzeBereik("Keuze (1-" + tijdslots.size() + "): ", 1, tijdslots.size());
        if (tijdslotIndex == -1) {
            return;
        }

        List<Gamemaster> gamemasters = db.geefGamemasters();
        IO.println("Kies een gamemaster:");
        for (int i = 0; i < gamemasters.size(); i++) {
            IO.println("\t" + (i + 1) + ". " + gamemasters.get(i).getVolledigeNaam());
        }

        int gmIndex = leesKeuzeBereik("Keuze (1-" + gamemasters.size() + "): ", 1, gamemasters.size());
        if (gmIndex == -1) {
            return;
        }

        Tijdslot gekozenTijdslot = tijdslots.get(tijdslotIndex - 1);
        Gamemaster gekozenGamemaster = gamemasters.get(gmIndex - 1);
        db.wijsGamemasterToe(gekozenTijdslot.getId(), gekozenGamemaster.getId());

        IO.println("Gamemaster " + gekozenGamemaster.getVolledigeNaam() + " toegewezen aan " + gekozenTijdslot.formatZonderPrijs());
    }

    public static void toonAlleGroepen() {
        List<Groep> groepen = db.geefGroepen();
        for (Groep groep : groepen) {
            IO.println(groep.formatZonderIndex());
        }
    }

    public static void toonDeelnemersVanGroep() {
        List<Groep> groepen = db.geefGroepen();
        for (int i = 0; i < groepen.size(); i++) {
            IO.println((i + 1) + ". " + groepen.get(i).formatZonderIndex());
        }

        int index = leesKeuzeBereik("Keuze (1-" + groepen.size() + "): ", 1, groepen.size());
        if (index == -1) {
            return;
        }

        Groep gekozenGroep = groepen.get(index - 1);
        IO.println("Deelnemers van " + gekozenGroep.getNaam() + ":");
        List<Deelnemers> deelnemers = db.geefDeelnemersVanGroep(gekozenGroep.getId());
        for (Deelnemers deelnemer : deelnemers) {
            IO.println(deelnemer.formatLijn());
        }
    }

    public static void maakNieuweGroep() {
        String naam = IO.readln("Naam: ");
        String wachtwoord = IO.readln("Wachtwoord: ");

        IO.println("Kies een taal:");
        IO.println("\t1. NL");
        IO.println("\t2. FR");
        IO.println("\t3. EN");
        IO.println("\t4. DE");
        int taalKeuze = leesKeuzeBereik("Keuze (1-4): ", 1, 4);
        if (taalKeuze == -1) {
            return;
        }

        String taal = switch (taalKeuze) {
            case 1 -> "NL";
            case 2 -> "FR";
            case 3 -> "EN";
            default -> "DE";
        };

        int groepId = db.maakGroepAan(naam, wachtwoord, taal);
        int aantal = 0;

        while ("j".equalsIgnoreCase(IO.readln("Deelnemer toevoegen? (j/n): "))) {
            String voornaam = IO.readln("Voornaam: ");
            String familienaam = IO.readln("Familienaam: ");
            String email = IO.readln("E-mail: ");
            String telefoon = IO.readln("Telefoonnummer (optioneel): ");

            int persoonId = db.voegPersoonEnDeelnemerToe(groepId, voornaam, familienaam, email, telefoon);
            String telefoonWeergave = telefoon == null || telefoon.isBlank() ? "-" : telefoon;
            IO.println("Deelnemer " + voornaam + " " + familienaam + " " + email + " " + telefoonWeergave + " toegevoegd (id " + persoonId + ").");
            aantal++;
        }

        IO.println("Groep aangemaakt (id " + groepId + "): " + naam + " (" + aantal + " deelnemers) -");
    }

    public static void toonBeschikbareTijdslotsKomendeMaand() {
        List<Tijdslot> tijdslots = db.geefBeschikbareTijdslotsKomendeMaand();
        for (Tijdslot tijdslot : tijdslots) {
            IO.println(tijdslot.formatMetPrijs());
        }
    }

    public static void maakNieuweReservatie() {
        List<Groep> groepen = db.geefGroepenZonderReservatie();
        if (groepen.isEmpty()) {
            IO.println("Geen groepen zonder reservatie beschikbaar.");
            return;
        }

        List<Tijdslot> tijdslots = db.geefBeschikbareTijdslotsKomendeMaand();
        if (tijdslots.isEmpty()) {
            IO.println("Geen beschikbare tijdslots in de komende maand.");
            return;
        }

        IO.println("Kies een groep:");
        for (int i = 0; i < groepen.size(); i++) {
            IO.println("\t" + (i + 1) + ". " + groepen.get(i).formatZonderIndex());
        }
        int groepIndex = leesKeuzeBereik("Keuze (1-" + groepen.size() + "): ", 1, groepen.size());
        if (groepIndex == -1) {
            return;
        }

        IO.println("Kies een tijdslot:");
        for (int i = 0; i < tijdslots.size(); i++) {
            IO.println("\t" + (i + 1) + ". " + tijdslots.get(i).formatMetPrijs());
        }
        int tijdslotIndex = leesKeuzeBereik("Keuze (1-" + tijdslots.size() + "): ", 1, tijdslots.size());
        if (tijdslotIndex == -1) {
            return;
        }

        Groep gekozenGroep = groepen.get(groepIndex - 1);
        Tijdslot gekozenTijdslot = tijdslots.get(tijdslotIndex - 1);
        db.maakReservatie(gekozenGroep.getId(), gekozenTijdslot.getId());

        IO.println("Reservatie aangemaakt: " + gekozenGroep.getNaam() + " → " + gekozenTijdslot.formatZonderPrijs());
    }

    public static void toonReservatiesOpDatum() {
        LocalDate datum = vraagDatum("Datum (dd/MM/yy): ");
        if (datum == null) {
            return;
        }

        List<Tijdslot> tijdslots = db.geefTijdslotsOpDatum(datum);
        for (Tijdslot tijdslot : tijdslots) {
            if (tijdslot.getGroepId() != null) {
                IO.println(tijdslot.formatVolledig());
            }
        }
    }

    private static LocalDate vraagDatum(String prompt) {
        try {
            return LocalDate.parse(IO.readln(prompt), DATUM_INPUT);
        } catch (DateTimeParseException ex) {
            IO.println("Ongeldige datum.");
            return null;
        }
    }

    private static LocalTime vraagUur(String prompt) {
        try {
            return LocalTime.parse(IO.readln(prompt), UUR_INPUT);
        } catch (DateTimeParseException ex) {
            IO.println("Ongeldig uur.");
            return null;
        }
    }

    private static int leesKeuzeBereik(String prompt, int min, int max) {
        try {
            int keuze = Integer.parseInt(IO.readln(prompt));
            if (keuze < min || keuze > max) {
                IO.println("Ongeldige keuze.");
                return -1;
            }
            return keuze;
        } catch (NumberFormatException ex) {
            IO.println("Ongeldige keuze.");
            return -1;
        }
    }
}
