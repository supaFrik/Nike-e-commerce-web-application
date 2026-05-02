package vn.demo.nike.features.catalog.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.catalog.search.dto.ProductSearchCriteria;
import vn.demo.nike.features.catalog.search.dto.ProductSearchItemResponse;
import vn.demo.nike.features.catalog.search.dto.ProductSearchPageResponse;
import vn.demo.nike.features.catalog.search.dto.SearchProductProjection;
import vn.demo.nike.features.catalog.search.repository.ProductSearchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    public ProductSearchPageResponse search(ProductSearchCriteria criteria) {
        criteria.normalize();

        Sort sort = resolveSort(criteria.getSort());

        Pageable pageable = buildPageable(criteria, sort);

        Page<SearchProductProjection> page = productSearchRepository.search(criteria.getQuery(), criteria.getCategory(), pageable);

        List<ProductSearchItemResponse> items = mapItems(page.getContent());

        ProductSearchPageResponse response = buildResponse(criteria, page, items);

        return response;
    }

    private ProductSearchPageResponse buildResponse(ProductSearchCriteria criteria, Page<SearchProductProjection> page, List<ProductSearchItemResponse> items) {
        return new ProductSearchPageResponse(
                items,
                page.getTotalElements(),
                criteria.getPage(),
                criteria.getPageSize(),
                page.getTotalPages(),
                criteria.getQuery(),
                criteria.getCategory(),
                criteria.getSort()
        );
    }

    private List<ProductSearchItemResponse> mapItems(List<SearchProductProjection> content) {
        return content.stream().map(
                p -> new ProductSearchItemResponse(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.getSalePrice(),
                        p.getSalePrice() != null,
                        p.getCategoryName(),
                        p.getImageUrl()
                )
        ).toList();
    }

    private Pageable buildPageable(ProductSearchCriteria criteria, Sort sort) {
        return PageRequest.of(
                criteria.getPage() - 1,
                criteria.getPageSize(),
                sort
        );
    }

    private Sort resolveSort(String sort) {
        return switch (sort) {
            case "price-low" -> Sort.by(Sort.Direction.ASC, "price");
            case "price-high" -> Sort.by(Sort.Direction.DESC, "price");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createDate");
            case "featured" -> Sort.by(Sort.Direction.DESC, "createDate");
            default -> Sort.by(Sort.Direction.DESC, "createDate");
        };
    }
}
