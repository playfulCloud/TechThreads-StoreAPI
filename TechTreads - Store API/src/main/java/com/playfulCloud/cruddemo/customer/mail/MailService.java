package com.playfulCloud.cruddemo.customer.mail;

import com.playfulCloud.cruddemo.customer.authenticate.RegisterRequest;
import com.playfulCloud.cruddemo.order.Order;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.Data;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Data
@Service
public class MailService implements JavaMailService{

    private final JavaMailSender javaMailSender;

    @Override
    public void registerMail(final RegisterRequest request) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(request.getEmail()));
            mimeMessage.setSubject("TechThreads - Registration Completed!");
            mimeMessage.setText("Dear  " + request.getFirstName() + " " +
                        request.getLastName() + ", thank you for registration.");
        };

        try {
            this.javaMailSender.send(preparator);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }


//    public void purchaseMail(Order order){
//
//    }

}
