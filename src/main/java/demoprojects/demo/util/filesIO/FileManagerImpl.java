package demoprojects.demo.util.filesIO;

import demoprojects.demo.dao.models.entities.FileFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;


@Component
public class FileManagerImpl implements FileManager {
    private static final BigDecimal ONE_BYTE_TO_MB =  BigDecimal.valueOf(0.00000095367432);
    private static final Integer MAX_BYTES =  5242880;
    @Override
    public boolean isFileSizeCorrect(MultipartFile file) {
        return file.getSize()*ONE_BYTE_TO_MB.doubleValue() <= MAX_BYTES;
    }

    @Override
    public boolean isFileFormatCorrect(MultipartFile file) {

        return file.getContentType().contains(FileFormat.jpeg.name()) ||
                file.getContentType().contains(FileFormat.jpg.name()) ||
                file.getContentType().contains(FileFormat.png.name());
    }

    @Override
    public boolean isFileSizeCorrect(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if(!isFileSizeCorrect(file)){
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isFileFormatCorrect(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            boolean check = file.getContentType().contains(FileFormat.jpeg.name()) ||
                    file.getContentType().contains(FileFormat.jpg.name()) ||
                    file.getContentType().contains(FileFormat.png.name());
            if (!check){
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isFileCountCorrect(List<MultipartFile> files) {
        return files.size() <= 5;
    }
}
