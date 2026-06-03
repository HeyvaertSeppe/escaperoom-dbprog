package logica;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Tijdslot {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yy", Locale.ROOT);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT);

    private final int id;
    private final LocalDateTime tijdstip;
    private final BigDecimal prijs;
    private final Integer gamemasterId;
    private final String gamemasterVoornaam;
    private final String gamemasterFamilienaam;
    private final Integer groepId;
    private final String groepNaam;

    public Tijdslot(int id,
                    LocalDateTime tijdstip,
                    BigDecimal prijs,
                    Integer gamemasterId,
                    String gamemasterVoornaam,
                    String gamemasterFamilienaam,
                    Integer groepId,
                    String groepNaam) {
        this.id = id;
        this.tijdstip = tijdstip;
        this.prijs = prijs;
        this.gamemasterId = gamemasterId;
        this.gamemasterVoornaam = gamemasterVoornaam;
        this.gamemasterFamilienaam = gamemasterFamilienaam;
        this.groepId = groepId;
        this.groepNaam = groepNaam;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTijdstip() {
        return tijdstip;
    }

    public BigDecimal getPrijs() {
        return prijs;
    }

    public Integer getGamemasterId() {
        return gamemasterId;
    }

    public Integer getGroepId() {
        return groepId;
    }

    public String formatDateTimeRange() {
        return DATE_FORMAT.format(tijdstip) + " " + TIME_FORMAT.format(tijdstip) + "-" + TIME_FORMAT.format(tijdstip.plusHours(1));
    }

    public String formatPrice() {
        return String.format(Locale.ROOT, "€%.2f", prijs);
    }

    public String formatGamemaster() {
        if (gamemasterVoornaam == null || gamemasterFamilienaam == null) {
            return "-";
        }
        return gamemasterVoornaam + " " + gamemasterFamilienaam;
    }

    public String formatGroep() {
        return groepNaam == null ? "-" : groepNaam;
    }

    public String formatVolledig() {
        return formatDateTimeRange() + " " + formatPrice() + " gm: " + formatGamemaster() + " groep: " + formatGroep();
    }

    public String formatMetPrijs() {
        return formatDateTimeRange() + " " + formatPrice();
    }

    public String formatZonderPrijs() {
        return formatDateTimeRange();
    }
}
