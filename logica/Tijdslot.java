package escaperoom.logica;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Tijdslot {
    String tijdstip;
    BigDecimal prijs;
    String voornaam;
    String familienaam;
    String groepnaam;
    int tijdslotId;
    int tijdslotIdOrder;



    public Tijdslot(int tijdslotId, String tijdstip, BigDecimal prijs, String voornaam, String familienaam, String naam) {
        this.prijs = prijs;
        this.tijdstip = tijdstip;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.groepnaam = naam;
        this.tijdslotId = tijdslotId;
    }

    public String getGamemasterName()           { return voornaam; }
    public int getTijdstipId(){ return tijdslotId;}
    public void setTijdslotId(int tijdslotIdOrder){ this.tijdslotIdOrder = tijdslotIdOrder;}


    @Override
    public String toString() {
        if(voornaam == null){
            voornaam = "-";
        }
        if(familienaam == null){
            familienaam = "";
        }
        if(groepnaam == null){
            groepnaam = "-";
        }
        String jaar = tijdstip.substring(2, 4);
        String maand = tijdstip.substring(5, 7);
        String dag = tijdstip.substring(8, 10);

        String tijd = tijdstip.substring(11,tijdstip.length()-3);
        LocalTime startTijd = LocalTime.parse(tijd);
        LocalTime eindTijd = startTijd.plusHours(1);

        String datum = dag + "/" + maand + "/" + jaar+"  "+tijd+"-"+eindTijd;

        String gm_naam = voornaam + " " + familienaam;
        String uitvoer = datum + "  €"+prijs+"  gm: "+gm_naam+"  groep: "+groepnaam;
        return uitvoer;
    }

    public int getId() {
        return tijdslotId;
    }

    public String toStringSimple() {
        String jaar = tijdstip.substring(2, 4);
        String maand = tijdstip.substring(5, 7);
        String dag = tijdstip.substring(8, 10);

        String tijd = tijdstip.substring(11,tijdstip.length()-3);
        LocalTime startTijd = LocalTime.parse(tijd);
        LocalTime eindTijd = startTijd.plusHours(1);

        String datum = dag + "/" + maand + "/" + jaar+"  "+tijd+"-"+eindTijd;


        return datum;
    }

}