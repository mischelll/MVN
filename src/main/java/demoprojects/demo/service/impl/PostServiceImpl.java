package demoprojects.demo.service.impl;

import demoprojects.demo.dao.models.entities.Category;
import demoprojects.demo.dao.models.entities.CategoryName;
import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.dao.repositories.PostRepository;
import demoprojects.demo.dao.repositories.UserRepository;
import demoprojects.demo.service.CategoryService;
import demoprojects.demo.service.PostService;
import demoprojects.demo.service.models.PostCreateServiceModel;
import demoprojects.demo.service.models.PostViewServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryService categoryService;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, UserRepository userRepository, CategoryService categoryService) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<PostViewServiceModel> findAll() {
        List<PostViewServiceModel> posts = new ArrayList<>();
        List<Post> all = this.postRepository.findAll();

        all.forEach(post -> {
            PostViewServiceModel viewServiceModel =
                    this.modelMapper
                            .map(post,
                                    PostViewServiceModel.class);

            viewServiceModel.setAuthor(post.getAuthor().getUsername());
            posts.add(viewServiceModel);
        });

        return posts;
    }

    @Override
    public List<PostViewServiceModel> findLatest10() {
        return this.postRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Post::getPostedOn).reversed())
                .limit(9)
                .map(post -> {
                    PostViewServiceModel map = this.modelMapper.map(post, PostViewServiceModel.class);
                    map.setCategories(post.getCategories().iterator().next().getName());
                    map.setCommentsCount(post.getComments().size());

                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PostViewServiceModel findById(String id) {
        Post byId = this.postRepository.findById(id).orElse(null);
        PostViewServiceModel map = this.modelMapper.map(byId, PostViewServiceModel.class);
        map.setCommentsCount(byId.getComments().size());
        map.setAuthor(byId.getAuthor().getUsername());
        return map;
    }

    @Override
    public PostCreateServiceModel create(PostCreateServiceModel post) {
        Post map = this.modelMapper.map(post, Post.class);

        map.setAuthor(this.userRepository.findByUsername(post.getAuthor()));
        map.setPostedOn(LocalDateTime.now());


        map.setCategories(Set.of(this.
                categoryService.
                findByName(post.
                        getCategory()
                        .name())));

        return this.modelMapper
                .map(this.postRepository.saveAndFlush(map),
                        PostCreateServiceModel.class);
    }

    @Override
    public Post edit(Post post) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<PostViewServiceModel> findPostsByCategory(String category) {
        Category byName = this.categoryService.findByName(category);
        List<Post> collect = this.postRepository
                .findAll()
                .stream()
               .collect(Collectors.toList());
        return this.postRepository
                .findAll()
                .stream()
                .filter(post -> {
                    boolean equals = false;
                    for (Category postCategory : post.getCategories()) {
                        equals = postCategory.getName().name().equals(byName.getName().name());
                    }

                    return equals;
                })
                .map(post -> this.modelMapper.map(post,PostViewServiceModel.class))
                .collect(Collectors.toList());
    }
}
