package logica;

public class Gamemaster {
    private final int id;
    private final String voornaam;
    private final String familienaam;

    public Gamemaster(int id, String voornaam, String familienaam) {
        this.id = id;
        this.voornaam = voornaam;
        this.familienaam = familienaam;
    }

    public int getId() {
        return id;
    }

    public String getVolledigeNaam() {
        return voornaam + " " + familienaam;
    }

    @Override
    public String toString() {
        return getVolledigeNaam();
    }
}
