package vn.demo.nike.features.catalog.search.entity;

import org.junit.jupiter.api.Test;
import vn.demo.nike.features.catalog.search.dto.request.ProductSearchRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductSearchCriteriaTest {

    @Test
    void constructorNormalizesBlankValuesAndAppliesDefaults() {
        ProductSearchRequest request = new ProductSearchRequest();
        request.setQuery("   ");
        request.setCategory("   ");
        request.setSort(" ");
        request.setPage(0);
        request.setPageSize(0);

        ProductSearchCriteria criteria = new ProductSearchCriteria(request);

        assertNull(criteria.getQuery());
        assertNull(criteria.getCategory());
        assertEquals("newest", criteria.getSort());
        assertEquals(1, criteria.getPage());
        assertEquals(24, criteria.getPageSize());
    }
}
