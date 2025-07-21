package caiseeglise;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

public class PDFCreator {

    public void createPdf(String filename, String startDate, String endDate, List<Entry> entries) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));

        document.open();

        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        Paragraph dateRange = new Paragraph("Entre " + startDate + " et " + endDate, normalFont);
        dateRange.setAlignment(Element.ALIGN_LEFT);
        document.add(dateRange);
        document.add(new Paragraph(" "));

        Paragraph movementTitle = new Paragraph("Mouvement d'entrée en caisse", normalFont);
        movementTitle.setAlignment(Element.ALIGN_LEFT);
        document.add(movementTitle);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        float[] columnWidths = {2f, 4f, 2f};
        table.setWidths(columnWidths);

        table.addCell(createHeaderCell("Date d'entrée"));
        table.addCell(createHeaderCell("Motif"));
        table.addCell(createHeaderCell("Montant"));

        double totalMontantEntrant = 0.0;

        for (Entry entry : entries) {
            table.addCell(createCell(entry.getDate(), normalFont));
            table.addCell(createCell(entry.getMotif(), normalFont));
            table.addCell(createCell(formatMontant(entry.getMontant()), normalFont));
            totalMontantEntrant += entry.getMontant();
        }

        document.add(table);

        Paragraph totalAmount = new Paragraph("Total Montant entrant : " + formatMontant(totalMontantEntrant), normalFont);
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
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

    private String formatMontant(double montant) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(0);
        return nf.format(montant) + " Ar";
    }

    public String generateUniquePdfName(String baseName, String directory) {
        int index = 1;
        String filename;
        do {
            filename = String.format("%s/%s_%d.pdf", directory, baseName, index);
            index++;
        } while (new java.io.File(filename).exists());
        return filename;
    }
}
