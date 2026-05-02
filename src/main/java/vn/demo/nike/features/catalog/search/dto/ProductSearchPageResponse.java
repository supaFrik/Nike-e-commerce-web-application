package vn.demo.nike.features.catalog.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductSearchPageResponse {
    List<ProductSearchItemResponse> items;
    long total;
    int page;
    int pageSize;
    int totalPages;
    String query;
    String category;
    String sort;
}
