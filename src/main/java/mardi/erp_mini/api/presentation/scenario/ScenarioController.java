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
    public CommonResponse<ScenarioResponse.ListRes> getList(@RequestParam String brandCode) {
        return new CommonResponse(scenarioService.getList(brandCode));
    }

    @Operation(summary = "시나리오 생성")
    @PostMapping
    public CommonResponse<Long> create(@RequestParam String brandCode, @RequestBody ScenarioRequest.Create request) {
        return new CommonResponse<>(scenarioService.create(request, brandCode));
    }

    @Operation(summary = "시나리오 삭제 (soft)")
    @DeleteMapping("/{scenarioId}")
    public CommonResponse<Long> delete(@PathVariable Long scenarioId) {
        scenarioService.delete(scenarioId);
        return CommonResponse.ok();
    }

    @Operation(summary = "시나리오 활성 / 비활성")
    @PatchMapping("/{scenarioId}/status")
    public CommonResponse<Long> updateStatus(@PathVariable Long scenarioId, @RequestParam boolean isActive) {
        scenarioService.updateActiveStatus(scenarioId, isActive);
        return CommonResponse.ok();
    }

}




