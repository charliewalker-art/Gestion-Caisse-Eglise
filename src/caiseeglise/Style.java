package caiseeglise;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
//

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

    // ? Conserver la taille actuelle
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


    // 🔷 Tableau stylisé
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

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
    }
   




// 🔷 Style pour JTextField
    public void styliserTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        textField.setCaretColor(Color.BLACK);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    
 private void styliserBase(JButton btn, Color bgColor, Color hoverColor) {
    btn.setBackground(bgColor);
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btn.setFocusPainted(false);
    btn.setContentAreaFilled(false);
    btn.setOpaque(false); // pour que le rendu fonctionne correctement
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // padding seulement

    btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = c.getWidth();
            int height = c.getHeight();

            // Dessine fond
            g2.setColor(btn.getBackground());
            g2.fillRoundRect(0, 0, width, height, 30, 30); // rayon = 30

            // Dessine texte
            super.paint(g, c);
        }

        @Override
        protected void paintButtonPressed(Graphics g, AbstractButton b) {
            g.setColor(b.getBackground().darker());
        }
    });

    // Hover effect
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn.setBackground(hoverColor);
            btn.repaint();
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(bgColor);
            btn.repaint();
        }
    });
}

    
     // 🟦 Bouton Primary
    public void styliserBoutonPrimary(JButton btn) {
        styliserBase(btn, new Color(13, 110, 253), new Color(30, 144, 255));
    }

    // 🟩 Bouton Success
    public void styliserBoutonSuccess(JButton btn) {
        styliserBase(btn, new Color(25, 135, 84), new Color(40, 167, 69));
    }

    // 🟥 Bouton Danger
    public void styliserBoutonDanger(JButton btn) {
        styliserBase(btn, new Color(220, 53, 69), new Color(200, 35, 51));
    }

    // 🟨 Bouton Warning
    public void styliserBoutonWarning(JButton btn) {
        styliserBase(btn, new Color(255, 193, 7), new Color(255, 213, 50));
        btn.setForeground(Color.BLACK); // texte noir pour contraste
    }
    public JButton creerBoutonArrondi(String texte, Color bg, Color border) {
    return new RoundedButton(texte, bg, border);
}

}
