package vn.devpro.javaweb32.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductApiController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}/sizes")
    public ResponseEntity<List<String>> getSizesByColor(
            @PathVariable Long id,
            @RequestParam String color
    ) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        List<String> sizes = product.getVariants().stream()
                .filter(v -> v.getColor() != null && v.getColor().getColorName() != null &&
                           v.getColor().getColorName().equalsIgnoreCase(color))
                .map(ProductVariant::getSize)
                .distinct()
                .toList();
        return ResponseEntity.ok(sizes);
    }
}
