package mardi.erp_mini.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class SmsMessageSender {

    private final RestTemplate restTemplate;

    @Value("${nhn_cloud.sender_key}")
    private String senderKey;

    @Value("${nhn_cloud.access_key}")
    private String accessKey;

    @Value("${nhn_cloud.secret_key}")
    private String secretKey;

    @Value("${client-url}")
    private String CLIENT_URL;

    //크리에이터 브랜드 초대 수락 이후
    public void sendValidatePhoneMessage(String phoneNumber, String content) {
        String sendPhoneNumber = "01089634895";

        HttpHeaders headers = createHeader();
        Request request = new Request(
                "[인증번호:" + content + "]" + "\n" + "퍼핀 인증을 위한 인증번호입니다.",
                sendPhoneNumber,
                List.of(new Recipient(phoneNumber, "82"))
        );

        HttpEntity<Request> httpRequest = new HttpEntity<>(request, headers);

        try {
            sendMessage(httpRequest);
        } catch (Exception e) {
            log.error("[sms 전송 실패] cause: {} , message: {}", NestedExceptionUtils.getMostSpecificCause(e),
                    e.getMessage());
            throw e;
        }
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Secret-Key", secretKey);
        return headers;
    }

    public void sendMessage(HttpEntity<Request> httpRequest) {
        String alimTalkSendRequestUrl =
                "https://api-sms.cloud.toast.com/sms/v3.0/appKeys/" + accessKey + "/sender/sms";

        restTemplate.exchange(
                alimTalkSendRequestUrl,
                HttpMethod.POST,
                httpRequest,
                String.class
        );
    }

    @Data
    @AllArgsConstructor
    public static class Request {

        //내용
        private String body;
        //송신번호
        private String sendNo;
        private List<Recipient> recipientList;
    }

    @Data
    @AllArgsConstructor
    public static class Recipient {

        private String recipientNo;
        private String countryCode;
    }
}
