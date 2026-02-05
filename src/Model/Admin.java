package Model;

import service.ExportStrategy;
import service.LibrarySystem;

public class Admin extends User {

    public Admin(String username, String password) {
        super(username, password);
    }

    // دالة إضافة ميديا: تستخدم الـ Singleton
    public void ajouterMedia(Media m) {
        System.out.println("Admin " + username + " ajoute un média...");
        // استدعاء النسخة الوحيدة من النظام
        LibrarySystem.getInstance().addMedia(m);
    }

    // دالة التصدير: تستخدم نمط Strategy
    public void exporterDonnees(ExportStrategy strategy) {
        System.out.println("Exportation en cours...");
        // نمرر قائمة الميديا الحالية للاستراتيجية المختارة
        strategy.export(LibrarySystem.getInstance().getCatalogue());
    }

}
