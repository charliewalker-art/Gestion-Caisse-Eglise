package caiseeglise.Models;

import java.sql.*;
import java.util.*;
import java.util.Date;
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

  /*  public String insertSortie(String motif, int montantSortie, String dateSortie, String ideglise) {
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
*/
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

    
  public List<Map<String, Object>> rechercherParDates(Date dateA, Date dateB, String idEglise) throws SQLException {
    List<Map<String, Object>> results = new ArrayList<>();

    String sql = "SELECT dateSortie, motif, montantSortie FROM " + nameTable +
                 " WHERE dateSortie BETWEEN ? AND ? AND ideglise = ? ORDER BY dateSortie ASC";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setDate(1, new java.sql.Date(dateA.getTime()));
        stmt.setDate(2, new java.sql.Date(dateB.getTime()));
        stmt.setString(3, idEglise);  // 

        try (ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }

                row.put("typeMouvement", "Sortie");
                results.add(row);
            }
        }
    }

    return results;
}

public String insertSortie(String motif, int montantSortie, String dateSortie, String ideglise) {
    String sqlSelectSolde = "SELECT COALESCE(Solde, 0) FROM EGLISE WHERE ideglise = ?";
    String sqlInsert = "INSERT INTO SORTIE (motif, montantSortie, dateSortie, ideglise) VALUES (?, ?, ?, ?)";
    String sqlUpdateSolde = "UPDATE EGLISE SET Solde = Solde - ? WHERE ideglise = ?";

    try {
        conn.setAutoCommit(false);  // Démarre la transaction

        // 1) Vérifier le solde
        int soldeActuel = 0;
        try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelectSolde)) {
            stmtSelect.setString(1, ideglise);
            ResultSet rs = stmtSelect.executeQuery();
            if (rs.next()) {
                soldeActuel = rs.getInt(1);
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
                return "EGLISE_NOT_FOUND";
            }
        }

        // 2) Vérifier si le solde après sortie serait < 10 000
        if ((soldeActuel - montantSortie) < 10000) {
            conn.setAutoCommit(true);
            return "le solde actuel ne doit pas être inférieur à 10.000 Ar";
        }

        // 3) Insertion dans SORTIE
        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
            stmtInsert.setString(1, motif);
            stmtInsert.setInt(2, montantSortie);
            stmtInsert.setString(3, dateSortie);
            stmtInsert.setString(4, ideglise);

            int rowsInsert = stmtInsert.executeUpdate();
            if (rowsInsert != 1) {
                conn.rollback();
                conn.setAutoCommit(true);
                return "INSERT_ERROR";
            }
        }

        // 4) Mise à jour du solde
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateSolde)) {
            stmtUpdate.setInt(1, montantSortie);
            stmtUpdate.setString(2, ideglise);

            int rowsUpdate = stmtUpdate.executeUpdate();
            if (rowsUpdate != 1) {
                conn.rollback();
                conn.setAutoCommit(true);
                return "UPDATE_ERROR";
            }
        }

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

}
 