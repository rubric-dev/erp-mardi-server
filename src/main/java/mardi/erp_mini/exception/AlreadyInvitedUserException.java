package mardi.erp_mini.exception;


import mardi.erp_mini.dto.response.ErrorCode;

public class AlreadyInvitedUserException extends BizBaseException {

    public AlreadyInvitedUserException() {
        super(ErrorCode.DUPLICATE_COMPANY_USER);
    }
}
