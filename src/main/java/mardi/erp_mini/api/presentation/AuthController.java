package mardi.erp_mini.api.presentation;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.request.AuthRequest;
import mardi.erp_mini.common.dto.response.AuthResponse;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;


    @Operation(summary = "로그인")
    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public CommonResponse<AuthResponse.Login> login(@ModelAttribute @Valid AuthRequest.Login dto) {
        return new CommonResponse<>(authService.loginUser(dto.getEmail(), dto.getPassword()));
    }

//    @Operation(summary = "파트너 탈퇴")
//    @DeleteMapping(value = "/partner")
//    public CommonResponse<Long> withdrawPartner() {
//        return new CommonResponse<>(partnerService.delete(AuthUtil.getPartnerId()));
//    }

    @Operation(summary = "토큰 리프레시")
    @PostMapping("/refresh")
    public CommonResponse<AuthResponse.Login> refresh(@RequestBody @Valid AuthRequest.Refresh dto) {
        return new CommonResponse<>(authService.refresh(dto.getRefreshToken()));
    }

    @Operation(summary = "회원가입 메일 전송")
    @PostMapping("/mail")
    public CommonResponse sendSignUpEmail(@RequestBody @Valid AuthRequest.EmailRequest dto) {
        authService.sendSignUpEmail(dto);
        return CommonResponse.ok();
    }

    @Operation(summary = "패스워드 초기화 메일 전송")
    @PostMapping("/mail/password")
    public CommonResponse sendPasswordResetEmail(@RequestBody @Valid AuthRequest.EmailRequest dto) {
        authService.sendPasswordResetEmail(dto);
        return CommonResponse.ok();
    }

    @Operation(summary = "패스워드 초기화")
    @PatchMapping("/password")
    public CommonResponse initPassword(@RequestBody @Valid AuthRequest.UpdatePassword dto) {
        authService.initPassword(dto.getToken(), dto.getPassword());
        return CommonResponse.ok();
    }

    @Operation(summary = "회원가입 by token")
    @PostMapping(value = "/signup/confirm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<AuthResponse.Login> signUpByToken(@RequestPart(value = "dto") @Valid AuthRequest.CreateByEmail dto,
                                                            @RequestPart(value = "file", required = false) MultipartFile file) {
        AuthResponse.Login loginResponse = authService.signUpByEmail(dto.getToken(), dto.getPassword(), dto.getName(), file);

        return new CommonResponse<>(loginResponse);
    }

    @Operation(summary = "연락처 인증 문자 전송")
    @PostMapping(value = "/creator/phone/sms")
    public CommonResponse sendValidatePhoneMessage(@RequestBody AuthRequest.SmsSend dto) {
        authService.sendValidatePhoneMessage(dto.phone());
        return CommonResponse.ok();
    }

    @Operation(summary = "연락처 인증")
    @PostMapping(value = "/creator/phone")
    public CommonResponse validatePhone(@RequestBody AuthRequest.ValidPhone dto) {
        authService.verifyPhoneBySmsValue(dto.getValue());
        return CommonResponse.ok();
    }
}
