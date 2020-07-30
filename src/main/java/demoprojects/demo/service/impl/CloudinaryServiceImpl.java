package demoprojects.demo.service.impl;

import com.cloudinary.Cloudinary;
import demoprojects.demo.service.interfaces.CloudinaryService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
}
