package vn.devpro.javaweb32.controller.administrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vn.devpro.javaweb32.common.constant.Jw32Contant;
import vn.devpro.javaweb32.dto.administrator.ProductCreateRequest;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.service.administrator.AdminProductService;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;

import java.util.*;

@RestController
@RequestMapping("/admin/api/products")
public class ProductAdminApiController implements Jw32Contant {

    private static final Logger log = LoggerFactory.getLogger(ProductAdminApiController.class);

    private final AdminProductService productService;
    private final CategoryAdminService categoryService;

    public ProductAdminApiController(AdminProductService productService,
                                     CategoryAdminService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    /* ============================= CREATE ============================= */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest request) {
        try {
            Product product = productService.createProduct(request, FOLDER_UPLOAD);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "id", product.getId(),
                    "redirectUrl", "/admin/product/list"
            ));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception io) { // IO or unexpected
            log.error("Create product failed", io);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to store images", "message", io.getMessage()));
        }
    }

    /* ============================= NAME CHECK ============================= */
    @GetMapping(value = "/check-name", produces = "application/json")
    public ResponseEntity<?> checkName(@RequestParam(name = "name", required = false) String name,
                                       @RequestParam(name = "excludeId", required = false) Long excludeId) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.ok(Map.of(
                    "conflict", false,
                    "suggestion", null,
                    "normalized", null
            ));
        }
        boolean conflict = productService.existsNameConflict(name, excludeId);
        String suggestion = conflict ? productService.generateNameSuggestion(name, excludeId) : name;
        String normalized = name.toLowerCase().replaceAll("\\s+", "");
        return ResponseEntity.ok(Map.of(
                "conflict", conflict,
                "suggestion", suggestion,
                "normalized", normalized
        ));
    }

    /* ============================= LIST ============================= */
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> listProducts(@RequestParam(value = "keyword", required = false) String keyword,
                                          @RequestParam(value = "categoryId", required = false) Long categoryId,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                          @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                          @RequestParam(value = "sort", required = false, defaultValue = "name") String sort) {
        try {
            var paged = productService.searchPaged(keyword, categoryId, page, size, sort);
            List<Map<String,Object>> items = new ArrayList<>();
            for (Product p : paged.items) {
                try { items.add(productService.buildSummary(p)); }
                catch (Exception inner) {
                    log.warn("Failed to summarize product id={}: {}", p != null ? p.getId() : null, inner.toString());
                    items.add(Map.of("id", p!=null?p.getId():null, "error","summary-failed"));
                }
            }
            return ResponseEntity.ok(new LinkedHashMap<String,Object>() {{
                put("page", paged.page);
                put("pageSize", paged.pageSize);
                put("totalItems", paged.totalItems);
                put("totalPages", paged.totalPages);
                put("items", items);
            }});
        } catch (Exception ex) {
            log.error("/admin/api/products list failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error fetching products", "message", ex.getMessage()));
        }
    }

    /* ============================= PARTIAL UPDATE ============================= */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id,
                                           @RequestBody Map<String,Object> body) {
        try {
            Product updated = productService.partialUpdateProduct(id, body);
            return ResponseEntity.ok(productService.buildSummary(updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(ex.getMessage().contains("not found")? HttpStatus.NOT_FOUND: HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Partial update failed id={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Update failed", "message", ex.getMessage()));
        }
    }

    /* ============================= FULL UPDATE ============================= */
    @PutMapping(value = "/{id}/full", consumes = "application/json", produces = "application/json")
    @Transactional
    public ResponseEntity<?> fullUpdateProduct(@PathVariable("id") Long id,
                                               @RequestBody ProductCreateRequest request) {
        try {
            Product product = productService.fullUpdateProduct(id, request, FOLDER_UPLOAD);
            return ResponseEntity.ok(Map.of("id", product.getId(), "updated", true));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(ex.getMessage().contains("not found")? HttpStatus.NOT_FOUND: HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception io) {
            log.error("Full update failed id={}", id, io);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to store images", "message", io.getMessage()));
        }
    }

    /* ============================= DELETE ============================= */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        Product p = productService.getById(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found"));
        }
        try {
            productService.deleteById(id);
            return ResponseEntity.ok(Map.of("deleted", true, "id", id));
        } catch (Exception ex) {
            log.error("Failed to delete product id={}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete product", "message", ex.getMessage()));
        }
    }
}
