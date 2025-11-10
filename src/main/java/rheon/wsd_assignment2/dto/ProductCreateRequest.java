package rheon.wsd_assignment2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "상품 생성 요청")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    @Schema(description = "상품명", example = "노트북")
    @NotBlank(message = "Product name is required")
    private String name;

    @Schema(description = "가격", example = "1500000")
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Integer price;

    @Schema(description = "상품 설명", example = "고성능 게이밍 노트북")
    private String description;

    @Schema(description = "재고 수량", example = "10")
    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;
}