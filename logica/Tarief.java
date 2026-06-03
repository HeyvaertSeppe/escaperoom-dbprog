package escaperoom.logica;

import java.math.BigDecimal;

public class Tarief {

    int id;
    String  categorie;
    BigDecimal prijs;

    public Tarief(int id, String categorie, BigDecimal prijs) {
        this.id = id;
        this.categorie = categorie;
        this.prijs = prijs;
    }

    @Override
    public String toString() {
        return id+". "+categorie+" - €"+prijs;
    }
}
