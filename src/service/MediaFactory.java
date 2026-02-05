package service;

import Model.*;

public class MediaFactory {

    public static Media createMedia(String type, int id, String titre, String auteur, int annee, String description, Object extraInfo) {

        if (type == null) return null;

        // تنظيف المدخلات
        String typeClean = type.trim();

        // تصحيح: لا نستخدم extraInfo هنا لأنه يحتوي على المدة/الصفحات وليس اسم المادة
        // سنمرر نصاً فارغاً "" للكائن، لأن المواد يتم إضافتها لاحقاً في Main عبر (ajouterCodeMatiere)
        String matiereInitiale = "";

        try {
            // 1. Video Case
            if (typeClean.equalsIgnoreCase("Video") || typeClean.equalsIgnoreCase("Vidéo")) {
                int duree = 0;
                // التحقق من النوع قبل التحويل لتجنب الأخطاء
                if (extraInfo instanceof Integer) {
                    duree = (Integer) extraInfo;
                } else {
                    duree = Integer.parseInt(extraInfo.toString());
                }
                // الترتيب: ID, Titre, Auteur, Annee, Desc, Matiere(Empty), Duree
                return new Video(id, titre, auteur, annee, description,  duree);
            }

            // 2. Document Case
            else if (typeClean.equalsIgnoreCase("Document") || typeClean.equalsIgnoreCase("Livre")) {
                int nbPages = 0;
                if (extraInfo instanceof Integer) {
                    nbPages = (Integer) extraInfo;
                } else {
                    nbPages = Integer.parseInt(extraInfo.toString());
                }
                return new Document(id, titre, auteur, annee, description,  nbPages);
            }

            // 3. Quiz Case
            else if (typeClean.equalsIgnoreCase("Quiz")) {
                String niveau = extraInfo.toString();
                return new Quiz(id, titre, auteur, annee, description,  niveau);
            }

        } catch (NumberFormatException e) {
            System.err.println("Erreur de format dans MediaFactory pour l'ID " + id + ": " + e.getMessage());
        }

        return null;
    }
}