package mardi.erp_mini.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String LOG_ID = "LOG_ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uuid = UUID.randomUUID().toString();

        StringBuilder sb = new StringBuilder()
                .append("[REQUEST] ")
                .append("Log ID : ")
                .append(uuid)
                .append(" || Uri : ")
                .append(request.getRequestURI())
                .append(" || Method : ")
                .append(request.getMethod());

        String queryString = request.getQueryString();
        if (queryString != null) {
            sb.append(" || [QUERY] : ").append(queryString);
        }

        log.info(sb.toString());

        request.setAttribute(LOG_ID, uuid);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Object uuid = request.getAttribute(LOG_ID);

        StringBuilder sb = new StringBuilder()
                .append("[RESPONSE] ")
                .append("Log ID : ")
                .append(uuid)
                .append(" || Uri : ")
                .append(request.getRequestURI())
                .append(" || Method : ")
                .append(request.getMethod());

        log.info(sb.toString());
    }
}
