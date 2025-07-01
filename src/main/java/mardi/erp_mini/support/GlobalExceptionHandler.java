package mardi.erp_mini.support;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mardi.erp_mini.dto.response.ErrorCode;
import mardi.erp_mini.dto.response.ErrorResponse;
import mardi.erp_mini.exception.BizBaseException;
import mardi.erp_mini.support.slack.SlackWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final SlackWrapper slackWrapper;

    private final Environment env;

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e, HttpServletRequest request) {
        if (List.of(env.getActiveProfiles()).contains("local"))
            e.printStackTrace();

        log.error("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),
                e.getMessage());

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getMsg(),
                errorCode.getCode()
        );

        slackWrapper.warning(dump(e, request).toString());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler({
            HttpMediaTypeNotSupportedException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestPartException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class,
            TypeMismatchException.class,
            MethodArgumentTypeMismatchException.class,
            MultipartException.class,
            IllegalArgumentException.class,
            SQLIntegrityConstraintViolationException.class,
            UnsatisfiedServletRequestParameterException.class,
            HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity handleClientException(Exception e) {
        log.error("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),
                e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                "x400"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity handleUnAuthorizedException(Exception e) {
        log.warn("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),
                e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                "x401"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),
                e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                "x400"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BizBaseException.class)
    public ResponseEntity handleBizException(BizBaseException e) {
        log.error("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),
                e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                e.getMsg(),
                e.getCode()
        );
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity accessDeniedException(AccessDeniedException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                ErrorCode.AUTH_INVALID_ACCESS_TOKEN.getCode()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity stackTrace(Exception e) {
        if (List.of(env.getActiveProfiles()).contains("local"))
            e.printStackTrace();

        log.error("Stack trace: ", e);

        ErrorResponse errorResponse = new ErrorResponse(
                NestedExceptionUtils.getMostSpecificCause(e).toString(),
                "0000"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[Exception] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),
                e.getMessage());

        Map<String, String> fieldErrorMap = e.getBindingResult().getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                fe -> fe.getDefaultMessage() == null ? "" : fe.getDefaultMessage(),
                                (o, n) -> n
                        )
                );

        String representativeMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(ErrorCode.INVALID_VALUE.getMsg());

        ErrorResponse errorResponse = new ErrorResponse(
                representativeMessage,
                ErrorCode.INVALID_VALUE.getCode(),
                fieldErrorMap
        );

        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }

    private StringBuilder dump(Exception e, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder("HttpRequest Dump:")
                .append("\n  Request Method: ").append(request.getMethod())
                .append("\n  Request URI   : ").append(request.getRequestURI())
                .append("\n  [Exception Cause]: ").append(NestedExceptionUtils.getMostSpecificCause(e))
                .append("\n  [Message]: ").append(e.getMessage())
                .append("\n  Parameters    : ");

        for (Enumeration<String> name = request.getParameterNames(); name.hasMoreElements(); ) {
            String key = name.nextElement();
            String[] values = request.getParameterValues(key);
            for (String value : values) {
                builder.append("\n    ").append(key).append('=').append(value);
            }
        }

        builder.append("\n  Headers       : ");
        for (Enumeration<String> header = request.getHeaderNames(); header.hasMoreElements(); ) {
            String key = header.nextElement();
            Enumeration<String> values = request.getHeaders(key);
            while (values.hasMoreElements()) {
                builder.append("\n    ").append(key).append(": ").append(values.nextElement());
            }
        }

        builder.append("\n  Body          : ");
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            try {
                String body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
                if (StringUtils.hasLength(body)) {
                    builder.append("\n");
                    builder.append(body);
                }
            } catch (IOException ignore) {
            }
        }

        return builder;
    }
}
