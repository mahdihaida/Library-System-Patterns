package Model;

import service.Observer;
import Model.Media;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Student extends User implements Observer {
    private Specialite specialite;
    private List<Matiere> matieresInscrites; // قائمة المواد المسجل بها الطالب

    public Student(String username, String password) {
        super(username, password);
        this.matieresInscrites = new ArrayList<>();
    }

    // دالة لربط التخصص
    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    // دالة لإضافة مادة للقائمة
    public void ajouterMatiere(Matiere m) {
        // نمنع التكرار البسيط
        for(Matiere existing : matieresInscrites) {
            if(existing.getCode().equalsIgnoreCase(m.getCode())) return;
        }
        this.matieresInscrites.add(m);
    }

    public List<Matiere> getMatieresInscrites() {
        return matieresInscrites;
    }

    /**
     * تحديث: دالة الـ update الآن تتحقق من المادة
     * لا يتم إرسال إشعار إلا إذا كانت مادة الميديا الجديدة ضمن مواد الطالب
     */
    public void update(Media m) {
        boolean isEnrolled = false;

        for (Matiere matiere : matieresInscrites) {
            for (String code : m.getCodesMatieres()) {
                if (matiere.getCode().equalsIgnoreCase(code)) {
                    isEnrolled = true;
                    break;
                }
            }
            if (isEnrolled) break;
        }

        if (isEnrolled) {
            String codesText = String.join(", ", m.getCodesMatieres());
            System.out.println("EMAIL TO " + username + ": Nouvelle ressource ajoutée pour votre cours " + codesText + " -> " + m.getTitre());
        }
    }





    public void addSubject(String textContent) {
        // TODO: add subject

    }

    public Collection<String> getSubscribedSubjects() {
        return null;
    }
}