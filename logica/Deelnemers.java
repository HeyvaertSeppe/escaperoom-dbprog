package escaperoom.logica;

public class Deelnemers {
    String voornaam;
    String familienaam;
    String emailadres;
    String telefoonnummer;
    String groepnaam;
    int persoonId;

    public Deelnemers(int persoonId, String voornaam, String familienaam, String emailadres, String telefoonnummer, String groepnaam) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.emailadres = emailadres;
        this.telefoonnummer = telefoonnummer;
        this.groepnaam = groepnaam;
        this.persoonId = persoonId;
    }


    @Override
    public String toString() {
        if(voornaam == null){
            voornaam = "-";
        }
        if(familienaam == null){
            familienaam = "";
        }
        if(telefoonnummer == null){
            telefoonnummer = "-";
        }
        if(emailadres == null){
            emailadres = "-";
        }
        String naam = voornaam + " " + familienaam;


        String uitvoer = persoonId+". "+ naam+"  "+emailadres+"  "+telefoonnummer;
        return uitvoer;

    }
}
