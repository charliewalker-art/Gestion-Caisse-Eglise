package caiseeglise;

import java.io.IOException;
import com.itextpdf.text.DocumentException;

public class CaiseEglise {

    public static void main(String[] args) {
        // Création du PDF à partir de la classe PDFCreator
        PDFCreator pdf = new PDFCreator();
        
        try {
            pdf.createPdf("HelloWorld.pdf"); // Or "rapport_caisse.pdf" if you prefer the same name as in PDFCreator
            System.out.println("PDF créé avec succès !");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}