package vn.devpro.javaweb32.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.dto.customer.product.ProductResponseDto;
import vn.devpro.javaweb32.dto.customer.product.ProductSearchCriteria;
import vn.devpro.javaweb32.entity.product.Category;
import vn.devpro.javaweb32.service.administrator.CategoryAdminService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductSearchService {

    @Autowired
    private CustomerProductService productService;

    @Autowired
    private CategoryAdminService categoryService;

    public SearchResult search(ProductSearchCriteria criteria) {
        List<ProductResponseDto> products;
        String categoryName = criteria.getCategory();
        if (categoryName != null && !categoryName.isBlank()) {
            Category cat = categoryService.findByName(categoryName.trim().toLowerCase());
            if (cat != null) {
                products = productService.getProductsByCategory(cat.getId());
            } else {
                products = Collections.emptyList();
            }
        } else {
            products = productService.getAllProducts();
        }

        products = products.stream()
                .filter(p -> criteria.getSaleOnly() == null || !criteria.getSaleOnly() || p.hasSale())
                .filter(p -> criteria.getSize() == null || criteria.getSize().isBlank() || (p.getVariants() != null && p.getVariants().stream().anyMatch(v -> criteria.getSize().equalsIgnoreCase(v.getSize()))))
                .filter(p -> criteria.getMinPrice() == null || (p.getEffectivePrice() != null && p.getEffectivePrice().compareTo(criteria.getMinPrice()) >= 0))
                .filter(p -> criteria.getMaxPrice() == null || (p.getEffectivePrice() != null && p.getEffectivePrice().compareTo(criteria.getMaxPrice()) <= 0))
                .collect(Collectors.toList());

        if (criteria.getSort() != null && !products.isEmpty()) {
            switch (criteria.getSort()) {
                case "price_asc":
                    products.sort(Comparator.comparing(ProductResponseDto::getEffectivePrice, Comparator.nullsLast(BigDecimal::compareTo)));
                    break;
                case "price_desc":
                    products.sort(Comparator.comparing(ProductResponseDto::getEffectivePrice, Comparator.nullsLast(BigDecimal::compareTo)).reversed());
                    break;
                case "newest":
                default:
                    products.sort(Comparator.comparing(ProductResponseDto::getCreateDate, Comparator.nullsLast(Date::compareTo)).reversed());
                    break;
            }
        }

        int total = products.size();
        int pageSize = Math.max(1, Math.min(criteria.getPageSize(), 200));
        int totalPages = (int) Math.ceil(total / (double) pageSize);
        int currentPage = Math.min(Math.max(1, criteria.getPage()), Math.max(1, totalPages == 0 ? 1 : totalPages));
        int from = (currentPage - 1) * pageSize;
        int to = Math.min(from + pageSize, total);
        List<ProductResponseDto> pageItems = total == 0 ? Collections.emptyList() : products.subList(from, to);

        return new SearchResult(pageItems, total, currentPage, totalPages, pageSize);
    }

    public static class SearchResult {
        private final List<ProductResponseDto> items;
        private final int total;
        private final int page;
        private final int totalPages;
        private final int pageSize;

        public SearchResult(List<ProductResponseDto> items, int total, int page, int totalPages, int pageSize) {
            this.items = items;
            this.total = total;
            this.page = page;
            this.totalPages = totalPages;
            this.pageSize = pageSize;
        }

        public List<ProductResponseDto> getItems() { return items; }
        public int getTotal() { return total; }
        public int getPage() { return page; }
        public int getTotalPages() { return totalPages; }
        public int getPageSize() { return pageSize; }
    }
}

