package caiseeglise.Models;

import java.sql.*;
import java.util.*;

public abstract class Table {

    protected Connection conn;
    protected String nameTable;
    protected List<String> columns;
    protected String primaryKey;

    public Table(Connection conn, String nameTable, List<String> columns, String primaryKey) {
        this.conn = conn;
        this.nameTable = nameTable;
        this.columns = columns;
        this.primaryKey = primaryKey;
    }

    // Pour les messages personnalisés (ex: "Eglise", "Client", etc.)
    protected abstract String getEntityLabel();

    // Affiche tous les enregistrements
    public List<Map<String, Object>> affiche() throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT * FROM " + nameTable;

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

    // Insérer des données
    public String insert(Map<String, Object> data) throws SQLException {
        Map<String, Object> filtered = filterColumns(data);
        Object idValue = filtered.get(primaryKey);  // récupérer l'ID

        StringBuilder sql = new StringBuilder("INSERT INTO " + nameTable + " (");
        StringBuilder placeholders = new StringBuilder();

        for (String col : filtered.keySet()) {
            sql.append(col).append(", ");
            placeholders.append("?, ");
        }

        sql.setLength(sql.length() - 2);
        placeholders.setLength(placeholders.length() - 2);
        sql.append(") VALUES (").append(placeholders).append(")");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int i = 1;
            for (Object val : filtered.values()) {
                stmt.setObject(i++, val);
            }
            stmt.executeUpdate();
        }

        return getEntityLabel() + " " + idValue + " enregistré avec succès.";
    }

    // Modifier des données (avec vérification)
    public String modifi(Object id, Map<String, Object> data) throws SQLException {
        Map<String, Object> filtered = filterColumns(data);

        StringBuilder sql = new StringBuilder("UPDATE " + nameTable + " SET ");
        for (String col : filtered.keySet()) {
            sql.append(col).append(" = ?, ");
        }

        sql.setLength(sql.length() - 2); // Supprime la dernière virgule
        sql.append(" WHERE ").append(primaryKey).append(" = ?");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int i = 1;
            for (Object val : filtered.values()) {
                stmt.setObject(i++, val);
            }
            stmt.setObject(i, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return getEntityLabel() + " " + id + " introuvable. Modification non effectuée.";
            }
        }

        return getEntityLabel() + " " + id + " modifié avec succès.";
    }

    // Supprimer un enregistrement (avec vérification)
    public String supprimer(Object id) throws SQLException {
        String sql = "DELETE FROM " + nameTable + " WHERE " + primaryKey + " = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return getEntityLabel() + " " + id + " introuvable.";
            }
        }

        return getEntityLabel() + " " + id + " supprimé avec succès.";
    }

    // Méthode utilitaire pour filtrer les colonnes valides
    protected Map<String, Object> filterColumns(Map<String, Object> data) {
        Map<String, Object> filtered = new LinkedHashMap<>();
        for (String col : columns) {
            if (data.containsKey(col)) {
                filtered.put(col, data.get(col));
            }
        }
        return filtered;
    }
}
