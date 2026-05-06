package vn.demo.nike.features.catalog.search.entity;

import lombok.Getter;
import vn.demo.nike.features.catalog.search.dto.request.ProductSearchRequest;

@Getter
public class ProductSearchCriteria {

    private final String query;
    private final String category;
    private final String sort;
    private final int page;
    private final int pageSize;

    public ProductSearchCriteria(ProductSearchRequest req) {
        this.query = normalize(req.getQuery());
        this.category = normalize(req.getCategory());
        this.sort = (req.getSort() == null || req.getSort().isBlank()) ? "newest" : req.getSort();
        this.page = (req.getPage() == null || req.getPage() < 1) ? 1 : req.getPage();
        this.pageSize = (req.getPageSize() == null || req.getPageSize() <= 0) ? 24 : req.getPageSize();
    }

    private String normalize(String input) {
        if (input == null) return null;
        String trimmed = input.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
