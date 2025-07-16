package mardi.erp_mini.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserByResponse {
    @Schema(description = "사용자id", example = "1")
    Long id;
    @Schema(description = "사용자 이름", example = "관리자")
    String name;
    @Schema(description = "사용자 프로필 이미지")
    String imageUrl;
}
