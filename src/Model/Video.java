package Model;

// تأكد من مسار الـ Visitor الصحيح
import service.StatsVisitor;

public class Video extends Media {
    private int dureeMinutes;

    // قمت بإزالة 'matiere' لأنه لم يكن مستخدماً، وقمت بتمرير "video" تلقائياً للأب
    public Video(int id, String titre, String auteur, int annee, String desc, int dureeMinutes) {
        // نمرر "video" كأول معامل ليتم حفظ النوع في كلاس Media
        // ملاحظة: تأكد أن ترتيب المعاملات هنا يطابق ترتيب Constructor في كلاس Media
        super("video", id, titre, auteur, annee, desc, dureeMinutes);
        this.dureeMinutes = dureeMinutes;
    }

    // إضافة @Override ممارسة جيدة

    public void accept(Visitor v) {
        v.visitVideo(this);
    }

    public int getDureeMinutes() {
        return dureeMinutes;
    }

    // دالة toString مخصصة للفيديو (اختياري لكن مفضل)
    @Override
    public String toString() {
        return super.toString() + " [Duration: " + dureeMinutes + " mins]";
    }

    public int getDuree() {
        return dureeMinutes;
    }

    public void setDuree(int duree) {
        this.dureeMinutes = duree;
    }
}