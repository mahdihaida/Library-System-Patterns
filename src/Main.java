import Model.Media;
import View.LoginFrame;
import service.LibrarySystem;
import service.MediaFactory;
import service.UniversityXMLLoader;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println(">>> Démarrage de l'application...");

        try {
            UniversityXMLLoader.loadStudentsFromXML();
            System.out.println("-> Données XML chargées avec succès.");
        } catch (Exception e) {
            System.err.println("-> Erreur lors du chargement XML: " + e.getMessage());
        }

        // 2. إضافة بيانات الميديا التجريبية
        initialiserDonneesTest();

        // 3. طباعة محتوى المكتبة للتأكد (اختياري - للتأكد من أن البيانات صحيحة)
        LibrarySystem.getInstance().getAllMedia().forEach(System.out::println);

        // 4. تشغيل الواجهة الرسومية
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true); // تأكد أن LoginFrame يرث من JFrame
        });
    }

    private static void initialiserDonneesTest() {
        LibrarySystem library = LibrarySystem.getInstance();

        // منع التكرار: إذا كانت المكتبة ممتلئة لا تضف شيئاً
        if (!library.getAllMedia().isEmpty()) {
            System.out.println("-> Les données existent déjà, initialisation ignorée.");
            return;
        }

        System.out.println("-> Initialisation des données de test...");
        try {
            // إضافة فيديو
            Media m1 = MediaFactory.createMedia("video", 1, "Java Streams API", "Oracle", 2024, "Cours avancé", 45);
            m1.ajouterCodeMatiere("NFP121");
            m1.ajouterCodeMatiere("NFA032");
            library.addMedia(m1);

            // إضافة كتاب/وثيقة
            Media m2 = MediaFactory.createMedia("document", 2, "UML Design Patterns", "Gang of Four", 2020, "Livre ref", 300);
            m2.ajouterCodeMatiere("NFA035");
            library.addMedia(m2);

            // إضافة فيديو آخر
            Media m3 = MediaFactory.createMedia("video", 3, "TCP/IP Protocol", "Cisco", 2023, "Réseaux", 60);
            m3.ajouterCodeMatiere("NFA042");
            library.addMedia(m3);

        } catch (Exception e) {
            System.err.println("Erreur lors de la création des médias : " + e.getMessage());
            e.printStackTrace();
        }
    }
}