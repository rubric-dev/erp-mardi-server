package mardi.erp_mini.exception;


import mardi.erp_mini.common.dto.response.ErrorCode;

public class FileDeleteFailedException extends BizBaseException {

    public FileDeleteFailedException() {
        super(ErrorCode.FILE_DELETE_FAILED);
    }
}
