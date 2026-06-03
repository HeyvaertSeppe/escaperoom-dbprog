package data;

import logica.Deelnemers;
import logica.Gamemaster;
import logica.Groep;
import logica.Tarief;
import logica.Tijdslot;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/escaperoom";
    private static final String USER = "dbprog";
    private static final String PASSWORD = "Azerty123!";

    private static Connection connection;

    private static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    private Tijdslot mapTijdslot(ResultSet rs) throws SQLException {
        Timestamp tijdstip = rs.getTimestamp("tijdstip");
        return new Tijdslot(
                rs.getInt("id"),
                tijdstip.toLocalDateTime(),
                rs.getBigDecimal("prijs"),
                (Integer) rs.getObject("gamemaster_id"),
                rs.getString("voornaam"),
                rs.getString("familienaam"),
                (Integer) rs.getObject("groep_id"),
                rs.getString("groep_naam")
        );
    }

    public List<Tijdslot> geefAlleTijdslots() {
        String sql = """
                SELECT ts.id,
                       ts.tijdstip,
                       t.prijs,
                       ts.gamemaster_id,
                       gm_p.voornaam,
                       gm_p.familienaam,
                       g.id AS groep_id,
                       g.naam AS groep_naam
                FROM tijdsloten ts
                INNER JOIN tarieven t ON t.id = ts.tarief_id
                LEFT JOIN gamemasters gm ON gm.persoon_id = ts.gamemaster_id
                LEFT JOIN personen gm_p ON gm_p.id = gm.persoon_id
                LEFT JOIN groepen g ON g.tijdslot_id = ts.id
                ORDER BY ts.tijdstip
                """;
        List<Tijdslot> lijst = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lijst.add(mapTijdslot(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij ophalen van tijdslots", e);
        }
        return lijst;
    }

    public List<Tijdslot> geefTijdslotsOpDatum(LocalDate datum) {
        String sql = """
                SELECT ts.id,
                       ts.tijdstip,
                       t.prijs,
                       ts.gamemaster_id,
                       gm_p.voornaam,
                       gm_p.familienaam,
                       g.id AS groep_id,
                       g.naam AS groep_naam
                FROM tijdsloten ts
                INNER JOIN tarieven t ON t.id = ts.tarief_id
                LEFT JOIN gamemasters gm ON gm.persoon_id = ts.gamemaster_id
                LEFT JOIN personen gm_p ON gm_p.id = gm.persoon_id
                LEFT JOIN groepen g ON g.tijdslot_id = ts.id
                WHERE DATE(ts.tijdstip) = ?
                ORDER BY ts.tijdstip
                """;
        List<Tijdslot> lijst = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(datum));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lijst.add(mapTijdslot(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij ophalen van tijdslots op datum", e);
        }
        return lijst;
    }

    public Tijdslot voegTijdslotToe(LocalDate datum, LocalTime startuur, int tariefId, Integer gamemasterId) {
        String insertSql = "INSERT INTO tijdsloten(tijdstip, tarief_id, gamemaster_id) VALUES(?, ?, ?)";
        String selectSql = """
                SELECT ts.id,
                       ts.tijdstip,
                       t.prijs,
                       ts.gamemaster_id,
                       gm_p.voornaam,
                       gm_p.familienaam,
                       g.id AS groep_id,
                       g.naam AS groep_naam
                FROM tijdsloten ts
                INNER JOIN tarieven t ON t.id = ts.tarief_id
                LEFT JOIN gamemasters gm ON gm.persoon_id = ts.gamemaster_id
                LEFT JOIN personen gm_p ON gm_p.id = gm.persoon_id
                LEFT JOIN groepen g ON g.tijdslot_id = ts.id
                WHERE ts.id = ?
                """;

        try (PreparedStatement insert = getConnection().prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insert.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(datum, startuur)));
            insert.setInt(2, tariefId);
            if (gamemasterId == null) {
                insert.setNull(3, java.sql.Types.INTEGER);
            } else {
                insert.setInt(3, gamemasterId);
            }
            insert.executeUpdate();

            int id;
            try (ResultSet keys = insert.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new RuntimeException("Geen id teruggekregen bij tijdslot insert");
                }
                id = keys.getInt(1);
            }

            try (PreparedStatement select = getConnection().prepareStatement(selectSql)) {
                select.setInt(1, id);
                try (ResultSet rs = select.executeQuery()) {
                    if (rs.next()) {
                        return mapTijdslot(rs);
                    }
                }
            }
            throw new RuntimeException("Tijdslot aangemaakt maar niet teruggevonden");
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij toevoegen van tijdslot", e);
        }
    }

    public List<Tijdslot> geefVrijeTijdslotsZonderGamemaster() {
        String sql = """
                SELECT ts.id,
                       ts.tijdstip,
                       t.prijs,
                       ts.gamemaster_id,
                       gm_p.voornaam,
                       gm_p.familienaam,
                       g.id AS groep_id,
                       g.naam AS groep_naam
                FROM tijdsloten ts
                INNER JOIN tarieven t ON t.id = ts.tarief_id
                LEFT JOIN gamemasters gm ON gm.persoon_id = ts.gamemaster_id
                LEFT JOIN personen gm_p ON gm_p.id = gm.persoon_id
                LEFT JOIN groepen g ON g.tijdslot_id = ts.id
                WHERE ts.gamemaster_id IS NULL
                  AND g.id IS NULL
                ORDER BY ts.tijdstip
                """;
        List<Tijdslot> lijst = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lijst.add(mapTijdslot(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij ophalen van vrije tijdslots", e);
        }
        return lijst;
    }

    public void wijsGamemasterToe(int tijdslotId, int gamemasterId) {
        String sql = "UPDATE tijdsloten SET gamemaster_id = ? WHERE id = ? AND gamemaster_id IS NULL";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, gamemasterId);
            ps.setInt(2, tijdslotId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij toewijzen gamemaster", e);
        }
    }

    public List<Tarief> geefTarieven() {
        String sql = "SELECT id, categorie, prijs FROM tarieven ORDER BY id";
        List<Tarief> lijst = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lijst.add(new Tarief(rs.getInt("id"), rs.getString("categorie"), rs.getBigDecimal("prijs")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij ophalen van tarieven", e);
        }
        return lijst;
    }

    public List<Gamemaster> geefGamemasters() {
        String sql = """
                SELECT gm.persoon_id, p.voornaam, p.familienaam
                FROM gamemasters gm
                INNER JOIN personen p ON p.id = gm.persoon_id
                ORDER BY gm.persoon_id
                """;
        List<Gamemaster> lijst = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lijst.add(new Gamemaster(rs.getInt("persoon_id"), rs.getString("voornaam"), rs.getString("familienaam")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij ophalen van gamemasters", e);
        }
        return lijst;
    }

    public List<Groep> geefGroepen() {
        String sql = """
                SELECT g.id,
                       g.naam,
                       COUNT(d.persoon_id) AS aantal,
                       ts.tijdstip
                FROM groepen g
                LEFT JOIN deelnemers d ON d.groep_id = g.id
                LEFT JOIN tijdsloten ts ON ts.id = g.tijdslot_id
                GROUP BY g.id, g.naam, ts.tijdstip
                ORDER BY g.id
                """;
        List<Groep> lijst = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp tijdstip = rs.getTimestamp("tijdstip");
                lijst.add(new Groep(
                        rs.getInt("id"),
                        rs.getString("naam"),
                        rs.getInt("aantal"),
                        tijdstip == null ? null : tijdstip.toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij ophalen van groepen", e);
        }
        return lijst;
    }

    public List<Deelnemers> geefDeelnemersVanGroep(int groepId) {
        String sql = """
                SELECT p.id, p.voornaam, p.familienaam, p.emailadres, p.telefoonnummer
                FROM deelnemers d
                INNER JOIN personen p ON p.id = d.persoon_id
                WHERE d.groep_id = ?
                ORDER BY p.id
                """;
        List<Deelnemers> lijst = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, groepId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lijst.add(new Deelnemers(
                            rs.getInt("id"),
                            rs.getString("voornaam"),
                            rs.getString("familienaam"),
                            rs.getString("emailadres"),
                            rs.getString("telefoonnummer")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij ophalen van deelnemers", e);
        }
        return lijst;
    }

    public int maakGroepAan(String naam, String wachtwoord, String taal) {
        String sql = "INSERT INTO groepen(naam, wachtwoord, taal) VALUES(?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, naam);
            ps.setString(2, wachtwoord);
            ps.setString(3, taal);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new RuntimeException("Geen id teruggekregen voor groep");
                }
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij maken van groep", e);
        }
    }

    public int voegPersoonEnDeelnemerToe(int groepId, String voornaam, String familienaam, String email, String telefoon) {
        String persoonSql = "INSERT INTO personen(voornaam, familienaam, emailadres, telefoonnummer) VALUES(?, ?, ?, ?)";
        String deelnemerSql = "INSERT INTO deelnemers(groep_id, persoon_id) VALUES(?, ?)";

        try {
            Connection con = getConnection();
            boolean autoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try (PreparedStatement persoonPs = con.prepareStatement(persoonSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement deelnemerPs = con.prepareStatement(deelnemerSql)) {
                persoonPs.setString(1, voornaam);
                persoonPs.setString(2, familienaam);
                persoonPs.setString(3, email);
                if (telefoon == null || telefoon.isBlank()) {
                    persoonPs.setNull(4, java.sql.Types.VARCHAR);
                } else {
                    persoonPs.setString(4, telefoon);
                }
                persoonPs.executeUpdate();
                int persoonId;
                try (ResultSet keys = persoonPs.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new RuntimeException("Geen id teruggekregen voor deelnemer");
                    }
                    persoonId = keys.getInt(1);
                }

                deelnemerPs.setInt(1, groepId);
                deelnemerPs.setInt(2, persoonId);
                deelnemerPs.executeUpdate();

                con.commit();
                con.setAutoCommit(autoCommit);
                return persoonId;
            } catch (Exception e) {
                con.rollback();
                con.setAutoCommit(autoCommit);
                throw e;
            }
        } catch (Exception e) {
            throw new RuntimeException("Fout bij toevoegen van deelnemer", e);
        }
    }

    public List<Groep> geefGroepenZonderReservatie() {
        String sql = """
                SELECT g.id,
                       g.naam,
                       COUNT(d.persoon_id) AS aantal,
                       ts.tijdstip
                FROM groepen g
                LEFT JOIN deelnemers d ON d.groep_id = g.id
                LEFT JOIN tijdsloten ts ON ts.id = g.tijdslot_id
                WHERE g.tijdslot_id IS NULL
                GROUP BY g.id, g.naam, ts.tijdstip
                ORDER BY g.id
                """;
        List<Groep> lijst = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp tijdstip = rs.getTimestamp("tijdstip");
                lijst.add(new Groep(
                        rs.getInt("id"),
                        rs.getString("naam"),
                        rs.getInt("aantal"),
                        tijdstip == null ? null : tijdstip.toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij ophalen van groepen zonder reservatie", e);
        }
        return lijst;
    }

    public List<Tijdslot> geefBeschikbareTijdslotsKomendeMaand() {
        String sql = """
                SELECT ts.id,
                       ts.tijdstip,
                       t.prijs,
                       ts.gamemaster_id,
                       gm_p.voornaam,
                       gm_p.familienaam,
                       g.id AS groep_id,
                       g.naam AS groep_naam
                FROM tijdsloten ts
                INNER JOIN tarieven t ON t.id = ts.tarief_id
                LEFT JOIN gamemasters gm ON gm.persoon_id = ts.gamemaster_id
                LEFT JOIN personen gm_p ON gm_p.id = gm.persoon_id
                LEFT JOIN groepen g ON g.tijdslot_id = ts.id
                WHERE ts.gamemaster_id IS NOT NULL
                  AND g.id IS NULL
                  AND DATE(ts.tijdstip) >= CURDATE()
                  AND DATE(ts.tijdstip) < DATE_ADD(CURDATE(), INTERVAL 1 MONTH)
                ORDER BY ts.tijdstip
                """;
        List<Tijdslot> lijst = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lijst.add(mapTijdslot(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij ophalen van beschikbare tijdslots", e);
        }
        return lijst;
    }

    public void maakReservatie(int groepId, int tijdslotId) {
        String sql = "UPDATE groepen SET tijdslot_id = ? WHERE id = ? AND tijdslot_id IS NULL";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, tijdslotId);
            ps.setInt(2, groepId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fout bij maken van reservatie", e);
        }
    }
}
