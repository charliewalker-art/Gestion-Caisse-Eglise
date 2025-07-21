package caiseeglise;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Classe représentant une entrée ou sortie de mouvement de caisse.
 * Contient la date, le motif et le montant du mouvement.
 */
class Entry {
    private String date;
    private String motif;
    private double montant;

    public Entry(String date, String motif, double montant) {
        this.date = date;
        this.motif = motif;
        this.montant = montant;
    }

    public String getDate() {
        return date;
    }

    public String getMotif() {
        return motif;
    }

    public double getMontant() {
        return montant;
    }
}

/**
 * Classe pour créer des documents PDF de rapport de caisse.
 */
public class PDFCreator {

    // Définition des polices pour le texte normal et en gras (peut être final)
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    /**
     * Crée un document PDF complet avec les mouvements d'entrée et de sortie.
     * C'est la méthode principale à appeler pour générer le rapport.
     *
     * @param filename Le nom du fichier PDF à créer.
     * @param startDate La date de début de la période du rapport.
     * @param endDate La date de fin de la période du rapport.
     * @param entreeEntries Une liste d'objets Entry pour les mouvements d'entrée.
     * @param sortieEntries Une liste d'objets Entry pour les mouvements de sortie.
     * @throws DocumentException Si une erreur liée au document PDF survient.
     * @throws IOException Si une erreur d'entrée/sortie survient lors de la création du fichier.
     */
    public void createFullCaisseReportPdf(String filename, String startDate, String endDate,
                                         List<Entry> entreeEntries, List<Entry> sortieEntries)
                                         throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));

        document.open();

        // Ajout du titre général du rapport
        Paragraph reportTitle = new Paragraph("Rapport de Mouvement de Caisse", BOLD_FONT);
        reportTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(reportTitle);
        document.add(new Paragraph(" ")); // Espace

        // Ajout de la période du rapport
        Paragraph dateRange = new Paragraph("Entre " + startDate + " et " + endDate, NORMAL_FONT);
        dateRange.setAlignment(Element.ALIGN_LEFT);
        document.add(dateRange);
        document.add(new Paragraph(" ")); // Espace

        // --- Section Mouvement d'entrée ---
        addMovementSection(document, "Mouvement d'entrée en caisse", entreeEntries, "Total Montant entrant : ");
        document.add(new Paragraph(" ")); // Espace entre les sections

        // --- Section Mouvement de sortie ---
        addMovementSection(document, "Mouvement de sortie de caisse", sortieEntries, "Total Montant sortant : ");
        document.add(new Paragraph(" ")); // Espace après la dernière section

        // --- Ajout du solde final (optionnel, mais utile pour un rapport complet) ---
        double totalEntree = calculateTotal(entreeEntries);
        double totalSortie = calculateTotal(sortieEntries);
        double soldeFinal = totalEntree - totalSortie;

        Paragraph soldeParagraph = new Paragraph("Solde Final : " + formatMontant(soldeFinal), BOLD_FONT);
        soldeParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(soldeParagraph);


        document.close();
    }

    /**
     * Ajoute une section de mouvement (entrée ou sortie) au document.
     *
     * @param document Le document PDF auquel ajouter la section.
     * @param sectionTitle Le titre de la section (ex: "Mouvement d'entrée en caisse").
     * @param entries La liste des entrées pour cette section.
     * @param totalPrefix Le préfixe pour la ligne du total (ex: "Total Montant entrant : ").
     * @throws DocumentException Si une erreur liée au document PDF survient.
     */
    private void addMovementSection(Document document, String sectionTitle, List<Entry> entries, String totalPrefix) throws DocumentException {
        // Titre de la section
        Paragraph movementTitle = new Paragraph(sectionTitle, NORMAL_FONT);
        movementTitle.setAlignment(Element.ALIGN_LEFT);
        document.add(movementTitle);
        document.add(new Paragraph(" ")); // Espace

        // Création d'un tableau avec 3 colonnes pour les données
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5f); // Petit espace avant le tableau
        table.setSpacingAfter(5f);  // Petit espace après le tableau

        // Définition des largeurs relatives des colonnes (Date, Motif, Montant)
        float[] columnWidths = {2f, 4f, 2f};
        table.setWidths(columnWidths);

        // Ajout des en-têtes de colonne
        table.addCell(createHeaderCell("Date d'entrée")); // Note: 'Date d'entrée' est générique, pourrait être 'Date'
        table.addCell(createHeaderCell("Motif"));
        table.addCell(createHeaderCell("Montant"));

        double totalMontant = 0.0; // Variable pour calculer le total des montants de cette section

        // Ajout des lignes de données à partir de la liste d'entrées
        for (Entry entry : entries) {
            table.addCell(createCell(entry.getDate(), NORMAL_FONT));
            table.addCell(createCell(entry.getMotif(), NORMAL_FONT));
            table.addCell(createCell(formatMontant(entry.getMontant()), NORMAL_FONT));
            totalMontant += entry.getMontant(); // Ajout au total de la section
        }

        document.add(table); // Ajoute le tableau au document

        // Paragraphe pour le total de la section
        Paragraph totalAmountParagraph = new Paragraph(totalPrefix + formatMontant(totalMontant), NORMAL_FONT);
        totalAmountParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(totalAmountParagraph);
    }

    /**
     * Calcule le total des montants d'une liste d'entrées.
     * @param entries La liste d'entrées.
     * @return Le total des montants.
     */
    private double calculateTotal(List<Entry> entries) {
        double total = 0.0;
        for (Entry entry : entries) {
            total += entry.getMontant();
        }
        return total;
    }


    /**
     * Crée une cellule d'en-tête pour le tableau.
     * @param content Le texte de l'en-tête.
     * @return Une instance de PdfPCell configurée comme en-tête.
     */
    private PdfPCell createHeaderCell(String content) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, BOLD_FONT));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

    /**
     * Crée une cellule de données pour le tableau.
     * @param content Le texte de la cellule.
     * @param font La police à utiliser pour le texte.
     * @return Une instance de PdfPCell configurée comme cellule de données.
     */
    private PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

    /**
     * Formate un montant double en chaîne de caractères avec un séparateur de milliers
     * et le suffixe " Ar".
     * Ex: 325000.0 devient "325.000 Ar"
     * @param montant Le montant à formater.
     * @return Le montant formaté en chaîne de caractères.
     */
    private String formatMontant(double montant) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(0);
        return nf.format(montant) + " Ar";
    }

    /**
     * Génère un nom de fichier PDF unique en ajoutant un index si le fichier existe déjà.
     *
     * @param baseName Le nom de base du fichier (sans extension ni chemin).
     * @param directory Le répertoire où le fichier sera enregistré.
     * @return Le nom de fichier complet et unique.
     */
    public String generateUniquePdfName(String baseName, String directory) {
        int index = 1;
        String filename;
        do {
            filename = String.format("%s/%s_%d.pdf", directory, baseName, index);
            index++;
        } while (new java.io.File(filename).exists());
        return filename;
    }

    /**
     * Méthode principale pour tester la création du PDF.
     */
    public static void main(String[] args) {
        PDFCreator pdfCreator = new PDFCreator();

        String startDate = "23-02-2025";
        String endDate = "15-03-2025";

        List<Entry> entreeEntries = new ArrayList<>();
        entreeEntries.add(new Entry("25-02-2025", "Rakitra", 325000.00));
        entreeEntries.add(new Entry("02-03-2025", "Don d'une association", 200000.00));

        List<Entry> sortieEntries = new ArrayList<>();
        sortieEntries.add(new Entry("25-02-2025", "Achat d'un micro", 28000.00));
        sortieEntries.add(new Entry("02-03-2025", "Jirama", 151820.00));

        try {
            // Appel de la nouvelle méthode pour créer le rapport complet
            pdfCreator.createFullCaisseReportPdf("rapport_caisse_complet.pdf", startDate, endDate, entreeEntries, sortieEntries);
            System.out.println("PDF complet créé avec succès !");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
