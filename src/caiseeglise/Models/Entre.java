package caiseeglise.Models;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class Entre extends Table {

    public Entre(Connection conn) {
        super(
            conn,
            "ENTRE",
            Arrays.asList("identre", "motif", "montantEntre", "dateEntre", "ideglise"),
            "identre"
        );
    }

    @Override
    protected String getEntityLabel() {
        return "Entrée";
    }

public String insertEntre(String motif, int montantEntre, String dateEntre, String ideglise) {
    String sql = "INSERT INTO ENTRE (motif, montantEntre, dateEntre, ideglise) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, motif);
        stmt.setInt(2, montantEntre);
        stmt.setString(3, dateEntre);
        stmt.setString(4, ideglise);

        int rows = stmt.executeUpdate();
        if (rows == 1) {
            return "INSERT_OK";
        } else {
            return "INSERT_ERROR: Aucune ligne insérée.";
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return "INSERT_EXCEPTION: " + e.getMessage();
    }
}

//entre
public List<Map<String, Object>> afficheEntres() throws SQLException {
    List<Map<String, Object>> results = new ArrayList<>();

    String sql = "SELECT * FROM " + nameTable + " ORDER BY " + primaryKey + " ASC";

    try (PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(meta.getColumnName(i), rs.getObject(i));
            }
            results.add(row);
        }
    }

    return results;
}
//fonction recherche
public List<Map<String, Object>> rechercherParMotif(String motifRecherche) throws SQLException {
    List<Map<String, Object>> results = new ArrayList<>();

    String sql = "SELECT * FROM " + nameTable + " WHERE motif LIKE ? ORDER BY " + primaryKey + " ASC";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, "%" + motifRecherche + "%");

        try (ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        }
    }

    return results;
}

//fontion 

public List<Map<String, Object>> rechercherParDates(Date dateA, Date dateB, String idEglise) throws SQLException {
    List<Map<String, Object>> results = new ArrayList<>();

    String sql = "SELECT dateEntre, motif, montantEntre FROM " + nameTable + 
                 " WHERE dateEntre BETWEEN ? AND ? AND ideglise = ? ORDER BY dateEntre ASC";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setDate(1, new java.sql.Date(dateA.getTime()));
        stmt.setDate(2, new java.sql.Date(dateB.getTime()));
        stmt.setString(3, idEglise);  // 🔁 correction ici

        try (ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }

                row.put("typeMouvement", "Entrée");
                results.add(row);
            }
        }
    }

    return results;
}

    
    //fonction entre 

}
