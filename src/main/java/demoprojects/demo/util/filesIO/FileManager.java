package demoprojects.demo.util.filesIO;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileManager {
    boolean isFileSizeCorrect(MultipartFile file);
    boolean isFileFormatCorrect(MultipartFile file);
    boolean isFileSizeCorrect(List<MultipartFile> files);
    boolean isFileFormatCorrect(List<MultipartFile> files);
    boolean isFileCountCorrect(List<MultipartFile> files);
}
