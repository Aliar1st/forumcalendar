package ru.forumcalendar.forumcalendar.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectToEntranceWithoutChoosingTeamInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        queryString = (queryString != null ? "?" + queryString : "");

        Object team = request.getSession().getAttribute(SessionAttributeName.CURRENT_TEAM_ATTRIBUTE);

        if (team == null && !requestURI.contains("/entrance/")) {
            response.sendRedirect("/entrance/1");
            return false;
        }

        return true;
    }
}
