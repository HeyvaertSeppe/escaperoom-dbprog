package escaperoom.logica;

import java.time.LocalTime;

public class Groep {
    String naam;
    int deelnemers;
    String tijdstip;
    int groepId;


    public Groep(int groepId, String naam, int deelnemers, String tijdstip) {
        this.groepId = groepId;
        this.naam = naam;
        this.deelnemers = deelnemers;
        this.tijdstip = tijdstip;
    }


    @Override
    public String toString() {
        String datum;
        if(naam == null){
            naam = "-";
        }
        if(tijdstip == null){
            tijdstip = "-";
            datum = tijdstip;
        }
        else{
            String jaar = tijdstip.substring(2, 4);
            String maand = tijdstip.substring(5, 7);
            String dag = tijdstip.substring(8, 10);


            String tijd = tijdstip.substring(11,tijdstip.length()-3);
            LocalTime startTijd = LocalTime.parse(tijd);
            LocalTime eindTijd = startTijd.plusHours(1);

            datum = dag + "/" + maand + "/" + jaar+"  "+tijd+"-"+eindTijd;

        }
        String uitvoer = groepId+". "+ naam +"  ("+deelnemers+" deelnemers)  "+datum;
        return uitvoer;
    }
}
