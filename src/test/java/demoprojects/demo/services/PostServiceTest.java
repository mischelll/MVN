package demoprojects.demo.services;

import demoprojects.demo.BaseTest;
import demoprojects.demo.dao.models.entities.Post;
import demoprojects.demo.dao.repositories.blog.PostRepository;
import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.models.view.PostViewServiceModel;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.UUID;

public class PostServiceTest extends BaseTest {
    @MockBean
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    private Post post;
    private PostViewServiceModel postViewServiceModel;

    private static String DEFAULT_ID = UUID.randomUUID().toString();
    private static String DEFAULT_TITLE = "Post Title";
    private static String DEFAULT_PREVIEW = "Post Preview";
    private static String DEFAULT_CONTENT = "Post Content";
    private static LocalDateTime DEFAULT_POSTED_ON = LocalDateTime.now();
    private static String DEFAULT_IMG_URL = "https://cdn.pixabay.com/photo/2015/02/24/15/41/dog-647528__340.jpg";

    public static Post initializeEntity() {
        Post post = new Post();
        post.setId(DEFAULT_ID);
        post.setTitle(DEFAULT_TITLE);
        post.setPreview(DEFAULT_PREVIEW);
        post.setContent(DEFAULT_CONTENT);
        post.setPostedOn(DEFAULT_POSTED_ON);
        post.setImgUrl(DEFAULT_IMG_URL);

        return post;
    }

    public static PostViewServiceModel initializeServiceModel() {
        PostViewServiceModel postViewServiceModel = new PostViewServiceModel();
        postViewServiceModel.setId(DEFAULT_ID);
        postViewServiceModel.setTitle(DEFAULT_TITLE);
        postViewServiceModel.setPreview(DEFAULT_PREVIEW);
        postViewServiceModel.setContent(DEFAULT_CONTENT);
        postViewServiceModel.setPostedOn(DEFAULT_POSTED_ON.toString());
        postViewServiceModel.setImgUrl(DEFAULT_IMG_URL);

        return postViewServiceModel;
    }



}
