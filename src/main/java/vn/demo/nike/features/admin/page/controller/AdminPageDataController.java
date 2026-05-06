package vn.demo.nike.features.admin.page.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.demo.nike.features.admin.page.dto.response.AdminCategoryListItemResponse;
import vn.demo.nike.features.admin.page.dto.response.AdminDashboardResponse;
import vn.demo.nike.features.admin.page.dto.response.AdminOrderListItemResponse;
import vn.demo.nike.features.admin.page.dto.response.AdminProductInventoryItemResponse;
import vn.demo.nike.features.admin.page.service.AdminPageDataService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminPageDataController {

    private final AdminPageDataService adminPageDataService;

    @GetMapping("/admin/api/page-data/products")
    public ResponseEntity<List<AdminProductInventoryItemResponse>> getProductInventory() {
        return ResponseEntity.ok(adminPageDataService.listProductInventory());
    }

    @GetMapping("/admin/api/page-data/categories")
    public ResponseEntity<List<AdminCategoryListItemResponse>> getCategories() {
        return ResponseEntity.ok(adminPageDataService.listCategories());
    }

    @GetMapping("/admin/api/page-data/orders")
    public ResponseEntity<List<AdminOrderListItemResponse>> getOrders() {
        return ResponseEntity.ok(adminPageDataService.listOrders());
    }

    @GetMapping("/admin/api/page-data/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {
        return ResponseEntity.ok(adminPageDataService.getDashboard());
    }
}
