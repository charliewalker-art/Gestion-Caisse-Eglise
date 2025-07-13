package caiseeglise.Models;


import caiseeglise.Models.Database;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
//import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableModel;
//import java.util.*;

public class Methodes {
    
       // Message générique
    public static void show(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    // Message de succès (avec icône information)
    public static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    // Message d'information
    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // Message d'erreur
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
     public static Connection getconnexion() {
        Connection conn = Database.GetConnexionDB();
        if (conn == null) {
            show("Connexion à la base de données impossible.");
        }
        return conn;
    }
      public static DefaultTableModel createTableModel(List<Map<String, Object>> data, Map<String, String> columnLabels) {
    if (data.isEmpty()) return new DefaultTableModel();

    // Colonnes réelles de la base
    List<String> dbColumns = new ArrayList<>(data.get(0).keySet());

    // Noms visibles pour l'utilisateur
    Vector<String> columnNames = new Vector<>();
    for (String dbCol : dbColumns) {
        columnNames.add(columnLabels.getOrDefault(dbCol, dbCol)); // par défaut : nom brut
    }

    // Contenu
    Vector<Vector<Object>> rows = new Vector<>();
    for (Map<String, Object> row : data) {
        Vector<Object> rowData = new Vector<>();
        for (String dbCol : dbColumns) {
            rowData.add(row.get(dbCol));
        }
        rows.add(rowData);
    }

    return new DefaultTableModel(rows, columnNames);
}

}
