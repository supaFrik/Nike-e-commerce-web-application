package vn.demo.nike.features.admin.category.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.demo.nike.features.admin.category.dto.AdminCategoryCreateRequest;
import vn.demo.nike.features.admin.category.dto.AdminCategoryResponse;
import vn.demo.nike.features.admin.category.service.AdminCategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public ResponseEntity<AdminCategoryResponse> createCategory(@Valid @RequestBody AdminCategoryCreateRequest request) {
        return ResponseEntity.ok(adminCategoryService.createCategory(request));
    }
}
