package Model;

// تأكد من استيراد Visitor
import service.StatsVisitor;

public class Quiz extends Media {
    private String niveau;

    // قمنا بإزالة 'String s' لأنه غير مستخدم
    public Quiz(int id, String titre, String auteur, int annee, String desc, String niveau) {

        // 1. نمرر "quiz" كنوع.
        // 2. نمرر 0 كقيمة افتراضية للمعامل الأخير (durationOrPages) لأن الكويز لا يملك صفحات أو مدة في هذا السياق
        super("quiz", id, titre, auteur, annee, desc, 0);

        this.niveau = niveau;
    }

    protected void afficherDetailsSpecifiques() {
        System.out.println("[QUIZ] Niveau: " + niveau);
    }



    public void accept(Visitor visitor) {
        visitor.visitQuiz(this);
    }


    public String getNiveau() {
        return niveau;
    }

    // دالة toString مخصصة (اختياري)
    @Override
    public String toString() {
        return super.toString() + " [Niveau: " + niveau + "]";
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
}