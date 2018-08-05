package ru.forumcalendar.forumcalendar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.forumcalendar.forumcalendar.domain.User;
import ru.forumcalendar.forumcalendar.repository.UserRepository;
import ru.forumcalendar.forumcalendar.service.UserService;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDateTime;

@EnableWebSecurity
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    @Qualifier("baseUserService")
//    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                    .mvcMatchers("/").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutSuccessUrl("/login");
//        http
//                .authorizeRequests()
//                .antMatchers("/**")
//                .permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .defaultSuccessUrl("/")
//                .failureUrl("/login?error=true")
//                .and()
//                .logout()
//                .permitAll()
//                .logoutSuccessUrl("/login");
    }

    @Bean
    public PrincipalExtractor principalExtractor(
            UserRepository userRepository,
            UserService userService
    ) {
        return map -> {
            User user = userRepository.findById(map.get("sub").toString()).orElseGet(() -> userService.signUp(map));
            user.setLastVisit(LocalDateTime.now());
            return userRepository.save(user);
        };
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(bCryptPasswordEncoder());
//        auth
//                .inMemoryAuthentication()
//                .withUser("user")
//                .password("111111")
//                .roles("USER")
//                .and()
//                .withUser("user1")
//                .password(bCryptPasswordEncoder().encode("111111"))
//                .roles("USER");

//    }

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
