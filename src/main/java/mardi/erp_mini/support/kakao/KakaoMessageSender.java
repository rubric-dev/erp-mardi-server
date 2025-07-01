package mardi.erp_mini.support.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class KakaoMessageSender {

    private final RestTemplate restTemplate;

    @Value("${nhn_cloud.sender_key}")
    private String senderKey;

    @Value("${nhn_cloud.access_key}")
    private String accessKey;

    @Value("${nhn_cloud.secret_key}")
    private String secretKey;

    @Value("${client-url}")
    private String CLIENT_URL;


    //브랜드에 크리에이터 초대
    public void sendBrandInviteMessage(String phoneNumber, String creatorName, String brandName, String value) {
        Map<String, String> templateParameters = new HashMap<>();

        templateParameters.put("creator", creatorName);
        templateParameters.put("brand", brandName);
        templateParameters.put("activate_url", CLIENT_URL + "/creator/invite?token=" + value);

        HttpHeaders headers = createHeader();
        Request request = new Request(
                senderKey,
                "BRAND_INVI",
                Collections.singletonList(new Recipient(
                        phoneNumber,
                        templateParameters
                ))
        );

        HttpEntity<Request> httpRequest = new HttpEntity<>(request, headers);
        sendKakaoMessage(httpRequest);
    }

    //크리에이터 브랜드 초대 수락 이후
    public void sendBrandInviteCompleteMessage(String phoneNumber) {
        Map<String, String> templateParameters = new HashMap<>();

        templateParameters.put("creator", "크리에이터");
        templateParameters.put("brand", "mlb");
        templateParameters.put("activate_url", CLIENT_URL + "/partner/signin");

        HttpHeaders headers = createHeader();
        Request request = new Request(
                senderKey,
                "BRAND_INVI_COMPLETE",
                Collections.singletonList(new Recipient(
                        phoneNumber,
                        templateParameters
                ))
        );

        HttpEntity<Request> httpRequest = new HttpEntity<>(request, headers);
        sendKakaoMessage(httpRequest);
    }

    //크리에이터가 캠페인 초대 수락 이후
    public void sendCampaignInviteAcceptMessage(String phoneNumber) {
        Map<String, String> templateParameters = new HashMap<>();

        templateParameters.put("creator", "크리에이터");
        templateParameters.put("캠페인 명", "캠페인 명");
        templateParameters.put("activate_url", CLIENT_URL + "/partner/signin");

        HttpHeaders headers = createHeader();
        Request request = new Request(
                senderKey,
                "CAST_ACCEPT",
                Collections.singletonList(new Recipient(
                        phoneNumber,
                        templateParameters
                ))
        );

        HttpEntity<Request> httpRequest = new HttpEntity<>(request, headers);
        sendKakaoMessage(httpRequest);
    }


    //크리에이터한테 제품 발송 되었을 때
    public void sendProductDeliveryMessage(String phoneNumber) {
        Map<String, String> templateParameters = new HashMap<>();

        templateParameters.put("creator", "크리에이터");
        templateParameters.put("campaign", "캠페인");
        templateParameters.put("name", "택배사");
        templateParameters.put("delevery_num", "송장번호");
        templateParameters.put("brand", "mlb");
        templateParameters.put("activate_url", CLIENT_URL + "/partner/signin");

        HttpHeaders headers = createHeader();
        Request request = new Request(
                senderKey,
                "PRODUCT_DELIVERY",
                Collections.singletonList(new Recipient(
                        phoneNumber,
                        templateParameters
                ))
        );

        HttpEntity<Request> httpRequest = new HttpEntity<>(request, headers);
        sendKakaoMessage(httpRequest);
    }


    private void sendKakaoMessage(HttpEntity<Request> httpRequest) {
        String alimTalkSendRequestUrl = "https://api-alimtalk.cloud.toast.com/alimtalk/v2.3/appkeys/" + accessKey + "/messages";

        ResponseEntity<String> response = restTemplate.exchange(
                alimTalkSendRequestUrl,
                HttpMethod.POST,
                httpRequest,
                String.class
        );

        log.info("[카카오알림톡발송] response: {}", response.getBody());
    }


    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Secret-Key", secretKey);
        return headers;
    }

    @Data
    @AllArgsConstructor
    public static class Request {

        private String senderKey;
        private String templateCode;
        private List<Recipient> recipientList;
    }

    @Data
    @AllArgsConstructor
    public static class Recipient {

        private String recipientNo;
        private Map<String, String> templateParameter;
    }
}
