package edu.uph.ii.springbootprj.services;

import javax.mail.MessagingException;
import java.io.File;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendMimeMessage(String to, String subject, String text) throws MessagingException;
    void sendMimeMessage(String to, String subject, String text, File file) throws MessagingException;
}
