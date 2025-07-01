package mardi.erp_mini.exception;


import mardi.erp_mini.dto.response.ErrorCode;

public class InvalidApiKeyException extends BizBaseException {

    public InvalidApiKeyException() {
        super(ErrorCode.AUTH_INVALID_API_KEY);
    }
}
