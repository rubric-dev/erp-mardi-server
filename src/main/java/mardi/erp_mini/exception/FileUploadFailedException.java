package mardi.erp_mini.exception;


import mardi.erp_mini.common.dto.response.ErrorCode;

public class FileUploadFailedException extends BizBaseException {

    public FileUploadFailedException() {
        super(ErrorCode.FILE_UPLOAD_FAILED);
    }
}
