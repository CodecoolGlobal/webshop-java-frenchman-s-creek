package com.codecool.shop.email;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Mailer extends Thread {
    private final String from;
    private final String password;
    private final String to;
    private final String sub;
    private final String msg;

    public Mailer(String from, String password, String to, String sub, String msg) {
        this.from = from;
        this.password = password;
        this.to = to;
        this.sub = sub;
        this.msg = msg;
    }
    
    public void run() {
        send();
    }

    private void send() {
        // Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Get Session
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        // Compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(sub);
            message.setText(msg);
            // Send message
            Transport.send(message);
            System.out.println("Message sent successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}