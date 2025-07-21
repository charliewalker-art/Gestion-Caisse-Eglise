package caiseeglise.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class HistoDonne {
    private final Connection conn;

    public HistoDonne(Connection conn) {
        this.conn = conn;
    }

    // Méthode existante : total par tous les mois
    public Map<String, Integer> getMontantEntreParMois(String ideglise) throws Exception {
        String sql = "SELECT MONTH(dateEntre) AS mois, SUM(montantEntre) AS total FROM ENTRE WHERE ideglise = ? GROUP BY mois";
        Map<String, Integer> map = new LinkedHashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ideglise);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int mois = rs.getInt("mois");
                int montant = rs.getInt("total");
                map.put(getNomMois(mois), montant);
            }
        }
        return map;
    }

    // Nouvelle méthode : total pour un mois spécifique
    public int getMontantEntreParMois(String ideglise, int mois) throws Exception {
        String sql = "SELECT SUM(montantEntre) AS total FROM ENTRE WHERE ideglise = ? AND MONTH(dateEntre) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ideglise);
            stmt.setInt(2, mois);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Méthode existante : total par tous les mois sorties
    public Map<String, Integer> getMontantSortieParMois(String ideglise) throws Exception {
        String sql = "SELECT MONTH(dateSortie) AS mois, SUM(montantSortie) AS total FROM SORTIE WHERE ideglise = ? GROUP BY mois";
        Map<String, Integer> map = new LinkedHashMap<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ideglise);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int mois = rs.getInt("mois");
                int montant = rs.getInt("total");
                map.put(getNomMois(mois), montant);
            }
        }
        return map;
    }

    // Nouvelle méthode : total sorties pour un mois spécifique
    public int getMontantSortieParMois(String ideglise, int mois) throws Exception {
        String sql = "SELECT SUM(montantSortie) AS total FROM SORTIE WHERE ideglise = ? AND MONTH(dateSortie) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ideglise);
            stmt.setInt(2, mois);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    public String getNomMois(int mois) {
        String[] moisNom = {
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };
        return (mois >= 1 && mois <= 12) ? moisNom[mois - 1] : "Inconnu";
    }
}
