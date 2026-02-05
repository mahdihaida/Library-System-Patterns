package service;

import Model.Media;
import java.util.List;

public interface ExportStrategy {
    void export(List<Media> data);
}
