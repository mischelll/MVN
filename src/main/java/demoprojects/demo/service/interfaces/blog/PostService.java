package demoprojects.demo.service.interfaces.blog;

import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.service.models.view.*;
import demoprojects.demo.service.models.bind.PostCreateServiceModel;

import java.util.List;

public interface PostService {

    List<PostViewServiceModel> findLatest10();

    PostViewServiceModel findById(String id);

    PostCreateServiceModel create(PostCreateServiceModel post);

    PostCreateServiceModel edit(PostCreateServiceModel post, String postId);

    void deleteById(String id);

    PostCategoryCountModel findPostsByCategory(String category);

    List<PostPopularViewModel> getTopThreePosts();

    List<PostViewServiceModel> findByTitle(String title);

    Post findByIdentificationNumber(String id);

    List<PostViewServiceModel> findByCategories(String category);

    List<PostResponseModel> listAll();

    List<PostViewServiceModel> listOneDayOldPosts();
}
