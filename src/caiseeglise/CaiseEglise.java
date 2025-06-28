package caiseeglise;

import caiseeglise.Models.Database;
import caiseeglise.Controlleurs.EgliseController;
import java.sql.Connection;
import java.sql.SQLException;

public class CaiseEglise {

    public static void main(String[] args) {

  
        //appel du form Eglise View
        javax.swing.SwingUtilities.invokeLater(() -> {
            new EgliseView().setVisible(true);
        }); 
    
        
    
    }

}
