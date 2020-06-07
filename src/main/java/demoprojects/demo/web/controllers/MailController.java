package demoprojects.demo.web.controllers;

import demoprojects.demo.service.EmailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class MailController {
    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;
    }
    @RequestMapping("/send/mail")
    public void sendMail() {
        emailService.sendEmail("mm.ivanovvv@gmail.com", "test mail", "This is a drill ;)");

    }
}
