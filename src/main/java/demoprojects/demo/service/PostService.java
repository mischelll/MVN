package demoprojects.demo.service;

import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.service.models.PostServiceModel;
import demoprojects.demo.service.models.PostViewServiceModel;

import java.util.List;

public interface PostService {
    List<PostViewServiceModel> findAll();
    List<Post> findLatest10();
    Post findById(Long id);
    Post create(PostServiceModel post);
    Post edit(Post post);
    void deleteById(String id);
}
