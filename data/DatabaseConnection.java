package escaperoom.data;

import escaperoom.logica.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static Connection con;
    public DatabaseConnection() {
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/escaperoom",
                    "dbprog",
                    "Azerty123!"
            );
            IO.println("Verbinding met databank gemaakt.");
        } catch (SQLException e) {
            IO.println("Verbinding mislukt: " + e.getMessage());
        }
    }

    public static Connection getConnection(String s) {
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/escaperoom",
                    "dbprog",
                    "Azerty123!"
            );
            IO.println("Verbinding met databank gemaakt.");
        } catch (SQLException e) {
            IO.println("Verbinding mislukt: " + e.getMessage());
        }
        return con;
    }

    public List<Tijdslot> geefTijdsloten() {
        List<Tijdslot> lijst = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT tijdsloten.id, tijdstip, prijs, voornaam ,familienaam, naam FROM tijdsloten LEFT JOIN tarieven on tarieven.id = tarief_id LEFT JOIN groepen on tijdsloten.id = tijdslot_id LEFT JOIN gamemasters on persoon_id = gamemaster_id LEFT JOIN personen on persoon_id = personen.id"
            );
            while (rs.next()) {
                lijst.add(new Tijdslot(
                        rs.getInt("id"),
                        rs.getString("tijdstip"),
                        rs.getBigDecimal("prijs"),
                        rs.getString("voornaam"),
                        rs.getString("familienaam"),
                        rs.getString("naam")
                ));
            }
        } catch (SQLException e) {
            IO.println(e.getMessage());
        }
        return lijst;
    }
    public List<Tijdslot> geefTijdsloten_datum(String datum) {
        List<Tijdslot> lijst = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT tijdsloten.id, tijdstip, prijs, voornaam ,familienaam, naam FROM tijdsloten LEFT JOIN tarieven on tarieven.id = tarief_id LEFT JOIN groepen on tijdsloten.id = tijdslot_id LEFT JOIN gamemasters on persoon_id = gamemaster_id LEFT JOIN personen on persoon_id = personen.id WHERE DATE(tijdstip) = '"+datum+"';"
            );
            while (rs.next()) {
                lijst.add(new Tijdslot(
                        rs.getInt("id"),
                        rs.getString("tijdstip"),
                        rs.getBigDecimal("prijs"),
                        rs.getString("voornaam"),
                        rs.getString("familienaam"),
                        rs.getString("naam")
                ));
            }
        } catch (SQLException e) {
            IO.println(e.getMessage());
        }
        return lijst;
    }

    public String voegTijdslotToe(String datum, int tarief_id, String gamemaster_id) {
        String insertSql = "INSERT INTO tijdsloten (tijdstip, tarief_id, gamemaster_id) VALUES (?, ?, ?)";
        String selectSql = "SELECT t.id, ta.prijs FROM tijdsloten t LEFT JOIN tarieven ta ON t.tarief_id = ta.id WHERE t.id = ?";

        try {

            PreparedStatement pstmtInsert = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            pstmtInsert.setString(1, datum);
            pstmtInsert.setInt(2, tarief_id);

            if (gamemaster_id == null || gamemaster_id.isEmpty()) {
                pstmtInsert.setNull(3, java.sql.Types.INTEGER);
            } else {
                pstmtInsert.setInt(3, Integer.parseInt(gamemaster_id));
            }

            int rowsAffected = pstmtInsert.executeUpdate();
            if (rowsAffected == 0) {
                return "Tijdslot aanmaken mislukt.";
            }

            int gegenereerdeId = 0;
            try (ResultSet generatedKeys = pstmtInsert.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    gegenereerdeId = generatedKeys.getInt(1);
                } else {
                    return "Tijdslot aangemaakt, maar ID kon niet worden opgehaald.";
                }
            }

            PreparedStatement pstmtSelect = con.prepareStatement(selectSql);
            pstmtSelect.setInt(1, gegenereerdeId);
            ResultSet rs = pstmtSelect.executeQuery();

            BigDecimal prijs = BigDecimal.ZERO;
            if (rs.next()) {
                prijs = rs.getBigDecimal("prijs");
            }

            String jaar = datum.substring(2, 4);
            String maand = datum.substring(5, 7);
            String dag = datum.substring(8, 10);
            String tijd = datum.substring(11, datum.length()-3);

            LocalTime startTijd = LocalTime.parse(tijd);
            LocalTime eindTijd = startTijd.plusHours(1);
            String datum_reform = dag + "/" + maand + "/" + jaar + "  " + tijd + "-" + eindTijd;

            return "Tijdslot aangemaakt (id " + gegenereerdeId + "): " + datum_reform + "  €" + prijs;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate entry")) {
                String errorMsg = "Tijdstip bestaat al!";
                IO.println(errorMsg);
                return errorMsg;
            } else {
                IO.println("Database fout: " + e.getMessage());
                return e.getMessage();
            }
        }
    }

    public List<Groep> geefGroepen() {
        List<Groep> lijst = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT groepen.id, naam, count(persoon_id), tijdstip FROM deelnemers LEFT JOIN groepen on groep_id = groepen.id LEFT JOIN personen on persoon_id = personen.id LEFT JOIN tijdsloten on tijdslot_id = tijdsloten.id GROUP BY naam ,tijdstip, groepen.id"
            );
            while (rs.next()) {
                lijst.add(new Groep(
                        rs.getInt("id"),
                        rs.getString("naam"),
                        rs.getInt("count(persoon_id)"),
                        rs.getString("tijdstip")
                ));
            }
        } catch (SQLException e) {
            IO.println(e.getMessage());
        }
        return lijst;
    }

    public List<Deelnemers> geefDeelnemers(int groep_id) {
        List<Deelnemers> lijst = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT personen.id,voornaam,familienaam,emailadres,telefoonnummer,naam FROM deelnemers LEFT JOIN personen on persoon_id = personen.id LEFT JOIN groepen on groep_id = groepen.id WHERE groep_id = "+groep_id
            );
            while (rs.next()) {
                lijst.add(new Deelnemers(
                        rs.getInt("id"),
                        rs.getString("voornaam"),
                        rs.getString("familienaam"),
                        rs.getString("emailadres"),
                        rs.getString("telefoonnummer"),
                        rs.getString("naam")
                ));
            }
        } catch (SQLException e) {
            IO.println(e.getMessage());
        }
        return lijst;
    }

    public String getGroepNaamByID(int id) {
        String groepNaam = "-";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT naam FROM groepen WHERE groepen.id = " + id);

            if (rs.next()) {
                groepNaam = rs.getString("naam");
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            IO.println(e.getMessage());
        }

        return groepNaam;
    }
    public String getGamemasterById(int id) {
        String naam = "-";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT voornaam,familienaam FROM personen INNER JOIN gamemasters on personen.id = persoon_id WHERE persoon_id =" + id);

            if (rs.next()) {
                naam = rs.getString("voornaam")+" "+ rs.getString("familienaam");
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            IO.println(e.getMessage());
        }

        return naam;
    }

    public List<Tarief> getTarieven() {
        List<Tarief> lijst = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM tarieven"
            );
            while (rs.next()) {
                lijst.add(new Tarief(
                        rs.getInt("id"),
                        rs.getString("categorie"),
                        rs.getBigDecimal("prijs")
                ));
            }
        } catch (SQLException e) {
            IO.println(e.getMessage());
        }
        return lijst;
    }

    public List<Gamemaster> getGamemasters() {
        List<Gamemaster> lijst = new ArrayList<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT persoon_id,voornaam,familienaam FROM gamemasters INNER JOIN personen on personen.id = persoon_id"
            );
            while (rs.next()) {
                lijst.add(new Gamemaster(
                        rs.getInt("persoon_id"),
                        rs.getString("voornaam"),
                        rs.getString("familienaam")
                ));
            }
        } catch (SQLException e) {
            IO.println("check seq:"+e.getMessage());
        }
        return lijst;
    }
    public void setGamemaster(int gamemasterId, int tijdstipId){
        try {
            Statement stmt = con.createStatement();
            int rs = stmt.executeUpdate(
                    "UPDATE tijdsloten SET gamemaster_id=1 WHERE id=26"
            );

        } catch (SQLException e) {
            IO.println("check seq:"+e.getMessage());
        }

    }
    public String getTijdslotById(int id) {
        String tijdstip = "-";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT tijdstip FROM tijdsloten WHERE id =" + id);

            if (rs.next()) {
                tijdstip = rs.getString("tijdstip");
            }

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            IO.println(e.getMessage());
        }
        String jaar = tijdstip.substring(2, 4);
        String maand = tijdstip.substring(5, 7);
        String dag = tijdstip.substring(8, 10);

        String tijd = tijdstip.substring(11,tijdstip.length()-3);
        LocalTime startTijd = LocalTime.parse(tijd);
        LocalTime eindTijd = startTijd.plusHours(1);

        String datum = dag + "/" + maand + "/" + jaar+"  "+startTijd+"-"+eindTijd;

        return datum;
    }
}