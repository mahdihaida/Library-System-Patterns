package service;

import Model.Media;
import java.util.List;

public class XMLExporter implements ExportStrategy {
    public void export(List<Media> medias) {
        System.out.println(">>> Exporting to XML format...");
        System.out.println("<catalogue>");

        for (Media m : medias) {
            System.out.println("  <media type='" + m.getClass().getSimpleName() + "'>");
            System.out.println("    <id>" + m.getId() + "</id>");
            System.out.println("    <titre>" + m.getTitre() + "</titre>");
            System.out.println("    <auteur>" + m.getAuteur() + "</auteur>");
            System.out.println("  </media>");
        }

        System.out.println("</catalogue>");
    }
}