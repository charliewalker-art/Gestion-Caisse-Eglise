package caiseeglise;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFCreator {

    public void createPdf(String filename) throws DocumentException, IOException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));

        document.open();

        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        // "Entre dates" paragraph
        Paragraph dateRange = new Paragraph("Entre 23-02-2025 et 15-03-2025", normalFont);
        dateRange.setAlignment(Element.ALIGN_LEFT);
        document.add(dateRange);
        document.add(new Paragraph(" ")); // space

        // "Mouvement d'entrée en caisse" paragraph
        Paragraph movementTitle = new Paragraph("Mouvement d'entrée en caisse", normalFont);
        movementTitle.setAlignment(Element.ALIGN_LEFT);
        document.add(movementTitle);
        document.add(new Paragraph(" ")); // space

        // Create a table with 3 columns
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Column widths - adjusted to make "Montant" column wider
        float[] columnWidths = {2f, 4f, 2f}; // Date, Motif, Montant
        table.setWidths(columnWidths);

        // Headers
        table.addCell(createHeaderCell("Date d'entrée"));
        table.addCell(createHeaderCell("Motif"));
        table.addCell(createHeaderCell("Montant"));

        // Data rows
        table.addCell(createCell("25-02-2025", normalFont));
        table.addCell(createCell("Rakitra", normalFont));
        table.addCell(createCell("325.000 Ar", normalFont));

        table.addCell(createCell("02-03-2025", normalFont));
        table.addCell(createCell("Don d'une association", normalFont));
        table.addCell(createCell("200.000 Ar", normalFont));

        document.add(table);

        // Total Montant entrant
        Paragraph totalAmount = new Paragraph("Total Montant entrant : 525.000 AR", normalFont);
        totalAmount.setAlignment(Element.ALIGN_LEFT);
        document.add(totalAmount);

        document.close();
    }

    private PdfPCell createHeaderCell(String content) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

    private PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

    public static void main(String[] args) {
        PDFCreator pdfCreator = new PDFCreator();
        try {
            pdfCreator.createPdf("rapport_caisse.pdf");
            System.out.println("PDF créé avec succès !");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}