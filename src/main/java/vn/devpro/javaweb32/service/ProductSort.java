package vn.devpro.javaweb32.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.util.List;

@Service
public class ProductSort {
    public Sort sortedProducts(String sort) {
        if ("newest".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        } else if ("price_asc".equals(sort)) {
            return Sort.by(Sort.Direction.ASC, "price");
        } else if ("price_desc".equals(sort)) {
            return Sort.by(Sort.Direction.DESC, "price");
        } else {
            return Sort.by(Sort.Direction.DESC, "featured").and(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
    }
}
