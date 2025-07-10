package mardi.erp_mini.api.presentation.scenario;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ScenarioRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.ScenarioResponse;
import mardi.erp_mini.security.AuthUtil;
import mardi.erp_mini.service.ScenarioService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/scenario")
public class ScenarioController {
    private final ScenarioService scenarioService;

    @Operation(summary = "시나리오 목록 조회")
    @GetMapping
    public CommonResponse<ScenarioResponse.ListRes> getList(@RequestParam Long brandId) {
        Long sessionUserId = AuthUtil.getUserId();
        return new CommonResponse(scenarioService.getList(sessionUserId, brandId));
    }

    @Operation(summary = "시나리오 생성")
    @PostMapping
    public CommonResponse<Long> post(@RequestParam Long brandId, @RequestBody ScenarioRequest.Create dto) {
        Long sessionUserId = AuthUtil.getUserId();
        return new CommonResponse<>(scenarioService.post(sessionUserId, dto.getName(), brandId));
    }

    @Operation(summary = "시나리오 삭제 (soft)")
    @DeleteMapping("/{id}")
    public CommonResponse<Long> delete(@PathVariable Long id) {
        Long sessionUserId = AuthUtil.getUserId();
        scenarioService.delete(sessionUserId, id);
        return CommonResponse.ok();
    }

    @Operation(summary = "시나리오 활성 / 비활성")
    @PatchMapping("/{id}/status")
    public CommonResponse<Long> updateStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        Long sessionUserId = AuthUtil.getUserId();
        scenarioService.updateActiveStatus(sessionUserId, id, isActive);
        return CommonResponse.ok();
    }

}




