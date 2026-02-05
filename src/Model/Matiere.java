package Model;

import java.util.ArrayList;
import java.util.List;

public class Matiere {
    private String code;
    private String libelle;
    private List<Media> medias; // المادة تجمع عدة وسائط

    public Matiere(String code, String libelle) {
        this.code = code;
        this.libelle = libelle;
        this.medias = new ArrayList<>();
    }

    // إضافة ميديا للمادة
    public void ajouterMedia(Media m) {
        medias.add(m);
    }

    // Getters
    public String getCode() { return code; }
    public String getLibelle() { return libelle; }
    public List<Media> getMedias() { return medias; }

    @Override
    public String toString() {
        return libelle + " (" + code + ")";
    }
}
