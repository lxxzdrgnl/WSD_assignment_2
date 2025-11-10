package rheon.wsd_assignment2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "에러 응답")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "상태", example = "error")
    private String status;

    @Schema(description = "데이터 (에러 시 null)", nullable = true)
    private Object data;

    @Schema(description = "에러 메시지")
    private String message;
}
