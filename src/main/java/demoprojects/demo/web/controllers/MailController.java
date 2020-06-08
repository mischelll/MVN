package demoprojects.demo.web.controllers;

import demoprojects.demo.service.EmailService;
import demoprojects.demo.util.Messages;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {
    private final EmailService emailService;

    public MailController(EmailService emailService) {
        this.emailService = emailService;

    }
    @RequestMapping("/send/mail")
    public void sendMail() {
        emailService.sendEmail("mishel.ivanovv@gmail.com", "Successful Registration", String.format(Messages.getSuccessfulReg(),"peter",213123));

    }
}
