package edu.uph.ii.springbootprj.services.impl;

import edu.uph.ii.springbootprj.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testjd@o2.pl");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendMimeMessage(String to, String subject, String text) throws MessagingException {
        var mimeMessage = emailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); //czy HTML
        emailSender.send(mimeMessage);

    }

    public void sendMimeMessage(String to, String subject, String text, File file) throws MessagingException {
        var mimeMessage = emailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); //czy HTML
        helper.addAttachment(file.getName(), file);
        emailSender.send(mimeMessage);

    }

}
