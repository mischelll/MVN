package demoprojects.demo.service.impl;

import com.cloudinary.Cloudinary;
import demoprojects.demo.service.interfaces.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    @Override
    public String upload(MultipartFile image) throws IOException {

                File file = File.createTempFile("temp-file", image.getOriginalFilename());
                image.transferTo(file);
        return   this.cloudinary
                        .uploader()
                        .upload(file, new HashMap())
                        .get("url")
                        .toString();

    }

    @Override
    public boolean isFileSizeCorrect(MultipartFile image) {
        return image.getSize() * 0.00000095367432 <= 5000;
    }

    @Override
    public boolean isFileFormatCorrect(MultipartFile image) {
        return image.getContentType().contains("jpeg") ||
                image.getContentType().contains("jpg") ||
                image.getContentType().contains("png");
    }
}
