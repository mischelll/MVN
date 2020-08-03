package demoprojects.demo.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CloudinaryService {
    void delete(String url);

    String upload(MultipartFile image) throws IOException;

    boolean isFileSizeCorrect(MultipartFile image);

    boolean isFileFormatCorrect(MultipartFile image);
}
