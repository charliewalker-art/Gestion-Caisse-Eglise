package caiseeglise.Models;

import java.sql.*;
import java.util.*;

public class Sortie extends Table {

    public Sortie(Connection conn) {
        super(
            conn,
            "SORTIE",
            Arrays.asList("idsortie", "ideglise", "motif", "montantSortie", "dateSortie"),
            "idsortie"
        );
    }

    @Override
    protected String getEntityLabel() {
        return "Sortie";
    }

    public String insertSortie(String motif, int montantSortie, String dateSortie, String ideglise) {
    String sqlInsert = "INSERT INTO SORTIE (motif, montantSortie, dateSortie, ideglise) VALUES (?, ?, ?, ?)";
    String sqlUpdateSolde = "UPDATE EGLISE SET Solde = COALESCE(Solde, 0) - ? WHERE ideglise = ?";

    try {
        conn.setAutoCommit(false);  // Démarre la transaction

        // 1) Insertion dans SORTIE
        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
            stmtInsert.setString(1, motif);
            stmtInsert.setInt(2, montantSortie);
            stmtInsert.setString(3, dateSortie);
            stmtInsert.setString(4, ideglise);

            int rowsInsert = stmtInsert.executeUpdate();
            if (rowsInsert != 1) {
                conn.rollback();
                conn.setAutoCommit(true);
                return "INSERT_ERROR: Aucune ligne insérée dans SORTIE.";
            }
        }

        // 2) Mise à jour du solde
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateSolde)) {
            stmtUpdate.setInt(1, montantSortie); // soustraction automatique dans la requête
            stmtUpdate.setString(2, ideglise);

            int rowsUpdate = stmtUpdate.executeUpdate();
            if (rowsUpdate != 1) {
                conn.rollback();
                conn.setAutoCommit(true);
                return "UPDATE_ERROR: Solde non mis à jour.";
            }
        }

        // 3) Tout s’est bien passé → on valide
        conn.commit();
        conn.setAutoCommit(true);
        return "INSERT_OK";

    } catch (SQLException e) {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
        }
        return "INSERT_EXCEPTION: " + e.getMessage();
    }
}
public List<Map<String, Object>> afficheSorties() throws SQLException {
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

//recherce 
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

}
 