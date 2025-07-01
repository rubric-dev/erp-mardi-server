package mardi.erp_mini.support.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsSesClient {
    private final SesV2Client sesClient;
    private final ObjectMapper objectMapper;

    @Value("${client-url}")
    private String CLIENT_URL;

    private static final String NO_REPLY = "Yedam <no-reply@hryedam.co.kr>";

    private void send(EmailContent content, String targetEmail) {
        sesClient.sendEmail(
                SendEmailRequest.builder()
                        .fromEmailAddress(NO_REPLY)
                        .content(content)
                        .destination(Destination.builder()
                                .toAddresses(targetEmail).build())
                        .build()
        );
    }

    private void send(Optional<EmailContent> content, String targetEmail) {
        content.ifPresent(emailContent -> sesClient.sendEmail(
                SendEmailRequest.builder()
                        .fromEmailAddress(NO_REPLY)
                        .content(emailContent)
                        .destination(Destination.builder()
                                .toAddresses(targetEmail).build())
                        .build()
        ));
    }


    private Optional<EmailContent> ofEmailContent(EmailTemplate template, Map<String, String> contentMap) {
        try {
            Template target = Template.builder()
                    .templateName(template.name())
                    .templateData(objectMapper.writeValueAsString(contentMap))
                    .build();

            return Optional.of(
                    EmailContent.builder()
                            .template(target)
                            .build()
            );
        } catch (JsonProcessingException e) {
            log.error("Failed to convert contentMap to JSON: {}", contentMap, e);
        } catch (Exception e) {
            log.error("Unexpected error while creating EmailContent", e);
        }
        return Optional.empty();
    }

    private EmailContent ofEmailContent(String bodyStr, String subjectStr) {
        Body body = Body.builder().text(Content.builder().data(bodyStr).build()).build();
        Content subject = Content.builder().data(subjectStr).build();

        Message message = Message.builder().body(body).subject(subject).build();

        return EmailContent.builder()
                .simple(message)
                .build();
    }


    public void sendSimpleEmail(String body, String subject, String targetEmail) {
        EmailContent content = ofEmailContent(body, subject);
        send(content, targetEmail);
    }

    public void sendSignUpEmail(String targetEmail, String token) {
        Map<String, String> contentMap = new HashMap<>();

        contentMap.put("activate_url", CLIENT_URL + "/signup/confirm?token=" + token);
        Optional<EmailContent> content = ofEmailContent(EmailTemplate.PUFFIN_PARTNER_SIGN_UP, contentMap);
        send(content, targetEmail);
//        sendPromotionMail(content, targetEmail);
    }

    public void sendPasswordResetEmail(String targetEmail, String name, String token) {
        Map<String, String> contentMap = new HashMap<>();

        contentMap.put("activate_url", CLIENT_URL + "/reset-password/confirm?token=" + token);
        contentMap.put("userName", name);
        Optional<EmailContent> content = ofEmailContent(EmailTemplate.PUFFIN_PARTNER_PASSWORD_RESET, contentMap);
        send(content, targetEmail);
    }

    public void sendCreatorPasswordResetEmail(String targetEmail, String token) {
        Map<String, String> contentMap = new HashMap<>();

        contentMap.put("activate_url", CLIENT_URL + "/creator/reset-password/confirm?token=" + token);
        contentMap.put("User Name", "test comp");
        Optional<EmailContent> content = ofEmailContent(EmailTemplate.PUFFIN_PARTNER_SIGN_UP, contentMap);
        send(content, targetEmail);
    }


    @Async
    public void sendBulkCompanyInvitationEmails(Map<String, String> tokenEmailMap, String companyName) {
        for (Map.Entry<String, String> entry : tokenEmailMap.entrySet()) {
            Map<String, String> contentMap = new HashMap<>();
            contentMap.put("activate_url", CLIENT_URL + "/process-invitation?token=" + entry.getValue());
            contentMap.put("brandName", companyName);
            Optional<EmailContent> content = ofEmailContent(EmailTemplate.PUFFIN_PARTNER_INVITE, contentMap);
            send(content, entry.getKey());
        }
    }

    @Async
    public void sendCompanyInvitaionEmail(String targetEmail, String token, String companyName) {
        Map<String, String> contentMap = new HashMap<>();

        contentMap.put("activate_url", CLIENT_URL + "/process-invitation?token=" + token);
        contentMap.put("brandName", companyName);
        Optional<EmailContent> content = ofEmailContent(EmailTemplate.PUFFIN_PARTNER_INVITE, contentMap);
        send(content, targetEmail);
    }

}
