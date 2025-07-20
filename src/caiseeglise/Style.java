package caiseeglise;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class Style {

    // 🔷 Style d'un JLabel (titre)
  // ✔️ Compatible avec java.awt.Label
public void appliquerStyleH1(Label label) {
    label.setFont(new Font("Segoe UI", Font.BOLD, 24));
    label.setForeground(new Color(0, 120, 215));
    label.setAlignment(Label.CENTER); // centrer le texte
}


    // 🔷 Style d'un bouton avec effet visuel et arrondi simple
   public void styliserBouton(JButton btn) {
    btn.setBackground(new Color(13, 110, 253));
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createLineBorder(new Color(13, 110, 253)));
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.setContentAreaFilled(false); // Pour l'effet de transparence
    btn.setOpaque(true);

    // 🔒 Conserver la taille actuelle
    btn.setPreferredSize(btn.getPreferredSize());

    // Effet de survol
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(30, 144, 255));
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(13, 110, 253));
        }
    });
}


    // 🔷 Style d'une JTable (DataGridView like)
    public void appliquerStyleTableau(JTable table) {
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(222, 226, 230));
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setRowHeight(40);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Alternance lignes
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                return cell;
            }
        });

        // En-tête
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
    }

    
}
