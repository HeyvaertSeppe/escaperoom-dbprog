package logica;

import java.math.BigDecimal;
import java.util.Locale;

public class Tarief {
    private final int id;
    private final String categorie;
    private final BigDecimal prijs;

    public Tarief(int id, String categorie, BigDecimal prijs) {
        this.id = id;
        this.categorie = categorie;
        this.prijs = prijs;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getPrijs() {
        return prijs;
    }

    @Override
    public String toString() {
        return id + ". " + categorie + " – " + String.format(Locale.ROOT, "€%.2f", prijs);
    }
}
