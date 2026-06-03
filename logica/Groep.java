package logica;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Groep {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ROOT);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT);

    private final int id;
    private final String naam;
    private final int aantalDeelnemers;
    private final LocalDateTime tijdstip;

    public Groep(int id, String naam, int aantalDeelnemers, LocalDateTime tijdstip) {
        this.id = id;
        this.naam = naam;
        this.aantalDeelnemers = aantalDeelnemers;
        this.tijdstip = tijdstip;
    }

    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public int getAantalDeelnemers() {
        return aantalDeelnemers;
    }

    public String formatZonderIndex() {
        return naam + " (" + aantalDeelnemers + " deelnemers) " + formatTijdslot();
    }

    public String formatTijdslot() {
        if (tijdstip == null) {
            return "-";
        }
        return DATE_FORMAT.format(tijdstip) + " " + TIME_FORMAT.format(tijdstip) + "-" + TIME_FORMAT.format(tijdstip.plusHours(1));
    }
}
