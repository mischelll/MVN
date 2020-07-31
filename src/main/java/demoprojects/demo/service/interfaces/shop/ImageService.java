package demoprojects.demo.service.interfaces.shop;

import demoprojects.demo.service.models.bind.ChangeAvatarServiceModel;
import demoprojects.demo.service.models.bind.ProductImageCreateServiceModel;
import demoprojects.demo.service.models.bind.UserAvatarServiceModel;

import java.util.List;

public interface ImageService {
    void uploadProductsPicture(ProductImageCreateServiceModel productImage, String productId);

    void uploadUserAvatar(ChangeAvatarServiceModel avatar, String userId);
}
