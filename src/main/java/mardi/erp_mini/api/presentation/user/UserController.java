package mardi.erp_mini.api.presentation.user;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.UserResponse;
import mardi.erp_mini.security.AuthUtil;
import mardi.erp_mini.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @Operation(summary = "내 회원 정보 조회")
    @GetMapping("/my")
    public CommonResponse<UserResponse.Detail> getMyDetail() {
        return new CommonResponse<>(userService.getUserById(AuthUtil.getUserId()));
    }

    @Operation(summary = "관리자 회원 정보 조회")
    @GetMapping("/{id}")
    public CommonResponse<UserResponse.Detail> getUserDetail(@PathVariable Long id) {
        return new CommonResponse<>(userService.getUserById(id));
    }

    @Operation(summary = "관리자 회원 목록 조회")
    @GetMapping
    public CommonResponse<List<UserResponse.ListRes>> getUserList() {
        return new CommonResponse<>(userService.getUserList());
    }

    @Operation(summary = "회원 삭제")
    @DeleteMapping("/{id}")
    public CommonResponse<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return CommonResponse.ok();
    }
}
