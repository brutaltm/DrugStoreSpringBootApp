package edu.uph.ii.springbootprj.services.impl;

import edu.uph.ii.springbootprj.config.ProfileNames;
import edu.uph.ii.springbootprj.domain.Role;
import edu.uph.ii.springbootprj.domain.User;
import edu.uph.ii.springbootprj.repositories.OrderRepository;
import edu.uph.ii.springbootprj.repositories.RoleRepository;
import edu.uph.ii.springbootprj.repositories.UserRepository;
import edu.uph.ii.springbootprj.services.EmailService;
import edu.uph.ii.springbootprj.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Profile(ProfileNames.USERS_IN_DATABASE)
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private OrderRepository orderRepository;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return convertToUserDetails(user);
    }

    @PreAuthorize("#n == authentication.name")


    private UserDetails convertToUserDetails(User user) {

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getType().toString()));
        }

        //var grantedAuthorities = user.getRoles().stream().map(r->new SimpleGrantedAuthority((r.getType().toString()))).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                grantedAuthorities);
        //UWAGA: klasa ma też drugi konstruktor – więcej parametrów!!!
    }

    @Override
    public void saveUser(User user, String siteURL) throws MessagingException {
        var userRole = roleRepository.findByType(Role.Types.ROLE_USER);
        var roles = new HashSet<Role>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordConfirm(user.getPassword());
        //user.setEnabled(true);
        //var to = "bartosz.ruta8@o2.pl";
        String to = user.getEmail();
        String activationCode = UUID.randomUUID().toString();
        user.setActivationCode(activationCode);
        //emailService.sendSimpleMessage(to,"Aktywacja konta",activationCode);
        var html = createActivationCodeMessage(activationCode,siteURL);
        emailService.sendMimeMessage(to,"Aktywacja konta", html);
        userRepository.save(user);
    }

    private String createActivationCodeMessage(String code, String siteURL) {
        var thymeleafCtx = new Context();
        String verifyURL = siteURL + "/activate?code=" + code;
        thymeleafCtx.setVariable("header", "To nagłówek");
        thymeleafCtx.setVariable("title", "Aktywacja konta");
        thymeleafCtx.setVariable("description", "Kliknij w link aby aktywować swoje konto: ");
        thymeleafCtx.setVariable("link",verifyURL);

        String html = templateEngine.process("mail-messages/activationCodeMessage", thymeleafCtx);
        return html;
    }
}
