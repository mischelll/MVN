package demoprojects.demo.service;

import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.service.models.view.PostCategoryCountModel;
import demoprojects.demo.service.models.bind.PostCreateServiceModel;
import demoprojects.demo.service.models.view.PostPopularViewModel;
import demoprojects.demo.service.models.view.PostViewServiceModel;

import java.util.List;

public interface PostService {
    List<PostViewServiceModel> findAll();
    List<PostViewServiceModel> findLatest10();
    PostViewServiceModel findById(String id);
    PostCreateServiceModel create(PostCreateServiceModel post);
    Post edit(Post post);
    void deleteById(String id);
    PostCategoryCountModel findPostsByCategory(String category);
    List<PostPopularViewModel> getTopThreePosts();
}
