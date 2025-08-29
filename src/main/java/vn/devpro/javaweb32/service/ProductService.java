package vn.devpro.javaweb32.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.dto.customer.ProductDetailDto;
import vn.devpro.javaweb32.dto.customer.ProductImageDto;
import vn.devpro.javaweb32.dto.customer.ProductVariantDto;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDetailDto getDetail(Long id) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new RuntimeException(("Product Not Found!")));
        return mapToDto(product);
    }

    private ProductDetailDto mapToDto(Product product) {
        var variants = product.getVariants().stream()
                .map(v -> new ProductVariantDto(v.getProduct(), v.getId(), v.getSize(), v.getColor(), v.getStock(), v.getPrice()))
                .toList();

        var images = product.getImages().stream()
                .map(i -> new ProductImageDto(i.getId(), i.getUrl(), i.getProduct()))
                .toList();
        return new ProductDetailDto(product.getId(), product.getName(), product.getPrice(), product.getDescription(), product.getStatus(), product.isFeatured(), product.getCreatedAt(), product.getVariants(), product.getImages(), product.getCategory());
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategory_NameIgnoreCase(categoryName, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
