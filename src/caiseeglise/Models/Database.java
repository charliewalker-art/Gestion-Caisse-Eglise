package caiseeglise.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_eglise?useSSL=false&serverTimezone=UTC";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    public static Connection GetConnexionDB() {
        try {
            Connection conn = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
         //   System.out.println(" Connexion à la base de données réussie !");
            
            // Vérifie si l'auto-commit est activé
            if (conn.getAutoCommit()) {
         //       System.out.println("️ Auto-commit est activé.");
            } else {
                System.out.println("️ Auto-commit est désactivé. Les transactions doivent être validées manuellement.");
            }

            return conn;

        } catch (SQLException e) {
            System.err.println(" Erreur de connexion : " + e.getMessage());
            return null;
        }
    }

  
}

