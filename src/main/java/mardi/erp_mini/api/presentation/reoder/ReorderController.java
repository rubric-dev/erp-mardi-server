package mardi.erp_mini.api.presentation.reoder;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ReorderRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.security.AuthUtil;
import mardi.erp_mini.service.ReorderService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reorder")
public class ReorderController {
    private final ReorderService reorderService;

    @Operation(summary = "리오더 요청")
    @PostMapping
    public CommonResponse<Long> post(@RequestBody ReorderRequest.Create dto){
        Long sessionUserId = AuthUtil.getUserId();
        return new CommonResponse<>(reorderService.post(sessionUserId, dto));
    }

    @Operation(summary = "리오더 확정")
    @PostMapping("/{id}/confirm")
    public CommonResponse confirm(@PathVariable Long id){
        Long sessionUserId = AuthUtil.getUserId();
        reorderService.confirm(sessionUserId, id);
        return CommonResponse.ok();
    }
}




