package mardi.erp_mini.api.presentation.reoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mardi.erp_mini.api.request.ReorderRequest;
import mardi.erp_mini.common.dto.response.CommonResponse;
import mardi.erp_mini.core.response.ReorderResponse;
import mardi.erp_mini.security.AuthUtil;
import mardi.erp_mini.service.ReorderService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reorder")
public class ReorderController {
    private final ReorderService reorderService;

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

    @Operation(summary = "리오더 홈 화면 mockdata 배포")
    @GetMapping("/mock")
    public CommonResponse<List<ReorderResponse.ListRes>> getMockData() {
        try {
            ClassPathResource resource = new ClassPathResource("mardi-mockdata.json");

            ObjectMapper objectMapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);


            List<ReorderResponse.ListRes> mockData = objectMapper.readValue(
                    resource.getInputStream(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ReorderResponse.ListRes.class)
            );
            return new CommonResponse<>(mockData);
        } catch (IOException e) {
            return CommonResponse.ok();
        }
    }
}




