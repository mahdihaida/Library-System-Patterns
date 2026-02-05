package Model;

import java.util.ArrayList;
import java.util.List;

public class Specialite {
    private String code;
    private String nom;
    private List<Matiere> matieres; // التخصص يتكون من مواد

    public Specialite(String code, String nom) {
        this.code = code;
        this.nom = nom;
        this.matieres = new ArrayList<>();
    }

    // إضافة مادة للتخصص
    public void ajouterMatiere(Matiere m) {
        matieres.add(m);
    }

    // Getters
    public String getCode() { return code; }
    public String getNom() { return nom; }
    public List<Matiere> getMatieres() { return matieres; }

    @Override
    public String toString() {
        return nom;
    }
}
