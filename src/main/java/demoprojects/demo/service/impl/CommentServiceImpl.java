package demoprojects.demo.service.impl;

import demoprojects.demo.dao.models.entities.Comment;
import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.CommentRepository;
import demoprojects.demo.service.CommentService;
import demoprojects.demo.service.PostService;
import demoprojects.demo.service.UserService;
import demoprojects.demo.service.models.bind.CommentCreateServiceModel;
import demoprojects.demo.service.models.view.CommentViewServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostService postService, UserService userService, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public CommentCreateServiceModel createComment(CommentCreateServiceModel comment) {
        Post byId = this.postService.findByIdentificationNumber(comment.getPostID());
        User byUsername = this.userService.findByUsername(comment.getAuthor());
        Comment comment1 = new Comment();
        comment1.setAuthor(byUsername);
        comment1.setDate(comment.getDate());
        comment1.setPost(byId);
        comment1.setText(comment.getText());

        return this
                .mapper
                .map(this.commentRepository
                                .saveAndFlush(comment1),
                        CommentCreateServiceModel.class);
    }

    @Override
    public List<CommentViewServiceModel> commentsOfPost(String postId) {
        return this.commentRepository
                .findAll()
                .stream()
                .filter(comment -> comment.getPost().getId().equals(postId))
                .sorted((a, b) -> {
                    int sort = Integer.compare(b.getLikes(), a.getLikes());
                    if (sort == 0) {
                        return a.getDate().compareTo(b.getDate());
                    }

                    return sort;
                })
                .map(comment -> {
                    CommentViewServiceModel map = this.mapper.map(comment,
                            CommentViewServiceModel.class);

                    map.setAuthor(comment.getAuthor().getUsername());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
                    map.setDate(comment.getDate().format(formatter));
                    map.setPostID(postId);

                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String commentId, String postId) {

        this.commentRepository.deleteById(commentId);
    }

    @Override
    public void like(String commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElse(null);
        assert comment != null;
        comment.setLikes(comment.getLikes() + 1);

        this.commentRepository.saveAndFlush(comment);
    }

    @Override
    public void dislike(String commentId) {
        Comment comment = this.commentRepository.findById(commentId).orElse(null);
        assert comment != null;
        comment.setDislikes(comment.getDislikes() + 1);

        this.commentRepository.saveAndFlush(comment);
    }
}
