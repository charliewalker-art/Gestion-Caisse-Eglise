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

//fonction affiche Entre
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

    //fonction modieir ENTTRE
public String modifierEntree(int identre, String nouveauMotif, int nouveauMontant, String nouvelleDate) {
    String sqlSelect = "SELECT montantEntre, ideglise FROM ENTRE WHERE identre = ?";
    String sqlUpdateEntree = "UPDATE ENTRE SET motif = ?, montantEntre = ?, dateEntre = ? WHERE identre = ?";

    try {
        conn.setAutoCommit(false); // début de transaction

        int ancienMontant = 0;
        String ideglise = null;

        // 1. Récupérer les infos actuelles de l'entrée
        try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
            stmtSelect.setInt(1, identre);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                ancienMontant = rs.getInt("montantEntre");
                ideglise = rs.getString("ideglise");
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
                return "ENTREE_NOT_FOUND";
            }
        }

        // 2. Mettre à jour l'entrée
        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateEntree)) {
            stmtUpdate.setString(1, nouveauMotif);
            stmtUpdate.setInt(2, nouveauMontant);
            stmtUpdate.setString(3, nouvelleDate);
            stmtUpdate.setInt(4, identre);

            int rows = stmtUpdate.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                conn.setAutoCommit(true);
                return "UPDATE_ENTREE_FAILED";
            }
        }

        // 3. Calculer la différence de montant
        int difference = nouveauMontant - ancienMontant;

        // 4. Appeler la méthode mettreAJourSolde de la classe Eglise (sans la modifier)
        Eglise eglise = new Eglise(conn); // instanciation avec la même connexion
        String result = eglise.mettreAJourSolde(ideglise, difference);

        if (!"UPDATE_OK".equals(result)) {
            conn.rollback();
            conn.setAutoCommit(true);
            return "UPDATE_SOLDE_FAILED: " + result;
        }

        // 5. Valider la transaction
        conn.commit();
        conn.setAutoCommit(true);
        return "MODIFICATION_OK";

    } catch (SQLException e) {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
        }
        e.printStackTrace();
        return "MODIFICATION_EXCEPTION: " + e.getMessage();
    }
}


  //fontion supprimet
public String supprimerEntree(int identre) {
    String sqlSelect = "SELECT montantEntre, ideglise FROM ENTRE WHERE identre = ?";
    String sqlDelete = "DELETE FROM ENTRE WHERE identre = ?";

    try {
        conn.setAutoCommit(false); // Début transaction

        int montant = 0;
        String ideglise = null;

        // 1. Récupération du montant et ideglise
        try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
            stmtSelect.setInt(1, identre);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                montant = rs.getInt("montantEntre");
                ideglise = rs.getString("ideglise");
            } else {
                conn.rollback();
                conn.setAutoCommit(true);
                return "ENTREE_NOT_FOUND";
            }
        }

        // 2. Suppression de l'entrée
        try (PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {
            stmtDelete.setInt(1, identre);
            int rowsDeleted = stmtDelete.executeUpdate();
            if (rowsDeleted == 0) {
                conn.rollback();
                conn.setAutoCommit(true);
                return "DELETE_FAILED";
            }
        }

        // 3. Mise à jour du solde (soustraction du montant supprimé)
        Eglise eglise = new Eglise(conn);
        String soldeUpdateResult = eglise.mettreAJourSolde(ideglise, -montant); // soustraction

        if (!"UPDATE_OK".equals(soldeUpdateResult)) {
            conn.rollback();
            conn.setAutoCommit(true);
            return "UPDATE_SOLDE_FAILED: " + soldeUpdateResult;
        }

        conn.commit();
        conn.setAutoCommit(true);
        return "DELETE_OK";

    } catch (SQLException e) {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException ex) {}
        e.printStackTrace();
        return "DELETE_EXCEPTION: " + e.getMessage();
    }
}


}
