package caiseeglise.Controlleurs;

import caiseeglise.Models.Table;

import java.sql.Connection;
import java.util.Map;

public abstract class Controlleur<T extends Table> {

    protected T table;

    public Controlleur(Connection conn) {
        this.table = createTable(conn);
    }

    // Chaque contrôleur concret doit créer l’objet modèle
    protected abstract T createTable(Connection conn);

    // Méthodes génériques réutilisables pour tous les modèles
    public void ajouter(Map<String, Object> data) {
        try {
            System.out.println(table.insert(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modifier(Object id, Map<String, Object> nouvellesValeurs) {
        try {
            System.out.println(table.modifi(id, nouvellesValeurs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supprimer(Object id) {
        try {
            System.out.println(table.supprimer(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void afficherTout() {
        try {
            var resultats = table.affiche();
            for (var ligne : resultats) {
                System.out.println(ligne);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
