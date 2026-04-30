package vn.demo.nike.features.admin.product.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.dto.AdminProductCategoryOptionResponse;
import vn.demo.nike.features.admin.product.dto.AdminCreatedProductResponse;
import vn.demo.nike.features.admin.product.dto.AdminProductCreateRequest;
import vn.demo.nike.features.admin.product.dto.AdminProductFormResponse;
import vn.demo.nike.features.admin.product.service.AdminProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping("/admin/api/products/form-options")
    public ResponseEntity<List<AdminProductCategoryOptionResponse>> getFormOptions() {
        return ResponseEntity.ok(adminProductService.getCategoryOptions());
    }

    @GetMapping("/admin/api/products/{productId}")
    public ResponseEntity<AdminProductFormResponse> getProductForm(@PathVariable Long productId) {
        return ResponseEntity.ok(adminProductService.getProductForm(productId));
    }

    @PostMapping(
            value = "/admin/api/products",
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
        return ResponseEntity.ok(response);
    }

    @PutMapping(
            value = "/admin/api/products/{productId}",
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
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/api/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        adminProductService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
