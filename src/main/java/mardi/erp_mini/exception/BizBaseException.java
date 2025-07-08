package mardi.erp_mini.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mardi.erp_mini.common.dto.response.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BizBaseException extends RuntimeException {

    private HttpStatus status = HttpStatus.CONFLICT;
    private String code;
    private String msg;
    private LocalDateTime timestamp = LocalDateTime.now();

    public BizBaseException(ErrorCode errorCode) {
        this(errorCode.getStatus(), errorCode.getCode(), errorCode.getMsg());
    }

    public BizBaseException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BizBaseException(String msg, HttpStatus status) {
        super(msg);
        this.msg = msg;
        this.status = status;
    }

    public BizBaseException(HttpStatus status, String code, String msg) {
        super(msg);
        this.status = status;
        this.code = code;
        this.msg = msg;
    }
}
