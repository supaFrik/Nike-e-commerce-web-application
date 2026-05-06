package vn.demo.nike.features.catalog.search.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import vn.demo.nike.features.catalog.search.dto.request.ProductSearchRequest;
import vn.demo.nike.features.catalog.search.dto.response.ProductSearchPageResponse;
import vn.demo.nike.features.catalog.search.entity.ProductSearchCriteria;
import vn.demo.nike.features.catalog.search.service.ProductSearchService;

@Controller
@RequiredArgsConstructor
public class ProductSearchApiController {

    private final ProductSearchService  productSearchService;

    @GetMapping("/api/products/search")
    public ResponseEntity<ProductSearchPageResponse> search(ProductSearchRequest criteria) {
        return ResponseEntity.ok(productSearchService.search(new ProductSearchCriteria(criteria)));
    }
}
