package demoprojects.demo.util.messaging;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;


import com.twilio.type.PhoneNumber;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
public class SmsSenderAndReceiver  {
    private  final String phoneNumber = "+16698001550";
    private static final String ACCOUNT_SID = "ACbb03959307b656aeb5de6f16f3923ce3";
    private static final String ACCOUNT_TOKEN = "5e5ea8bc2969c9f8d415f952a32c98b2";


    public void sendMessage(){
        Twilio.init(ACCOUNT_SID,ACCOUNT_TOKEN);

        Message.creator(new PhoneNumber("+359889116729"),new PhoneNumber(phoneNumber),"Good night from the MVN | Team.").create();
    }

}
