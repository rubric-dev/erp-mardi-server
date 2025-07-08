package mardi.erp_mini.api.presentation.user;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.UserResponse;
import mardi.erp_mini.security.AuthUtil;
import mardi.erp_mini.service.UserService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @Operation(summary = "내 정보 확인")
    @GetMapping("/my")
    public CommonResponse<UserResponse.Detail> get() {
        return new CommonResponse<>(userService.getUserById(AuthUtil.getUserId()));
    }

    @Operation(summary = "유저 상세 조회")
    @GetMapping("/{id}")
    public CommonResponse<UserResponse.Detail> getUserById(@PathVariable Long id) {
        return new CommonResponse(userService.getUserById(id));
    }

}
