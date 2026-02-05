package Model;

// تأكد من مسار Visitor الصحيح (غالباً في service)
import service.StatsVisitor;

public class Document extends Media {
    private int nbPages;

    // 1. حذفنا 'matiere' غير المستخدمة
    // 2. مررنا 'int nbPages' ليتم استخدامه هنا
    public Document(int id, String titre, String auteur, int annee, String desc, int nbPages) {

        // 3. نمرر "document" كثابت إلى الأب (Media)
        // ملاحظة: تأكد أن ترتيب المعاملات هنا يطابق Constructor كلاس Media لديك
        super("document", id, titre, auteur, annee, desc, nbPages);

        this.nbPages = nbPages;
    }

    // يفضل استخدام toString للطباعة، لكن هذه الدالة مقبولة إذا كان لديك منطق خاص
    protected void afficherDetailsSpecifiques() {
        System.out.println("[DOCUMENT] Pages: " + nbPages);
    }


    public void accept(Visitor v) {
        v.visitDocument(this);
    }

    // 4. ابق على دالة واحدة فقط للوصول لعدد الصفحات
    public int getNbPages() {
        return nbPages;
    }

    public int getNombrePages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    @Override
    public String toString() {
        return super.toString() + " [Pages: " + nbPages + "]";
    }

    public void setNombrePages(int nombrePages) {
        this.nbPages = nombrePages;
    }
}