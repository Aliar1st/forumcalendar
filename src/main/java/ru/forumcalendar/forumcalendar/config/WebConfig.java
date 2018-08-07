package ru.forumcalendar.forumcalendar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.forumcalendar.forumcalendar.converter.ActivityModelConverter;
import ru.forumcalendar.forumcalendar.converter.ShiftModelConverter;

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
    }

    @Bean
    public ShiftModelConverter shiftModelConverter() {
        return new ShiftModelConverter();
    }
}
