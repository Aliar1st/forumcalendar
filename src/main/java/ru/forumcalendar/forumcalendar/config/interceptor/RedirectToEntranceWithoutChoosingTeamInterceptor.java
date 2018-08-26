package ru.forumcalendar.forumcalendar.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import ru.forumcalendar.forumcalendar.config.constt.SessionAttributeName;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

//        Pattern p = Pattern.compile("((/editor/)|(/entrance/).+)|(/)");
        Pattern p = Pattern.compile("(((/editor/)|(/entrance/)|(/team/invite/.+)|(/static/)|(/public/)|(/webjars/)).+)|((/)|(/error))");

        boolean f =p.matcher(requestURI).matches();

        if (team == null && !p.matcher(requestURI).matches()) {
            response.sendRedirect("/entrance/1");
            return false;
        }

        return true;
    }
}
