package mardi.erp_mini.exception;


import mardi.erp_mini.common.dto.response.ErrorCode;

public class FileNotFoundException extends BizBaseException {

    public FileNotFoundException() {
        super(ErrorCode.NOT_FOUND_FILE);
    }
}
