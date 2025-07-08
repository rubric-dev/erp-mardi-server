package mardi.erp_mini.exception;


import mardi.erp_mini.common.dto.response.ErrorCode;

public class ExpiredTokenException extends BizBaseException {

    public ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }
}
