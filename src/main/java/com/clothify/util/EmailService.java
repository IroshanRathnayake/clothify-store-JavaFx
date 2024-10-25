package com.clothify.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailService {
    private static final String EMAIL = "work.iroshan@gmail.com";
    private static final String PASSWORD = "pryw ogjc ifce vujd";

    public String sendOTP(String recipientEmail) {
        String otp = generateOTP();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP for Password Reset");

            message.setText("Your OTP to reset the password is: " + otp);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }

        return otp;
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 10000 + random.nextInt(90000);
        return String.valueOf(otp);
    }
}
