package logica;

public class Deelnemers {
    private final int persoonId;
    private final String voornaam;
    private final String familienaam;
    private final String emailadres;
    private final String telefoonnummer;

    public Deelnemers(int persoonId, String voornaam, String familienaam, String emailadres, String telefoonnummer) {
        this.persoonId = persoonId;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.emailadres = emailadres;
        this.telefoonnummer = telefoonnummer;
    }

    public int getPersoonId() {
        return persoonId;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public String getEmailadres() {
        return emailadres;
    }

    public String getTelefoonnummer() {
        return telefoonnummer;
    }

    public String formatLijn() {
        return "- " + voornaam + " " + familienaam + " " + emailadres + " " + (telefoonnummer == null || telefoonnummer.isBlank() ? "-" : telefoonnummer);
    }
}
