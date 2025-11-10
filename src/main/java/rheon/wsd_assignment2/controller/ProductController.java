package rheon.wsd_assignment2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rheon.wsd_assignment2.common.ApiResponse;
import rheon.wsd_assignment2.dto.ErrorResponse;
import rheon.wsd_assignment2.dto.ProductCreateRequest;
import rheon.wsd_assignment2.dto.ProductResponse;
import rheon.wsd_assignment2.dto.ProductStockUpdateRequest;
import rheon.wsd_assignment2.dto.ProductUpdateRequest;
import rheon.wsd_assignment2.service.ProductService;

import java.util.List;

@Tag(name = "Product API", description = "상품 관리 API")
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "상품 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"status\": \"error\", \"data\": null, \"message\": \"Validation failed\"}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "중복된 상품명",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"status\": \"error\", \"data\": null, \"message\": \"Product with name '노트북' already exists\"}"
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        ProductResponse product = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(product, "Product created successfully"));
    }

    @Operation(summary = "대량 상품 생성", description = "대량으로 상품을 생성합니다. (내부 서버 오류 처리 시연)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "상품 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"status\": \"error\", \"data\": null, \"message\": \"Internal server error occurred\"}"
                            )
                    )
            )
    })
    @PostMapping("/bulk")
    public ResponseEntity<ApiResponse<ProductResponse>> bulkCreateProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        ProductResponse product = productService.bulkCreateProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(product, "Product created in bulk successfully"));
    }

    @Operation(summary = "상품 조회", description = "ID로 특정 상품을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "상품을 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"status\": \"error\", \"data\": null, \"message\": \"Product not found with id: 1\"}"
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "상품 ID") @PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(product));
    }

    @Operation(summary = "상품 검색", description = "상품명으로 상품을 검색합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "검색 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "검색 결과 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"status\": \"error\", \"data\": null, \"message\": \"No products found with name: 노트북\"}"
                            )
                    )
            )
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchProducts(
            @Parameter(description = "검색할 상품명") @RequestParam String name) {
        List<ProductResponse> products = productService.searchProducts(name);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(products, "Products found"));
    }

    @Operation(summary = "상품 정보 수정", description = "상품의 전체 정보를 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "상품을 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"status\": \"error\", \"data\": null, \"message\": \"Product not found with id: 1\"}"
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @Parameter(description = "상품 ID") @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {
        ProductResponse product = productService.updateProduct(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(product, "Product updated successfully"));
    }

    @Operation(summary = "상품 재고 수정", description = "상품의 재고만 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "재고 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "상품을 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"status\": \"error\", \"data\": null, \"message\": \"Product not found with id: 1\"}"
                            )
                    )
            )
    })
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductStock(
            @Parameter(description = "상품 ID") @PathVariable Long id,
            @Valid @RequestBody ProductStockUpdateRequest request) {
        ProductResponse product = productService.updateProductStock(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(product, "Product stock updated successfully"));
    }

    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "상품을 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"status\": \"error\", \"data\": null, \"message\": \"Product not found with id: 1\"}"
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "상품 ID") @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Operation(summary = "전체 상품 삭제", description = "모든 상품을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = "{\"status\": \"error\", \"data\": null, \"message\": \"Internal server error occurred\"}"
                            )
                    )
            )
    })
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
