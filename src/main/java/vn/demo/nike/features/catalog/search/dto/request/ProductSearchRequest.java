package vn.demo.nike.features.catalog.search.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {
    private String query;
    private String category;
    private String sort;
    private Integer page;
    private Integer pageSize;
}