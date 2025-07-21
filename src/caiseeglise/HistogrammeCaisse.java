package caiseeglise;

import caiseeglise.Models.HistoDonne;
import caiseeglise.Models.Methodes;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class HistogrammeCaisse extends JPanel {

    public HistogrammeCaisse(String ideglise, int mois) {
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            Connection conn = Methodes.getconnexion();

            if (conn != null) {
                HistoDonne donne = new HistoDonne(conn);

                // Récupérer les montants filtrés par mois (mois en int)
                int montantEntree = donne.getMontantEntreParMois(ideglise, mois);
                int montantSortie = donne.getMontantSortieParMois(ideglise, mois);

                String nomMois = donne.getNomMois(mois); // ou crée une méthode publique dans HistoDonne

                dataset.addValue(montantEntree, "Entrées", nomMois);
                dataset.addValue(montantSortie, "Sorties", nomMois);

            } else {
                Methodes.showError("Connexion à la base échouée.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Methodes.showError("Erreur : " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Mouvement de caisse pour " + ideglise + " - " + getNomMois(mois),
                "Mois",
                "Montant (Ar)",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);

        this.add(chartPanel, BorderLayout.CENTER);
    }

    // Méthode locale pour nom de mois (si pas accessible depuis HistoDonne)
    private String getNomMois(int mois) {
        String[] moisNom = {
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };
        return (mois >= 1 && mois <= 12) ? moisNom[mois - 1] : "Inconnu";
    }
}
