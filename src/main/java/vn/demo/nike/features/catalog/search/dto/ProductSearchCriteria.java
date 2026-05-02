package vn.demo.nike.features.catalog.search.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductSearchCriteria {
    String query;
    String category;
    String sort;
    int page;
    int pageSize;

    public void normalize() {
        if (query != null) {
            query = query.trim();
            if (query.isEmpty()) query = null;
        }
        if (category != null) {
            category = category.trim();
            if (category.isEmpty()) category = null;
        }

        if (sort == null || sort.isBlank()) {
            sort = "newest";
        }

        if (page < 1) {
            page = 1;
        }

        pageSize = 24;
    }
}
