package mardi.erp_mini.exception;


import mardi.erp_mini.dto.response.ErrorCode;

public class DuplicateNameException extends BizBaseException {
    public DuplicateNameException() {
        super(ErrorCode.DUPLICATE_NAME);
    }
}
