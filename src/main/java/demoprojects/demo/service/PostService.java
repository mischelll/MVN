package demoprojects.demo.service;

import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.service.models.PostCreateServiceModel;
import demoprojects.demo.service.models.PostViewServiceModel;

import java.util.List;

public interface PostService {
    List<PostViewServiceModel> findAll();
    List<PostViewServiceModel> findLatest10();
    Post findById(Long id);
    PostCreateServiceModel create(PostCreateServiceModel post);
    Post edit(Post post);
    void deleteById(String id);
}
