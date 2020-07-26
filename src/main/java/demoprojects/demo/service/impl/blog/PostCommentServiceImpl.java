package demoprojects.demo.service.impl.blog;

import demoprojects.demo.dao.models.entities.PostComment;
import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.dao.models.entities.User;
import demoprojects.demo.dao.repositories.blog.PostCommentRepository;
import demoprojects.demo.service.interfaces.blog.PostCommentService;
import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.bind.CommentCreateServiceModel;
import demoprojects.demo.service.models.view.CommentViewServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostCommentServiceImpl implements PostCommentService {
    private final PostCommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final ModelMapper mapper;


    public PostCommentServiceImpl(PostCommentRepository commentRepository, PostService postService, UserService userService, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public CommentCreateServiceModel createComment(CommentCreateServiceModel comment) {
        Post byId = this.postService.findByIdentificationNumber(comment.getPostID());
        User byUsername = this.userService.findByUsername(comment.getAuthor());
        PostComment comment1 = new PostComment();
        comment1.setUsername(byUsername.getUsername());
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

                    map.setAuthor(comment.getUsername());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
                    map.setDate(comment.getDate().format(formatter));
                    map.setPostID(postId);

                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String commentId, String postId) {
        PostComment postComment =
                this.commentRepository.findById(commentId).orElse(null);
        assert postComment != null;


        this.commentRepository.delete(postComment);
    }

    @Override
    public void like(String commentId) {
        PostComment comment = this.commentRepository.findById(commentId).orElse(null);
        assert comment != null;
        comment.setLikes(comment.getLikes() + 1);

        this.commentRepository.saveAndFlush(comment);
    }

    @Override
    public void dislike(String commentId) {
        PostComment comment = this.commentRepository.findById(commentId).orElse(null);
        assert comment != null;
        comment.setDislikes(comment.getDislikes() + 1);

        this.commentRepository.saveAndFlush(comment);
    }


}
