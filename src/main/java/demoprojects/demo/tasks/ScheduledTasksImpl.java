package demoprojects.demo.tasks;

import demoprojects.demo.service.interfaces.blog.PostService;
import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.interfaces.user.EmailService;
import demoprojects.demo.service.interfaces.user.UserService;
import demoprojects.demo.service.models.view.PostViewServiceModel;
import demoprojects.demo.service.models.view.ProductViewServiceModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class ScheduledTasksImpl implements ScheduledTasks {
    private final UserService userService;
    private final ProductService productService;
    private final PostService postService;
    private final EmailService emailService;


    public ScheduledTasksImpl(UserService userService, ProductService productService, PostService postService, EmailService emailService) {
        this.userService = userService;
        this.productService = productService;
        this.postService = postService;
        this.emailService = emailService;
    }

    @Override
    @Scheduled(cron = "0 0 12 * * SUN")
    public void notifyUsersForNewProducts() {
        List<String> allUserEmails = this.userService.listAllUserEmails();
        Integer newProductsCount = this.productService.getNewProductsCount();

        allUserEmails.forEach(email -> this.emailService.sendEmail(email, "New products in the past week", String.format("There are %d new products in the last week!\n\n Check them out here: http://localhost:8080/mvn/shop/products", newProductsCount)));
        System.out.println("Sent new products notifications!");
    }

    @Override
    @Scheduled(cron = "0 0 12 * * *")
    public void blogSubscription() {
        List<String> userEmails = this.userService.findUsersSubscribedToBlog();
        List<PostViewServiceModel> postViewServiceModels = this.postService.listOneDayOldPosts();
        StringBuffer buffer = new StringBuffer();

        buffer.append("New Posts from the last 24h: ");
        buffer.append(System.lineSeparator());
        buffer.append(System.lineSeparator());
        postViewServiceModels.forEach(postViewServiceModel -> {

            buffer.append(String.format("%s, written by %s%nCheck it out here: http://localhost:8080/posts/article?id=%s",
                    postViewServiceModel.getTitle(),
                    postViewServiceModel.getAuthor(),
                    postViewServiceModel.getId()));

            buffer.append(System.lineSeparator());
            buffer.append(System.lineSeparator());
        });

        userEmails.forEach(email -> {
            this.emailService.sendEmail(email, "Posts in the last 24h :)", buffer.toString());
        });
    }

    @Override
    @Scheduled(cron = "0 5 12 * * *")
    public void shopSubscription() {
        List<String> userEmails = this.userService.findUsersSubscribedToShop();
        List<ProductViewServiceModel> productViewServiceModels =
                this.productService.listOneDayOldProducts();
        StringBuffer buffer = new StringBuffer();

        buffer.append("New Products from the last 24h: ");
        buffer.append(System.lineSeparator());
        buffer.append(System.lineSeparator());
        productViewServiceModels.forEach(productViewServiceModel -> {
            buffer.append(String.format("%s, sold by %s%nCheck it out here: http://localhost:8080/mvn/shop/product?id=%s",
                    productViewServiceModel.getTitle(),
                    productViewServiceModel.getUsername(),
                    productViewServiceModel.getId()));

            buffer.append(System.lineSeparator());
            buffer.append(System.lineSeparator());
        });

        userEmails.forEach(email -> {
            this.emailService.sendEmail(email, "Post in the last 24h :)", buffer.toString());
        });
    }
}
