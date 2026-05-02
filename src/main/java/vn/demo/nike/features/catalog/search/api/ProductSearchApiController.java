package vn.demo.nike.features.catalog.search.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import vn.demo.nike.features.catalog.search.dto.ProductSearchCriteria;
import vn.demo.nike.features.catalog.search.dto.ProductSearchPageResponse;
import vn.demo.nike.features.catalog.search.service.ProductSearchService;

@Controller
@RequiredArgsConstructor
public class ProductSearchApiController {

    private final ProductSearchService  productSearchService;

    @GetMapping("/api/products/search")
    public ResponseEntity<ProductSearchPageResponse> search(ProductSearchCriteria criteria) {
        return ResponseEntity.ok(productSearchService.search(criteria));
    }
}
