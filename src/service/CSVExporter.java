package service;

import Model.Media;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVExporter implements ExportStrategy {
    public void export(List<Media> medias) {
        System.out.println(">>> Exporting to CSV format...");

        // سنقوم بالطباعة في الكونسول وأيضاً حفظ ملف حقيقي
        try (PrintWriter writer = new PrintWriter(new FileWriter("export.csv"))) {
            // الهيدر
            writer.println("ID,Titre,Auteur,Annee,Type");

            for (Media m : medias) {
                // تحضير السطر
                String line = m.getId() + "," + m.getTitre() + "," + m.getAuteur() + "," + m.getAnnee() + "," + m.getClass().getSimpleName();

                // كتابة في الملف
                writer.println(line);
                // طباعة في الكونسول للتأكيد
                System.out.println(line);
            }
            System.out.println("Fichier 'export.csv' créé avec succès !");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}