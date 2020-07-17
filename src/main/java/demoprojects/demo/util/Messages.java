package demoprojects.demo.util;


public class Messages {
    public static final StringBuilder SUCCESSFUL_REG = new StringBuilder();

    public Messages() {
    }

    public static String getSuccessfulReg() {
        SUCCESSFUL_REG.append("Welcome, %s!\n\nYou have registered successfully to the MVN society!\n\nFollow this link to complete your registration: localhost:8080/mvn/users/registration/%d/%s");
        SUCCESSFUL_REG.append(System.lineSeparator());
        SUCCESSFUL_REG.append(System.lineSeparator());
        SUCCESSFUL_REG.append("The MVN team™© All rights reserved.");
        return SUCCESSFUL_REG.toString();
    }

    public static String resendVerification() {
        StringBuilder resend = new StringBuilder();
        resend.append("Your new verification URL is: localhost:8080/mvn/users/registration/%d");
        resend.append(System.lineSeparator());
        resend.append(System.lineSeparator());
        resend.append("The MVN team™© All rights reserved.");

        return resend.toString();
    }
}
