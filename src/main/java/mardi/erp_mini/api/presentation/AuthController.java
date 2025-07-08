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

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;

    @Operation(summary= "사용자 추가")
    @PostMapping("/create")
    public CommonResponse<Void> create(@RequestBody AuthRequest.Create request){
        authService.createUser(request);
        return CommonResponse.ok();
    }

    @Operation(summary = "로그인")
    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public CommonResponse<AuthResponse.Login> login(@ModelAttribute @Valid AuthRequest.Login dto) {
        return new CommonResponse<>(authService.loginUser(dto.getEmail(), dto.getPassword()));
    }

    @Operation(summary = "토큰 리프레시")
    @PostMapping("/refresh")
    public CommonResponse<AuthResponse.Login> refresh(@RequestBody @Valid AuthRequest.Refresh dto) {
        return new CommonResponse<>(authService.refresh(dto.getRefreshToken()));
    }

}
