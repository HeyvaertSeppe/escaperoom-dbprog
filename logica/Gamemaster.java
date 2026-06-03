package escaperoom.logica;

public class Gamemaster {
    int id;
    String voornaam;
    String familienaam;
    public Gamemaster(int id, String voornaam, String familienaam) {
        this.familienaam = familienaam;
        this.voornaam = voornaam;
        this.id = id;
    }

    @Override
    public String toString() {
        return voornaam+" "+familienaam;
    }

    public int getId() {
        return id;
    }
}
