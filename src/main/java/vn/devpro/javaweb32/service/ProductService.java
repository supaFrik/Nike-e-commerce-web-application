package vn.devpro.javaweb32.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.dto.product.ProductColorDto;
import vn.devpro.javaweb32.dto.product.ProductDetailDto;
import vn.devpro.javaweb32.dto.product.ProductImageDto;
import vn.devpro.javaweb32.dto.product.ProductVariantDto;
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
                .map(v -> new ProductVariantDto(v.getProduct(), v.getId(), v.getPrice(), v.getSize(), v.getStock(), v.getColorName()))
                .toList();

        var images = product.getImages().stream()
                .map(i -> new ProductImageDto(i.getId(), i.getUrl(), i.getProduct()))
                .toList();

        var colors = product.getColors().stream()
                .map(c -> new ProductColorDto(c.getId(), c.getColorName(), c.getFolderPath()))
                .toList();
        return new ProductDetailDto(product.getId(), product.getName(), product.getPrice(), product.getDescription(), product.getStatus(), product.isFavourites(), product.getCreatedAt(), variants, images, colors, product.getCategory());
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategory_NameIgnoreCase(categoryName, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
