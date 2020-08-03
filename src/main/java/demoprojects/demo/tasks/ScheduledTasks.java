package demoprojects.demo.tasks;

import demoprojects.demo.service.interfaces.shop.ProductService;
import demoprojects.demo.service.interfaces.user.EmailService;
import demoprojects.demo.service.interfaces.user.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTasks {
    private final UserService userService;
    private final ProductService productService;
    private final EmailService emailService;
    private static final String TIME_NOW = LocalDateTime.now().toString();

    public ScheduledTasks(UserService userService, ProductService productService, EmailService emailService) {
        this.userService = userService;
        this.productService = productService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 12 * * SUN")
    public void notifyUsersForNewProducts() {
        List<String> allUserEmails = this.userService.listAllUserEmails();
        Integer newProductsCount = this.productService.getNewProductsCount();

        allUserEmails.forEach(email-> this.emailService.sendEmail(email,"New products in the past week",String.format("There are %d new products in the last week!\n\n Check them out here: http://localhost:8080/mvn/shop/products",newProductsCount)));
        System.out.println("Sent new products notifications!");
    }
}
