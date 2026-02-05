package Model;

import service.StatsVisitor;
import java.util.ArrayList;
import java.util.List;

public class Media {
    private String type;
    private int id;
    private String title;
    private String author;
    private int year;
    private String category;
    private int durationOrPages; // مدة الفيديو أو عدد الصفحات
    private List<String> codesMatieres; // ✔ غير static

    // Constructor
    public Media(String type, int id, String title, String author, int year, String category, int durationOrPages) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.durationOrPages = durationOrPages;
        this.codesMatieres = new ArrayList<>();
    }

    // إضافة كود مادة
    public void ajouterCodeMatiere(String code) {
        if (code != null && !code.isBlank() && !codesMatieres.contains(code.trim())) {
            codesMatieres.add(code.trim());
        }
    }

    // Getter لجميع أكواد المواد
    public List<String> getCodesMatieres() {
        return codesMatieres;
    }

    // Setter قائمة المواد
    public void setCodesMatieres(List<String> codesMatieres) {
        this.codesMatieres = codesMatieres;
    }

    // Getters & Setters عادية
    public String getType() { return type; }
    public int getId() { return id; }
    public String getTitre() { return title; }
    public String getAuteur() { return author; }
    public int getAnnee() { return year; }
    public String getDescription() { return category; }
    public int getDureeOuPagesInt() { return durationOrPages; } // صيغة عدد صحيح
    public String getDureeOuPages() { return durationOrPages + ""; } // صيغة نصية

    public void setId(int id) { this.id = id; }
    public void setTitre(String titre) { this.title = titre; }
    public void setAuteur(String auteur) { this.author = auteur; }
    public void setAnnee(int annee) { this.year = annee; }
    public void setDescription(String description) { this.category = description; }

    // Visitor
//    public void accept(StatsVisitor visitor) {
//        visitor.visitMedia(this);
//    }

    // تصحيح toString
    @Override
    public String toString() {
        return type + ";" + id + ";" + title + ";" + author + ";" + year + ";" + category + ";" + codesMatieres + ";" + durationOrPages;
    }


    // Visitor Pattern - proper implementation
    public void accept(Visitor visitor) {
        visitor.visitMedia(this);
    }
}
