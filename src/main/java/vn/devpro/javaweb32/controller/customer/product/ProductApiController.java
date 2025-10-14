package vn.devpro.javaweb32.controller.customer.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.dto.customer.product.ProductSearchCriteria;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductVariant;
import vn.devpro.javaweb32.repository.ProductRepository;
import vn.devpro.javaweb32.service.customer.ProductSearchService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductRepository productRepository;
    private final ProductSearchService productSearchService;

    @Autowired
    public ProductApiController(ProductRepository productRepository, ProductSearchService productSearchService) {
        this.productRepository = productRepository;
        this.productSearchService = productSearchService;
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
                .map(ProductVariant::getSizeValue)
                .distinct()
                .toList();
        return ResponseEntity.ok(sizes);
    }

    @GetMapping("")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sale", required = false) Boolean sale,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam(value = "minPrice", required = false) java.math.BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) java.math.BigDecimal maxPrice,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "24") Integer pageSize
    ) {
        ProductSearchCriteria c = new ProductSearchCriteria();
        c.setCategory(category);
        c.setSaleOnly(sale);
        c.setSize(size);
        c.setMinPrice(minPrice);
        c.setMaxPrice(maxPrice);
        c.setSort(sort);
        c.setPage(page);
        c.setPageSize(pageSize);
        var result = productSearchService.search(c);
        return ResponseEntity.ok(result);
    }
}
