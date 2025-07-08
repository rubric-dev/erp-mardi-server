package mardi.erp_mini.exception;


import mardi.erp_mini.common.dto.response.ErrorCode;

public class InvalidValueException extends BizBaseException {

    public InvalidValueException() {
        super(ErrorCode.INVALID_VALUE);
    }

    public InvalidValueException(String msg) {
        super(
                ErrorCode.INVALID_VALUE.getStatus(),
                ErrorCode.INVALID_VALUE.getCode(),
                msg
        );
    }
}
