package mardi.erp_mini.exception;


import mardi.erp_mini.dto.response.ErrorCode;

public class DuplicateCodeException extends BizBaseException {
    public DuplicateCodeException() {
        super(ErrorCode.DUPLICATE_CODE);
    }
}
