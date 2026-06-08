package vn.demo.nike.features.admin.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.dto.response.AdminProductCategoryOptionResponse;
import vn.demo.nike.features.admin.product.dto.response.AdminCreatedProductResponse;
import vn.demo.nike.features.admin.product.dto.request.AdminProductCreateRequest;
import vn.demo.nike.features.admin.product.dto.response.AdminProductFormResponse;
import vn.demo.nike.features.admin.product.service.AdminProductService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/v1/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("/form-options")
    public ResponseEntity<List<AdminProductCategoryOptionResponse>> getFormOptions() {
        return ResponseEntity.ok(adminProductService.getCategoryOptions());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<AdminProductFormResponse> getProductForm(@PathVariable Long productId) {
        return ResponseEntity.ok(adminProductService.getProductForm(productId));
    }

    @PostMapping(
            value = "/admin/api/v1/products",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AdminCreatedProductResponse> createProduct(
            @RequestPart("productData") AdminProductCreateRequest productData,
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("fileClientKeys") List<String> fileClientKeys
    ) {
        AdminCreatedProductResponse response = adminProductService.createProduct(
                productData,
                files,
                fileClientKeys
        );
        URI location = URI.create("/admin/api/v1/products/" + response.getProductId());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    @PutMapping(
            value = "/{productId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AdminCreatedProductResponse> updateProduct(
            @PathVariable Long productId,
            @RequestPart("productData") AdminProductCreateRequest productData,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "fileClientKeys", required = false) List<String> fileClientKeys
    ) {
        AdminCreatedProductResponse response = adminProductService.updateProduct(
                productId,
                productData,
                files,
                fileClientKeys
        );
        URI location  = URI.create("/admin/api/v1/products/" + response.getProductId());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        adminProductService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
