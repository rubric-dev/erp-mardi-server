package mardi.erp_mini.support.kakao;


import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RequestMapping("/kakao/message")
@RequiredArgsConstructor
@RestController
public class KakaoTemplateTestApi {
    private final KakaoMessageSender kakaoMessageSender;

    @PostMapping
    public void sendProductDelivery() {
        kakaoMessageSender.sendProductDeliveryMessage("01092494441");
    }

    @PostMapping("/2")
    public void sendBrandInviteComplete() {
        kakaoMessageSender.sendBrandInviteCompleteMessage("01092494441");
    }

    @PostMapping("/3")
    public void sendCampaignInviteComplete() {
        kakaoMessageSender.sendCampaignInviteAcceptMessage("01092494441");
    }

    @PostMapping("/4")
    public void sendBrandInviteMessage() {
//        kakaoMessageSender.sendBrandInviteMessage("01092494441");
    }
}
