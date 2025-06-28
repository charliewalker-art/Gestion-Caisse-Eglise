package caiseeglise;

import caiseeglise.Models.Database;
import caiseeglise.Controlleurs.EgliseController;
import java.sql.Connection;
import java.sql.SQLException;

public class CaiseEglise {

    public static void main(String[] args) {

  
        javax.swing.SwingUtilities.invokeLater(() -> {
            new EgliseView().setVisible(true);
        }); 

        
        
        
      /*  Connection comnn = Database.GetConnexionDB();
        if (comnn == null) {
            System.out.println("Connexion impossible. Fin du programme.");
            return;
        }

        EgliseController egliseController = new EgliseController(comnn);

        try {
            String newId = egliseController.getTable().Eglise_ID_unique();

            egliseController.ajouterEglise(
                newId,
                "Angelo",
                50000
            );

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la génération de l'ID.");
        }*/
    }

}
