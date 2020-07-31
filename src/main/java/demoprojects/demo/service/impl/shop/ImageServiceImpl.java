package demoprojects.demo.service.impl.shop;

import demoprojects.demo.dao.models.entities.FileFormat;
import demoprojects.demo.dao.models.entities.Image;
import demoprojects.demo.dao.models.entities.Product;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.shop.ProductRepository;
import demoprojects.demo.dao.repositories.user.ImageRepository;
import demoprojects.demo.dao.repositories.user.UserRepository;
import demoprojects.demo.service.interfaces.shop.ImageService;
import demoprojects.demo.service.models.bind.ChangeAvatarServiceModel;
import demoprojects.demo.service.models.bind.ProductImageCreateServiceModel;
import demoprojects.demo.service.models.bind.UserAvatarServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ImageServiceImpl(ImageRepository imageRepository, ProductRepository productRepository, UserRepository userRepository, ModelMapper mapper) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public void uploadProductsPicture(ProductImageCreateServiceModel productImage, String productId) {
        Image map = this.mapper.map(productImage, Image.class);
        String[] split = productImage.getFormat().split("/");
        map.setFormat(FileFormat.valueOf(split[1]));

        Product product = this.productRepository.findById(productId).orElse(null);
        assert product != null;
        product.getProductImages().add(this.imageRepository.saveAndFlush(map));

        this.productRepository.saveAndFlush(product);
    }

    @Override
    public void uploadUserAvatar(ChangeAvatarServiceModel avatar, String userId) {
        Image map = this.mapper.map(avatar, Image.class);
        String[] split = avatar.getFormat().split("/");
        map.setFormat(FileFormat.valueOf(split[1]));

        User user = this.userRepository.findById(userId).orElse(null);
        assert user != null;
        user.setAvatar(this.imageRepository.saveAndFlush(map));

        this.userRepository.saveAndFlush(user);
    }
}
