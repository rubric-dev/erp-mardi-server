package mardi.erp_mini.common.dto.response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Getter
@ToString
public class CommonResponse<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String path = getCurrentPath();
    private final T data;

    public CommonResponse(T data) {
        this.data = data;
    }

    public CommonResponse() {
        this.data = null;
    }

    public static CommonResponse ok() {
        return new CommonResponse<>();
    }

    public static String getCurrentPath() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request.getRequestURI();
        }
        return null;
    }
}
