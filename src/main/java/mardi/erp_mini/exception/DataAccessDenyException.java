package mardi.erp_mini.exception;


import mardi.erp_mini.common.dto.response.ErrorCode;

public class DataAccessDenyException extends BizBaseException {
    public DataAccessDenyException() {
        super(ErrorCode.DATA_ACCESS_DENY);
    }
}
