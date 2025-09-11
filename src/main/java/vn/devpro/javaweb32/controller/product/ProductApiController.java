package vn.devpro.javaweb32.controller.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.util.List;

public class ProductApiController {

    private ProductRepository productRepository;

    public ProductApiController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}/sizes")
    public ResponseEntity<List<String>> getUniqueColor(
            @PathVariable Long id,
            @RequestParam String color
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        List<String> sizes = product.getVariants().stream()
                .filter(v -> v.getColor() != null && v.getColor().equalsIgnoreCase(color))
                .map(ProductVariant::getSize)
                .distinct()
                .toList();

        return ResponseEntity.ok(sizes);
    }
}
