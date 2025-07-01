package mardi.erp_mini.exception;


import mardi.erp_mini.dto.response.ErrorCode;

public class ExcelUploadFailedException extends BizBaseException {

    public ExcelUploadFailedException() {
        super(ErrorCode.EXCEL_FILE_UPLOAD_FAILED);
    }
}
