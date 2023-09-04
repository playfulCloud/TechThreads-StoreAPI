package com.playfulCloud.cruddemo.customer.mail;

import com.playfulCloud.cruddemo.customer.authenticate.RegisterRequest;
import com.playfulCloud.cruddemo.order.orderResponse.OrderRequest;
import com.playfulCloud.cruddemo.product.service.ProductService;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.Data;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Data
@Service
public class JavaMailServiceImpl implements JavaMailService {

    private final JavaMailSender javaMailSender;
    private final ProductService productService;

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

    @Override
    public void purchaseMail(OrderRequest request) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(request.getEmail()));
            mimeMessage.setSubject("TechThreads - Your purchase has been accepted");
            mimeMessage.setText("Dear  " + request.getEmail() + " " + ", thank you for your purchase on our site " + "\n" +
                    "Order summary: " + boughtProducts(request.getContent()) + "\n");

        };
        try {
            this.javaMailSender.send(preparator);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public String boughtProducts(String content) {
        String[] items = content.split(",");
        StringBuilder result = new StringBuilder();
        for (String item : items) {
            result.append(productService.findById(Integer.parseInt(item)).get()).append("\n");
        }
        return result.toString();
    }


}
