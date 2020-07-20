package demoprojects.demo.service.interfaces.blog;

import demoprojects.demo.service.models.bind.CommentCreateServiceModel;
import demoprojects.demo.service.models.view.CommentViewServiceModel;

import java.util.List;

public interface PostCommentService {
    CommentCreateServiceModel createComment(CommentCreateServiceModel comment);

    List<CommentViewServiceModel> commentsOfPost(String postId);

    void delete(String commentId, String postId);

    void like(String commentId);

    void dislike(String commentId);
}
