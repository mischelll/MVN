package demoprojects.demo.service.impl.blog;

import demoprojects.demo.dao.models.entities.PostCategory;
import demoprojects.demo.dao.models.entities.PostCategoryName;
import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.blog.PostRepository;
import demoprojects.demo.dao.repositories.user.UserRepository;
import demoprojects.demo.service.interfaces.blog.PostCategoryService;
import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.models.view.PostCategoryCountModel;
import demoprojects.demo.service.models.bind.PostCreateServiceModel;
import demoprojects.demo.service.models.view.PostPopularViewModel;
import demoprojects.demo.service.models.view.PostResponseModel;
import demoprojects.demo.service.models.view.PostViewServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PostCategoryService categoryService;


    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper,
                           UserRepository userRepository, PostCategoryService categoryService) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.categoryService = categoryService;


    }


    @Override
    public List<PostViewServiceModel> findLatest10() {
        return this.postRepository
                .findTop9ByOrderByPostedOnDesc()
                .stream()
                .filter(post -> post.getAuthor().isEnabled())
                .map(this::mapViewModel)
                .collect(Collectors.toList());
    }

    private PostViewServiceModel mapViewModel(Post post) {
        PostViewServiceModel map = this.modelMapper.map(post, PostViewServiceModel.class);
        map.setCategories(post.getCategories().iterator().next().getName());
        map.setCommentsCount(post.getComments().size());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        map.setPostedOn(post.getPostedOn().format(formatter));
        return map;
    }

    @Override
    public PostViewServiceModel findById(String id) {
        Post byId = this.postRepository.findById(id).orElse(null);
        assert byId != null;
        PostViewServiceModel map = this.modelMapper.map(byId, PostViewServiceModel.class);
        map.setCommentsCount(byId.getComments().size());
        map.setAuthor(byId.getAuthor().getUsername());
        map.setAuthorBio(byId.getAuthor().getBio());
        if (byId.getAuthor().getAvatar() != null) {
            map.setAuthorImgUrl(byId.getAuthor().getAvatar().getImgUrl());
        }
        return map;
    }

    @Override
    public PostCreateServiceModel create(PostCreateServiceModel post) {
        Post map = this.modelMapper.map(post, Post.class);
        User byUsername = this.userRepository.findByUsername(post.getAuthor()).orElse(null);
        if (byUsername == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        map.setAuthor(byUsername);
        map.setPostedOn(LocalDateTime.now());
        return getPostCreateServiceModel(post, map);
    }

    @Override
    public PostCreateServiceModel edit(PostCreateServiceModel post, String postId) {
        Post post1 = this.postRepository.findById(postId).orElse(null);
        Post editPost = this.modelMapper.map(post, Post.class);
        assert post1 != null;
        post1.setContent(editPost.getContent());
        post1.setTitle(editPost.getTitle());
        post1.setImgUrl(editPost.getImgUrl());
        post1.setPreview(editPost.getPreview());

        return getPostCreateServiceModel(post, post1);
    }

    private PostCreateServiceModel getPostCreateServiceModel(PostCreateServiceModel post, Post post1) {
        Set<PostCategory> categories = new HashSet<>();
        post.getCategory().forEach(categoryName -> categories.add(this.categoryService.findByName(categoryName.name())));
        post1.setCategories(categories);
        return this.modelMapper
                .map(this.postRepository.saveAndFlush(post1),
                        PostCreateServiceModel.class);
    }


    @Override
    public void deleteById(String id) {
        Post post = this.postRepository.findById(id).orElse(null);
        assert post != null;
        post.setCategories(null);
        post.setComments(null);
        post.setAuthor(null);
        this.postRepository.saveAndFlush(post);
        this.postRepository.deleteById(id);
    }

    @Override
    public PostCategoryCountModel findPostsByCategory(String category) {
        PostCategory byName = this.categoryService.findByName(category);
        List<Post> collect = this.postRepository
                .findAll()
                .stream()
                .filter(post -> {
                    boolean equals = false;

                    for (PostCategory postCategory : post.getCategories()) {
                        equals = postCategory.getName().name().equals(byName.getName().name());
                    }
                    return equals;
                }).collect(Collectors.toList());
        return new PostCategoryCountModel(collect.size(), category);
    }

    @Override
    public List<PostPopularViewModel> getTopThreePosts() {
        return this.postRepository
                .findAll()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getComments().size(), a.getComments().size()))
                .limit(3)
                .map(post -> {
                    PostPopularViewModel map = this.modelMapper.map(post, PostPopularViewModel.class);
                    map.setCommentsCount(post.getComments().size());
                    map.setAuthor(post.getAuthor().getUsername());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
                    map.setPostedOn(post.getPostedOn().format(formatter));
                    return map;
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<PostViewServiceModel> findByTitle(String title) {
        return this.postRepository
                .findAllByTitleContainsOrderByPostedOnDesc(title)
                .stream()
                .map(this::mapViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public Post findByIdentificationNumber(String id) {
        return this
                .postRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public List<PostViewServiceModel> findByCategories(String category) {
        PostCategory byName = this.categoryService.findByName(category);
        return this.postRepository
                .findAll()
                .stream()
                .filter(post -> {
                    boolean equals = false;
                    for (PostCategory postCategory : post.getCategories()) {
                        equals = postCategory.getName().name().equals(byName.getName().name());
                    }
                    return equals;
                })
                .map(post -> {
                    PostViewServiceModel map = this.modelMapper.map(post, PostViewServiceModel.class);
                    map.setCommentsCount(post.getComments().size());
                    map.setAuthor(post.getAuthor().getUsername());
                    map.setCategories(PostCategoryName.valueOf(category));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
                    map.setPostedOn(post.getPostedOn().format(formatter));
                    return map;
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<PostResponseModel> listAll() {
        return postRepository
                .findAll()
                .stream()
                .map(post -> {
                    PostResponseModel mapPost = this.modelMapper.map(post, PostResponseModel.class);
                    mapPost.setAuthor(post.getAuthor().getUsername());
                    mapPost.setPostedOn(post.getPostedOn().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy HH:mm")));
                    mapPost.setCommentsCount(post.getComments().size());
                    mapPost.setCategories(post
                            .getCategories()
                            .stream()
                            .map(category -> category.getName().name())
                            .collect(Collectors.joining(", ")));

                    return mapPost;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PostViewServiceModel> listOneDayOldPosts() {
        return this.postRepository.findOneDayOldPosts().stream().map(post -> {
            PostViewServiceModel map = this.modelMapper.map(post, PostViewServiceModel.class);
            map.setAuthor(post.getAuthor().getUsername());

            return map;
        }).collect(Collectors.toList());

    }
}
