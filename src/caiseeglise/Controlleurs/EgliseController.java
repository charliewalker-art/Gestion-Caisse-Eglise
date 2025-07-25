package caiseeglise.Controlleurs;

import caiseeglise.Models.Eglise;
import java.sql.Connection;
import java.util.Map;

public class EgliseController extends Controlleur<Eglise> {

    public EgliseController(Connection conn) {
        super(conn);
    }

    @Override
    protected Eglise createTable(Connection conn) {
        return new Eglise(conn);
    }

    // Retourne un message (au lieu d'imprimer dans la console)
    public String ajouterEglise(String design) {
    return table.insertionEglise(design);
}


    // Idem si tu veux aussi modifier et supprimer avec retour
    public String modifierEglise(String ideglise, Map<String, Object> data) {
        try {
            return table.modifi(ideglise, data);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la modification : " + e.getMessage();
        }
    }

    public String supprimerEglise(String ideglise) {
        try {
            return table.supprimer(ideglise);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la suppression : " + e.getMessage();
        }
    }

    public Eglise getTable() {
        return table;
    }
}
