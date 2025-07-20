package caiseeglise.Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Eglise extends Table {

    public Eglise(Connection conn) {
        super(
            conn,
            "EGLISE",
            Arrays.asList("ideglise", "Design", "Solde"),
            "ideglise"
        );
    }

    @Override
    protected String getEntityLabel() {
        return "Eglise";
    }

    // Méthode pour générer un nouvel ID automatique
    public String Eglise_ID_unique() throws SQLException {
        String prefix = "EG";
        String sql = "SELECT ideglise FROM " + nameTable + " WHERE ideglise LIKE ? ORDER BY ideglise DESC LIMIT 1";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prefix + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String lastId = rs.getString("ideglise"); // ex: EG005
                    // Extraire la partie numérique
                    String numberPart = lastId.substring(prefix.length());
                    int number = Integer.parseInt(numberPart);
                    number++; // Incrémenter
                    // Recréer l'ID avec 3 chiffres
                    return prefix + String.format("%03d", number);
                } else {
                    // Aucun ID existant, commencer à EG001
                    return prefix + "001";
                }
            }
        }
        
    }
    public List<Map<String, Object>> afficheEglises() throws SQLException {
    List<Map<String, Object>> results = new ArrayList<>();

    String sql = "SELECT * FROM " + nameTable + " ORDER BY ideglise ASC"; // 

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
public String insertionEglise(String design) {
    try {
        String ideglise = Eglise_ID_unique(); // génère l'ID unique
        int soldeInitial = 0;

        Map<String, Object> data = Map.of(
            "ideglise", ideglise,
            "Design", design,
            "Solde", soldeInitial
        );

        return insert(data); // appelle la méthode insert héritée de Table
    } catch (SQLException e) {
        return "Erreur lors de l'insertion : " + e.getMessage();
    }
}
public String mettreAJourSolde(String ideglise, int montant) {
    String sql = "UPDATE EGLISE SET Solde = COALESCE(Solde, 0) + ? WHERE ideglise = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, montant);
        stmt.setString(2, ideglise);

        int rows = stmt.executeUpdate();
        System.out.println("Update Solde rows affected: " + rows);

        if (rows == 0) {
            return "UPDATE_NOT_FOUND";
        }
        return "UPDATE_OK";
    } catch (SQLException e) {
        e.printStackTrace();
        return "UPDATE_EXCEPTION: " + e.getMessage();
    }
}



    
}
