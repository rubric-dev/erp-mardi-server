package mardi.erp_mini.common.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    AUTH_INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "0003", "Invalid access token"),
    DATA_ACCESS_DENY(HttpStatus.FORBIDDEN, "0004", "접근이 불가능한 데이터 입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "0005", "이미 만료된 토큰입니다."),
    AUTH_INVALID_API_KEY(HttpStatus.UNAUTHORIZED, "0006", "잘못된 api key입니다"),
    MANAGER_ROLE_REQUIRED(HttpStatus.FORBIDDEN, "0007", "매니저 권한이 필요합니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0000", "internal server error"),

    CONFLICT_EXIST_EMAIL(HttpStatus.CONFLICT, "2001", "이미 사용중인 이메일입니다"),
    ALREADY_ACCEPTED_CAMPAIGN_INVITATION(HttpStatus.CONFLICT, "2002", "이미 캠페인에 참여한 크리에이터입니다"),

    NOT_FOUND_USER_AUTH(HttpStatus.NOT_FOUND, "3001", "사용자 권한을 찾을 수 없습니다"),
    NOT_FOUND_BRAND_USER(HttpStatus.NOT_FOUND, "3002", "사용자가 소속된 브랜드를 찾을 수 없습니다"),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "3003", "상품 정보를 찾을 수 없습니다"),

    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "3004", "제품 정보를 찾을 수 없습니다"),
    NOT_FOUND_OPTION(HttpStatus.NOT_FOUND, "3005", "제품의 옵션 정보를 찾을 수 없습니다"),
    NOT_FOUND_DEPLETION_LEVEL(HttpStatus.NOT_FOUND, "3006", "활성화된 시나리오에서 해당 되는 소진율 단계를 찾을 수 없습니다"),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "3007", "파일을 찾을 수 없습니다"),

    INVALID_VALUE(HttpStatus.BAD_REQUEST, "4001", "invalid value"),
    ALREADY_FINISHED_CAMPAIGN(HttpStatus.BAD_REQUEST, "4007", "이미 종료된 캠페인입니다"),
    OWNER_DELETE_FAILED(HttpStatus.BAD_REQUEST, "4008", "소유한 브랜드 존재 시 탈퇴가 불가능합니다."),
	EXISTS_IN_PROGRESS_CAMPAIGN(HttpStatus.BAD_REQUEST, "4009", "진행중인 캠페인이 존재합니다."),
    REQUIRED_OWNER_ROLE(HttpStatus.BAD_REQUEST, "4010", "브랜드 소유자만 접근 가능합니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5001", "파일 업로드 중 오류가 발생했습니다"),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5002", "파일 삭제 중 오류가 발생했습니다"),
    EXCEL_FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5003", "엑셀 파일 다운로드 중 오류가 발생했습니다"),
    EXCEL_FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5004", "엑셀 파일 업로드 중 오류가 발생했습니다"),
    SCRAPING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5005", "정보 수집(스크래핑)에 실패했습니다"),
    APIFY_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5006", "Apify 요청에 실패했습니다"),
    SUBSCRIPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "5007", "결제 요청에 실패했습니다"),

    DUPLICATE_COMPANY_USER(HttpStatus.BAD_REQUEST, "6001", "이미 등록된 유저 계정입니다"),
    DUPLICATE_CODE(HttpStatus.CONFLICT, "6002", "이미 사용중인 코드입니다"),
    DUPLICATE_BRAND_HASHTAG(HttpStatus.BAD_REQUEST, "6003", "이미 존재하는 해시태그입니다"),
    DUPLICATE_CREATOR(HttpStatus.CONFLICT, "6004", "이미 초대된 크리에이터입니다."),
    DUPLICATE_CAMPAIGN_CREATOR(HttpStatus.BAD_REQUEST, "6005", "이미 존재하는 크리에이터입니다"),
    DUPLICATE_NAME(HttpStatus.CONFLICT, "6006", "이미 사용중인 이름입니다"),
    DUPLICATE_CARD(HttpStatus.CONFLICT, "6008", "이미 카드 정보를 등록하였습니다."),
    DUPLICATE_SUBSCRIPTION(HttpStatus.CONFLICT, "6009", "이미 구독 정보가 존재합니다."),
    REQUIRED_SUBSCRIPTION(HttpStatus.CONFLICT, "6010", "구독이 필요한 기능입니다."),

    INVALID_EXCEL_VALUE(HttpStatus.BAD_REQUEST, "7001", "잘못된 엑셀 값입니다. 확인 후 다시 요청해주세요");

    private final HttpStatus status;
    private final String code;
    private final String msg;
}
