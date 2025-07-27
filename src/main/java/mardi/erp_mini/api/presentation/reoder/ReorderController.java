package mardi.erp_mini.api.presentation.reoder;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ReorderRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.ReorderResponse;
import mardi.erp_mini.security.AuthUtil;
import mardi.erp_mini.service.ReorderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reorder")
public class ReorderController {
    private final ReorderService reorderService;

    @Operation(summary = "생산요청 처리 조회", description = "리오더 생산 요청 목록 조회")
    @GetMapping("/{id}")
    public CommonResponse<List<ReorderResponse.ReorderListRes>> getReorderProductionList(@Valid @RequestBody ReorderRequest.ReorderSearchParam searchParam){
        return new CommonResponse<>(reorderService.getReorderProductionList(searchParam));
    }

    @Operation(summary = "리오더 요청")
    @PostMapping
    public CommonResponse<Long> post(@RequestBody ReorderRequest.Create dto){
        return new CommonResponse<>(reorderService.post(dto));
    }

    @Operation(summary = "리오더 확정")
    @PostMapping("/{id}/confirm")
    public CommonResponse confirm(@PathVariable Long id){
        Long sessionUserId = AuthUtil.getUserId();
        reorderService.confirm(sessionUserId, id);
        return CommonResponse.ok();
    }

    @Operation(summary = "리오더 홈 - 목록 조회", description = "현재 스웨거 예시로 설정 된 두 상품만 가데이터가 있습니다")
    @PostMapping("list")
    public CommonResponse<List<ReorderResponse.ListRes>> getReorderList(@Valid @RequestBody ReorderRequest.SearchParam searchParam){
        return new CommonResponse<>(reorderService.getReorderList(searchParam));
    }
}




