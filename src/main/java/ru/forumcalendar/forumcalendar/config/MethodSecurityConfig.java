package ru.forumcalendar.forumcalendar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

//    @Override
//    protected MethodSecurityExpressionHandler createExpressionHandler() {
//        DefaultMethodSecurityExpressionHandler expressionHandler =
//                new DefaultMethodSecurityExpressionHandler();
//
//        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator(activityService, shiftService, speakerService, teamService, eventService));
//
//        return expressionHandler;
//    }
}