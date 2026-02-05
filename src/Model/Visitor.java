package Model;

// Visitor interface following proper Visitor pattern
// This interface should be in Model package to avoid circular dependencies
public interface Visitor {
    void visitMedia(Media media);
    void visitVideo(Video video);
    void visitDocument(Document doc);
    void visitQuiz(Quiz quiz);
}
