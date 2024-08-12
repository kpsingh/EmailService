package com.lld4.emailservice.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lld4.emailservice.dtos.SendEmailDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SendEmailConsumer {

    private ObjectMapper objectMapper;
    private EmailUtil emailUtil;

    public SendEmailConsumer(ObjectMapper objectMapper, EmailUtil emailUtil) {
        this.objectMapper = objectMapper;
        this.emailUtil = emailUtil;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleSendEmailMessage(String message ){
        System.out.println("Kafka has triggered the event to Consume");
        SendEmailDto sendEmailDto = null;
        try {
            sendEmailDto = objectMapper.readValue(message, SendEmailDto.class);
            // We can have the logic to send the email
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(sendEmailDto);

        //Send an Email.
        //SMTP -> Simple Mail Transfer Protocol.
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("temp39766@gmail.com", "wddohrzomjejssjd");
            }
        };
        Session session = Session.getInstance(props, auth);

        emailUtil.sendEmail(
                session,
                sendEmailDto.getTo(),
                sendEmailDto.getSubject(),
                sendEmailDto.getBody()
        );

    }
}
