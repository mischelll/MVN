package demoprojects.demo.service.impl;

import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.dao.repositories.PostRepository;
import demoprojects.demo.dao.repositories.UserRepository;
import demoprojects.demo.service.PostService;
import demoprojects.demo.service.models.PostServiceModel;
import demoprojects.demo.service.models.PostViewServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<PostViewServiceModel> findAll() {
        List<PostViewServiceModel> posts = new ArrayList<>();
        List<Post> all = this.postRepository.findAll();
        all.forEach(post -> {
            PostViewServiceModel viewServiceModel = this.modelMapper.map(post, PostViewServiceModel.class);
            viewServiceModel.setAuthor(post.getAuthor().getUsername());
            posts.add(viewServiceModel);
        });

        return posts;
    }

    @Override
    public List<Post> findLatest5() {
        return null;
    }

    @Override
    public Post findById(Long id) {
        return null;
    }

    @Override
    public Post create(PostServiceModel post) {
        Post map = this.modelMapper.map(post, Post.class);
        map.setAuthor(this.userRepository.findByUsername(post.getAuthor()));

        map.setPostedOn(LocalDateTime.now());

        return this.postRepository.saveAndFlush(map);
    }

    @Override
    public Post edit(Post post) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
