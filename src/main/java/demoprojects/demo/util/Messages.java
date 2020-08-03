package demoprojects.demo.util;


public class Messages {
    public static final StringBuilder SUCCESSFUL_REG = new StringBuilder();
    public static final String CONTACT_THROUGH_US_MESSAGE = "We received a request for one of your products.\n Please contact the client." ;

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

    public static String interestInProductCC(String seller, String productTitle) {
        StringBuilder productReq = new StringBuilder();
        productReq.append(String.format("%s , someone has an interest in one of your products: %s",seller, productTitle));

        return productReq.toString();
    }

    public static String interestInProduct(String from, String productTitle, String text) {
        StringBuilder productReq = new StringBuilder();
        productReq.append(String.format("The request for %s was sent from user with email: %s", productTitle, from));
        productReq.append(System.lineSeparator());
        productReq.append("This is his/her message:");
        productReq.append(System.lineSeparator());
        productReq.append(System.lineSeparator());
        productReq.append(text);
        productReq.append(System.lineSeparator());
        productReq.append(System.lineSeparator());
        productReq.append("The MVN team™© All rights reserved.");

        return productReq.toString();
    }
    public static String interestInProductThroughUs(String from, String productTitle, String text) {
        StringBuilder productReq = new StringBuilder();
        productReq.append("We received a request! :)");
        productReq.append(System.lineSeparator());
        productReq.append(String.format("The request for %s was sent from user with email: %s", productTitle, from));
        productReq.append(System.lineSeparator());
        productReq.append(text);
        productReq.append(System.lineSeparator());
        productReq.append("The MVN team™© All rights reserved.");

        return productReq.toString();
    }
}
