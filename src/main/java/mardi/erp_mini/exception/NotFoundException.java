package mardi.erp_mini.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BizBaseException {

    public NotFoundException(String msg) {
        super(msg, HttpStatus.NOT_FOUND);
    }

    public NotFoundException() {
        super("resource not found", HttpStatus.NOT_FOUND);
    }
}
