package service;

import Model.*;

public class StatsVisitor implements Visitor {
    private int countVideo = 0;
    private int countDocument = 0;
    private int countQuiz = 0;
    private int totalDureeVideo = 0;


    public void visitMedia(Media media) {
        // Double dispatch - delegate to specific visit method
        media.accept(this);
    }

    @Override
    public void visitVideo(Video video) {
        countVideo++;
        totalDureeVideo += video.getDuree();
    }

    @Override
    public void visitDocument(Document doc) {
        countDocument++;
    }

    @Override
    public void visitQuiz(Quiz quiz) {
        countQuiz++;
    }

    public void afficherResultats() {
        System.out.println("=== Statistiques de la Bibliothèque ===");
        System.out.println("Nombre de Vidéos   : " + countVideo + " (Total durée: " + totalDureeVideo + " mins)");
        System.out.println("Nombre de Documents: " + countDocument);
        System.out.println("Nombre de Quiz     : " + countQuiz);
        System.out.println("=======================================");
    }

    // Proper encapsulation with private fields and public getters
    public int getCountVideo() { return countVideo; }
    public int getCountDocument() { return countDocument; }
    public int getCountQuiz() { return countQuiz; }
    public int getTotalDureeVideo() { return totalDureeVideo; }
    
    // Reset method for reuse
    public void reset() {
        countVideo = 0;
        countDocument = 0;
        countQuiz = 0;
        totalDureeVideo = 0;
    }
}
