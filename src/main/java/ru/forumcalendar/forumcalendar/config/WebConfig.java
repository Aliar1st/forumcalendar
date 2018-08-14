package ru.forumcalendar.forumcalendar.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.forumcalendar.forumcalendar.converter.*;
import ru.forumcalendar.forumcalendar.repository.UserRepository;
import ru.forumcalendar.forumcalendar.service.UserService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/public/**")
                .addResourceLocations("/uploads/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ActivityModelConverter());
        registry.addConverter(new ShiftModelConverter());
        registry.addConverter(teamModelConverter());
        registry.addConverter(new SpeakerModelConverter());
        registry.addConverter(new EventModelConverter());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TrailingSlashRemoveInterceptor());
        registry.addInterceptor(new RedirectToEntranceWithoutChoosingTeamInterceptor());
    }

    @Bean
    public TeamModelConverter teamModelConverter() {
        return new TeamModelConverter();
    }

    @Bean
    public PrincipalExtractor principalExtractor(
            UserRepository userRepository,
            UserService userService
    ) {
        return map -> userRepository.save(
                userRepository.findById(map.get("sub").toString())
                        .orElseGet(() -> userService.signUp(map))
        );
    }
}
