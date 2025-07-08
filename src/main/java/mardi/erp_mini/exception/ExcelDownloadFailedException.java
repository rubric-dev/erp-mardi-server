package mardi.erp_mini.exception;


import mardi.erp_mini.common.dto.response.ErrorCode;

public class ExcelDownloadFailedException extends BizBaseException {

    public ExcelDownloadFailedException() {
        super(ErrorCode.EXCEL_FILE_DOWNLOAD_FAILED);
    }
}
