package edu.uph.ii.springbootprj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile(ProfileNames.USERS_IN_MEMORY)
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        User.UserBuilder userBuilder = User.builder();

        UserDetails admin = userBuilder
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        UserDetails user = userBuilder
                .username("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .build();

        UserDetails superUser = userBuilder
                .username("superuser")
                .password(passwordEncoder.encode("super"))
                .roles("USER","ADMIN")
                .build();

        manager.createUser(admin);
        manager.createUser(user);
        manager.createUser(superUser);

        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/webjars/**","/css/**","/public**","/**","/drugs/list**","/register","saveUser","/zdjecia/**","/asd/**", "/activate", "/drugs/details**").permitAll()
                .mvcMatchers("/drugs/details**", "/account/**").hasRole("USER")
                .mvcMatchers("/drugs/details**","/drugs/edit**","/drugs/add**","/drugs/delete**","/h2-console/**")
                .access("hasRole('ADMIN')")
                .anyRequest()
                .authenticated()//każde żądanie ma być uwierzytelnione
                .and().formLogin()//enable form login instead of basic login
                .and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
                .and().headers().frameOptions().sameOrigin();//allow use of frame to same origin urls

        http
                .formLogin()//uwierzytelnienie poprzez formularz logowania
                .loginPage("/login")//formularz dostępny jest pod URL
                .defaultSuccessUrl("/")
                .permitAll();//przyznaj dostęp wszystkim użytkownikom

        http.exceptionHandling().accessDeniedPage("/forbidden");
    }

}
