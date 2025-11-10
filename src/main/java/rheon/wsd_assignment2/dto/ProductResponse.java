package rheon.wsd_assignment2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rheon.wsd_assignment2.entity.Product;

import java.time.LocalDateTime;

@Schema(description = "상품 응답")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품명", example = "노트북")
    private String name;

    @Schema(description = "가격", example = "1500000")
    private Integer price;

    @Schema(description = "상품 설명", example = "고성능 게이밍 노트북")
    private String description;

    @Schema(description = "재고 수량", example = "10")
    private Integer stock;

    @Schema(description = "생성일시", example = "2025-11-10T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-11-10T12:00:00")
    private LocalDateTime updatedAt;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
