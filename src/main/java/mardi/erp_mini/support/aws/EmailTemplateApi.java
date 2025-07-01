//package mardi.erp_mini.support.aws;
//
//
//import io.swagger.v3.oas.annotations.Hidden;
//import lombok.*;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import rubric_labs.yedam_server.exception.FileUploadFailedException;
//import software.amazon.awssdk.services.sesv2.SesV2Client;
//import software.amazon.awssdk.services.sesv2.model.CreateEmailTemplateRequest;
//import software.amazon.awssdk.services.sesv2.model.EmailTemplateContent;
//import software.amazon.awssdk.services.sesv2.model.UpdateEmailTemplateRequest;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//@Hidden
//@RequiredArgsConstructor
//@RequestMapping("/email-template")
//@RestController
//public class EmailTemplateApi {
//    private final SesV2Client sesV2Client;
//
//    //form data로
//    @PostMapping
//    public void saveTemplate(CreateTemplateRequest request){
//        registerTemplateToAws(request);
//    }
//
//    @PutMapping
//    public void updateTemplate(UpdateTemplateRequest request){
//        updateTemplateAws(request);
//    }
//
//    public void registerTemplateToAws(CreateTemplateRequest request) {
//        String htmlContent;
//
//        try {
//            htmlContent = new BufferedReader(new InputStreamReader(request.getHtmlPart().getInputStream())).lines()
//                    .reduce("", (accumulator, actual) -> accumulator + actual);
//        } catch (IOException e) {
//            throw new FileUploadFailedException();
//        }
//
//        EmailTemplateContent emailTemplateContent = EmailTemplateContent.builder()
//                .subject(request.getSubject())
//                .html(htmlContent)
//                .build();
//
//        CreateEmailTemplateRequest createEmailTemplateRequest = CreateEmailTemplateRequest.builder()
//                .templateName(request.getTemplateName())
//                .templateContent(emailTemplateContent)
//                .build();
//
//        sesV2Client.createEmailTemplate(createEmailTemplateRequest);
//    }
//
//
//    public void updateTemplateAws(UpdateTemplateRequest request) {
//        String htmlContent;
//
//        try {
//            htmlContent = new BufferedReader(new InputStreamReader(request.getHtmlPart().getInputStream())).lines()
//                    .reduce("", (accumulator, actual) -> accumulator + actual);
//        } catch (IOException e) {
//            throw new FileUploadFailedException();
//        }
//
//        EmailTemplateContent emailTemplateContent = EmailTemplateContent.builder()
//                .subject(request.getSubject())
//                .html(htmlContent)
//                .build();
//
//        UpdateEmailTemplateRequest updateEmailTemplateRequest = UpdateEmailTemplateRequest.builder()
//                .templateName(request.getTemplateName())
//                .templateContent(emailTemplateContent)
//                .build();
//
//        sesV2Client.updateEmailTemplate(updateEmailTemplateRequest);
//    }
//
//
//    @Setter
//    @Getter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class CreateTemplateRequest {
//        private String templateName;
//        private MultipartFile htmlPart;
//        private String subject;
//    }
//
//    @Setter
//    @Getter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class UpdateTemplateRequest {
//        private String templateName;
//        private MultipartFile htmlPart;
//        private String subject;
//    }
//}
//
//
//
