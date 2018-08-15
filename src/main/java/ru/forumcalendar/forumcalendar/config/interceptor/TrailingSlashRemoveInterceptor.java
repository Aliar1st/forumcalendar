package ru.forumcalendar.forumcalendar.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TrailingSlashRemoveInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        String requestURI = request.getRequestURI();

        if (requestURI.endsWith("/") && !requestURI.equals("/")) {
            String queryString = request.getQueryString();
            queryString = (queryString != null ? "?" + queryString : "");

            requestURI = requestURI.substring(0, requestURI.length() - 1);
            response.sendRedirect(requestURI + queryString);
            return false;
        }

        return true;
    }
}
