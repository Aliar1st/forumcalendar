package ru.forumcalendar.forumcalendar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ru.forumcalendar.forumcalendar.converter.*;
import ru.forumcalendar.forumcalendar.repository.UserTeamRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        registry.addConverter(shiftModelConverter());
        registry.addConverter(teamModelConverter());
        registry.addConverter(new SpeakerModelConverter());
        registry.addConverter(new EventModelConverter());
    }

    @Bean
    public ShiftModelConverter shiftModelConverter() {
        return new ShiftModelConverter();
    }

    @Bean
    public TeamModelConverter teamModelConverter() {
        return new TeamModelConverter();
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TrailingSlashRemoveInterceptor());
    }
}
