package edu.uph.ii.springbootprj.services;

import edu.uph.ii.springbootprj.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.mail.MessagingException;

public interface UserService extends UserDetailsService {
    void saveUser(User user, String siteURL) throws MessagingException;
}
