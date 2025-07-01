package mardi.erp_mini.support.slack;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Profile("prod")
@RequiredArgsConstructor
@Service
public class ProdSlackWrapper implements SlackWrapper {
    private final RestTemplate restTemplate;
    private static final String WEBHOOK_URL = "https://hooks.slack.com/services/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";
    private static final String APP_NAME = "퍼핀 API 서버";

    private static final String ICON_INFO = ":information_source:";

    /**
     * 정보 메시지 발송
     */
    public void info(String message, String webhookUrl) {
        send(message, ICON_INFO, webhookUrl);
    }

    private static final String ICON_SUCCESS = ":large_green_circle:";

    /**
     * 성공 메시지 발송
     */
    @Override
    public void success(String message) {
        send(message, ICON_SUCCESS, WEBHOOK_URL);
    }

    public void success(String message, String webhookUrl) {
        send(message, ICON_SUCCESS, webhookUrl);
    }

    private static final String ICON_WARNING = ":warning:";

    /**
     * 경고 메시지 발송
     */
    @Override
    public void warning(String message) {
        send(message, ICON_WARNING, WEBHOOK_URL);
    }

    private static final String ICON_DANGER = ":rotating_light:";

    /**
     * 오류 메시지 발송
     */
    public void danger(String message, String webhookUrl) {
        send(message, ICON_DANGER, webhookUrl);
    }

    /**
     * 일반 메시지 발송
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void send(String message, String icon, String webhookUrl) {
        send(message, icon, null, webhookUrl);
    }

    /**
     * 일반 메시지 발송
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void send(String message, String icon, String channel, String webhookUrl) {
        sendMessage(message, icon, channel, webhookUrl);
    }

    /**
     * 내부 처리
     */
    private void sendMessage(String message, String icon, String channel, String webhookUrl) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("username", "PROD");
            parameters.put("text", message);
            if (!ObjectUtils.isEmpty(icon)) parameters.put("icon_emoji", icon);
            if (!ObjectUtils.isEmpty(channel)) parameters.put("channel", "#" + channel);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(parameters);
            restTemplate.exchange(webhookUrl, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
