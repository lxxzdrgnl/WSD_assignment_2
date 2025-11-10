package rheon.wsd_assignment2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rheon.wsd_assignment2.dto.ProductCreateRequest;
import rheon.wsd_assignment2.dto.ProductResponse;
import rheon.wsd_assignment2.dto.ProductStockUpdateRequest;
import rheon.wsd_assignment2.dto.ProductUpdateRequest;
import rheon.wsd_assignment2.entity.Product;
import rheon.wsd_assignment2.exception.DuplicateResourceException;
import rheon.wsd_assignment2.exception.InternalServerException;
import rheon.wsd_assignment2.exception.ResourceNotFoundException;
import rheon.wsd_assignment2.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Product with name '" + request.getName() + "' already exists");
        }

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .stock(request.getStock())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created: {}", savedProduct.getId());
        return ProductResponse.from(savedProduct);
    }

    @Transactional
    public ProductResponse bulkCreateProduct(ProductCreateRequest request) {
        try {
            if (productRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException("Product with name '" + request.getName() + "' already exists");
            }

            Product product = Product.builder()
                    .name(request.getName())
                    .price(request.getPrice())
                    .description(request.getDescription())
                    .stock(request.getStock())
                    .build();

            Product savedProduct = productRepository.save(product);
            log.info("Bulk product created: {}", savedProduct.getId());
            return ProductResponse.from(savedProduct);
        } catch (Exception e) {
            throw new InternalServerException("Failed to create product in bulk operation", e);
        }
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductResponse.from(product);
    }

    public List<ProductResponse> searchProducts(String name) {
        List<Product> products = productRepository.findByNameContaining(name);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found with name containing: " + name);
        }
        return products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.updateProduct(
                request.getName(),
                request.getPrice(),
                request.getDescription(),
                request.getStock()
        );

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated: {}", updatedProduct.getId());
        return ProductResponse.from(updatedProduct);
    }

    @Transactional
    public ProductResponse updateProductStock(Long id, ProductStockUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.updateStock(request.getStock());

        Product updatedProduct = productRepository.save(product);
        log.info("Product stock updated: {}", updatedProduct.getId());
        return ProductResponse.from(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
        log.info("Product deleted: {}", id);
    }

    @Transactional
    public void deleteAllProducts() {
        try {
            long count = productRepository.count();
            productRepository.deleteAll();
            log.info("All products deleted. Count: {}", count);
        } catch (Exception e) {
            throw new InternalServerException("Failed to delete all products", e);
        }
    }
}
