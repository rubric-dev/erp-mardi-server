package mardi.erp_mini.exception;


import mardi.erp_mini.common.dto.response.ErrorCode;

public class ManagerRoleRequiredException extends BizBaseException {
    public ManagerRoleRequiredException() {
        super(ErrorCode.MANAGER_ROLE_REQUIRED);
    }
}
