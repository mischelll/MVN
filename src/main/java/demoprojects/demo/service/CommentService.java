package demoprojects.demo.service;

import demoprojects.demo.service.models.bind.CommentCreateServiceModel;
import demoprojects.demo.service.models.view.CommentViewServiceModel;

import java.util.List;

public interface CommentService {
    CommentCreateServiceModel createComment(CommentCreateServiceModel comment);

    List<CommentViewServiceModel> commentsOfPost(String postId);
}
