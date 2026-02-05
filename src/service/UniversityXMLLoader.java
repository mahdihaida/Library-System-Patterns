package service;

import Model.*;
import org.w3c.dom.*;
import org.w3c.dom.Document;

import javax.xml.parsers.*;
import java.io.File;

public class UniversityXMLLoader {

    public static void loadStudentsFromXML() {
        try {
            File inputFile = new File("universite.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // 1. نبدأ من التخصصات (specialite)
            NodeList specList = doc.getElementsByTagName("specialite");

            for (int i = 0; i < specList.getLength(); i++) {
                Node specNode = specList.item(i);
                if (specNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element specElement = (Element) specNode;
                    String specName = specElement.getAttribute("nom"); // اسم التخصص
                    Specialite specialite = new Specialite(specName, specName); // ننشئ كائن التخصص

                    // 2. داخل كل تخصص، نقرأ الطلاب (etudiant)
                    NodeList etudiantList = specElement.getElementsByTagName("etudiant");
                    for (int j = 0; j < etudiantList.getLength(); j++) {
                        Element etuElement = (Element) etudiantList.item(j);

                        String username = etuElement.getAttribute("username");
                        String password = etuElement.getAttribute("password");

                        Student student = new Student(username, password);
                        student.setSpecialite(specialite); // نربط الطالب بالتخصص

                        // 3. نقرأ مواد الطالب (valeur)
                        NodeList valeurList = etuElement.getElementsByTagName("valeur");
                        for (int k = 0; k < valeurList.getLength(); k++) {
                            String codeMatiere = valeurList.item(k).getTextContent().trim();
                            // ننشئ المادة ونضيفها لقائمة مواد الطالب والتخصص
                            Matiere matiere = new Matiere(codeMatiere, codeMatiere); // الاسم والكود نفس الشيء مؤقتاً

                            student.ajouterMatiere(matiere); // إضافة للطالب
                            specialite.ajouterMatiere(matiere); // إضافة للتخصص (لتجميع كل المواد)
                        }

                        // إضافة الطالب للنظام
                        LibrarySystem.getInstance().attach(student);
                    }
                }
            }
            System.out.println(">>> Chargement XML terminé selon le format ISSAE.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}