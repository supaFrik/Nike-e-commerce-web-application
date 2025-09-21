package vn.devpro.javaweb32.service.customer;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductSort {
    public Sort sortedProducts(String sort) {
        if ("newest".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "createDate");
        } else if ("price_asc".equals(sort)) {
            return Sort.by(Sort.Direction.ASC, "price");
        } else if ("price_desc".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "price");
        } else {
            return Sort.by(Sort.Direction.DESC, "favourites").and(Sort.by(Sort.Direction.DESC, "createDate"));
        }
    }
}
