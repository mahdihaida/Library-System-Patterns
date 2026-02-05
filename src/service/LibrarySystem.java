package service;

import Model.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import java.util.List;
import java.util.stream.Collectors;
public class LibrarySystem implements Subject {

    private static LibrarySystem instance;
    private List<Media> catalogue;
    private List<User> users;
    private List<Observer> observers;
    private static final String FILE_NAME = "catalogue.txt";

    private LibrarySystem() {
        catalogue = new ArrayList<>();
        users = new ArrayList<>();
        observers = new ArrayList<>();
        users.add(new Admin("admin", "admin"));
        loadStudentsFromXML();
        loadCatalogueFromFile(); // Load media from text file
    }

    public static synchronized LibrarySystem getInstance() {
        if (instance == null) {
            instance = new LibrarySystem();
        }
        return instance;
    }

    // ... (Observer & Add methods remain similar, will ensure saveMediaToFile is correct below) ...

    public void addObserver(Observer o) {
        if (!observers.contains(o)) observers.add(o);
    }

    public void removeObserver(Observer o) { observers.remove(o); }

    public void notifyObservers(Media m) {
        for (Observer o : observers) {
            o.update(m);
        }
    }

    public void addMedia(Media m) {
        // Simple duplicate check (ID check)
        for(Media existing : catalogue) {
            if(existing.getId() == m.getId()) {
                // Auto-increment ID if duplicate or 0
                int newId = getNextId();
                m.setId(newId);
                break;
            }
        }
        if(m.getId() == 0) {
            m.setId(getNextId());
        }

        catalogue.add(m);
        saveToCatalogueFile();
        notifyObservers(m);
    }

    private int getNextId() {
        int max = 0;
        for(Media m : catalogue) if(m.getId() > max) max = m.getId();
        return max + 1;
    }

    public List<Media> getCatalogue() { return catalogue; }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    // ... (loadStudentsFromXML remains the same) ...
    private void loadStudentsFromXML() {
        try {
            File xmlFile = new File("universite.xml");
            if (!xmlFile.exists()) return;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("etudiant");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String user = element.getAttribute("username");
                    String pass = element.getAttribute("password");
                    Student s = new Student(user, pass);

                    // Check parent specialite if available (omitted for brevity, assuming direct list)
                    // Just get <valeur> directly
                    NodeList valeurs = element.getElementsByTagName("valeur");
                    for (int j = 0; j < valeurs.getLength(); j++) {
                        s.addSubject(valeurs.item(j).getTextContent());
                    }
                    users.add(s);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadCatalogueFromFile() {
        File file = new File("catalogue.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {

                // تجاهل الأسطر الفاسدة
                if (line.trim().isEmpty() || line.startsWith("[C@")) continue;

                String[] parts = line.split(";");
                if (parts.length != 8) continue; // مهم: يجب أن يكون 8 بالضبط

                String type = parts[0].trim();
                int id = Integer.parseInt(parts[1].trim());
                String titre = parts[2].trim();
                String auteur = parts[3].trim();
                int annee = Integer.parseInt(parts[4].trim());
                String desc = parts[5].trim();

                // ==== معالجة المواد ====
                String matieresStr = parts[6].trim(); // مثال: [NFP121, NFA032]
                matieresStr = matieresStr.replace("[", "").replace("]", "");
                String[] codes = matieresStr.split(",");

                // ==== معالجة extra ====
                String extraStr = parts[7].trim();
                Media m = null;

                if (type.equalsIgnoreCase("video")) {
                    int duree = extractNumber(extraStr);
                    m = new Model.Video(id, titre, auteur, annee, desc, duree);

                } else if (type.equalsIgnoreCase("document")) {
                    int pages = extractNumber(extraStr);
                    m = new Model.Document(id, titre, auteur, annee, desc, pages);

                } else if (type.equalsIgnoreCase("quiz")) {
                    m = new Model.Quiz(id, titre, auteur, annee, desc, extraStr);
                }

                if (m != null) {
                    for (String code : codes) {
                        code = code.trim();
                        if (!code.isEmpty()) {
                            m.ajouterCodeMatiere(code);
                        }
                    }
                    catalogue.add(m);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading catalogue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int extractNumber(String extraStr) {
        try {
            return Integer.parseInt(extraStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public String[] getAllMatiereCodes() {
        Set<String> codes = new HashSet<>();

        // Safety check
        if (users != null) {
            for (User u : users) {
                if (u instanceof Student) {
                    Student s = (Student) u;

                    if (s.getSubscribedSubjects() != null) {
                        codes.addAll(s.getSubscribedSubjects());
                    }
                }
            }
        }

        // Add hardcoded defaults if empty
        if (codes.isEmpty()) {
            return new String[]{"NFP121", "NFA035", "NFA032", "NFA007"};
        }

        return codes.toArray(new String[0]);
    }


    // Interface methods implementation
    public void attach(Observer o) { addObserver(o); }
    public void detach(Observer o) { removeObserver(o); }

//    public boolean updateMedia(Media m) {
//        if (m == null) return false;
//
//        for (int i = 0; i < catalogue.size(); i++) {
//            if (catalogue.get(i).getId() == m.getId()) {
//                catalogue.set(i, m);        // 1️⃣ تحديث داخل الذاكرة
//                saveToCatalogueFile();      // 2️⃣ إعادة كتابة الملف كامل
//                notifyObservers(m);         // 3️⃣ تحديث الواجهات
//                return true;
//            }
//        }
//        return false;
//    }


    public boolean deleteMedia(int id) {
        return catalogue.removeIf(m -> m.getId() == id);

    }
// في ملف service/LibrarySystem.java


    public String[] getMatiereCodesFromXML() {
        // نستخدم Set لمنع تكرار اسم المادة
        Set<String> codesSet = new HashSet<>();

        try {
            File file = new File("universite.xml"); // تأكد أن الاسم مطابق لملفك
            if (!file.exists()) {
                return new String[]{}; // إرجاع مصفوفة فارغة إذا الملف غير موجود
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            // جلب جميع العناصر التي اسمها <valeur> بغض النظر عن مكانها
            NodeList nList = doc.getElementsByTagName("valeur");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String code = node.getTextContent().trim();
                    if (!code.isEmpty()) {
                        codesSet.add(code); // الإضافة للـ Set تمنع التكرار تلقائياً
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // تحويل الـ Set إلى مصفوفة String
        return codesSet.toArray(new String[0]);
    }
    public List<Observer> getObservers() {
        return observers;
    }

    public Collection<Media> getAllMedia() {
        return catalogue;
    }


    public void saveToCatalogueFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("catalogue.txt"))) {

            for (Media m : catalogue) {

                String extra = "0";
                if (m instanceof Video) {
                    extra = String.valueOf(((Video) m).getDuree());
                } else if (m instanceof Model.Document) {
                    extra = String.valueOf(((Model.Document) m).getNombrePages());
                } else if (m instanceof Quiz) {
                    extra = ((Quiz) m).getNiveau();
                }

                // ✅ حفظ كل المواد
                String matieres = "[" + String.join(", ", m.getCodesMatieres()) + "]";

                pw.println(
                        m.getType() + ";" +
                                m.getId() + ";" +
                                m.getTitre() + ";" +
                                m.getAuteur() + ";" +
                                m.getAnnee() + ";" +
                                m.getDescription() + ";" +
                                matieres + ";" +
                                extra
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean updateMedia(Media updated) {
        for (int i = 0; i < catalogue.size(); i++) {
            Media m = catalogue.get(i);
            if (m.getId() == updated.getId()) {
                // تحديث الحقول الأساسية
                m.setTitre(updated.getTitre());
                m.setAuteur(updated.getAuteur());
                m.setAnnee(updated.getAnnee());
                m.setDescription(updated.getDescription());

                // تحديث الأكواد (الآن هي List<String>)
                m.setCodesMatieres(updated.getCodesMatieres());

                // تحديث الخاصية الخاصة بالنوع
                if (m instanceof Video && updated instanceof Video) {
                    ((Video) m).setDuree(((Video) updated).getDuree());
                } else if (m instanceof Model.Document && updated instanceof Model.Document) {
                    ((Model.Document) m).setNombrePages(((Model.Document) updated).getNombrePages());
                } else if (m instanceof Quiz && updated instanceof Quiz) {
                    ((Quiz) m).setNiveau(((Quiz) updated).getNiveau());
                }

                // إرسال الإشعارات للطلاب المسجلين في المواد المحدثة فقط
                notifyObservers(m);

                // حفظ التعديلات إلى الملف
                saveToCatalogueFile();

                return true;
            }
        }
        return false; // لم نجد الـ ID
    }


    public List<Media> getMediaByMatiere(String codeMatiere) {
        if (codeMatiere == null || codeMatiere.isEmpty()) {
            return Collections.emptyList();
        }

        return catalogue.stream()
                .filter(m -> m.getCodesMatieres() != null &&
                        m.getCodesMatieres().stream()
                                .anyMatch(c -> c.equalsIgnoreCase(codeMatiere)))
                .collect(Collectors.toList());
    }


}