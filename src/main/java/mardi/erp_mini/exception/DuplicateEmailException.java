package mardi.erp_mini.exception;


import mardi.erp_mini.dto.response.ErrorCode;

public class DuplicateEmailException extends BizBaseException {
    public DuplicateEmailException() {
        super(ErrorCode.CONFLICT_EXIST_EMAIL);
    }
}
