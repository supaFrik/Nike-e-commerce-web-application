package vn.devpro.javaweb32.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.dto.customer.product.ProductColorDto;
import vn.devpro.javaweb32.dto.customer.product.ProductDetailDto;
import vn.devpro.javaweb32.dto.customer.product.ProductImageDto;
import vn.devpro.javaweb32.dto.customer.product.ProductVariantDto;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private String buildImageUrl(String productName, String colorFolderPath, String baseImage) {
        if (productName == null || colorFolderPath == null || baseImage == null) {
            return null;
        }

        StringBuilder urlBuilder = new StringBuilder("/images/products/");
        urlBuilder.append(productName);
        urlBuilder.append("/").append(colorFolderPath);
        urlBuilder.append("/").append(baseImage).append("-1.avif");

        return urlBuilder.toString();
    }

    public String getProductMainImageUrl(Product product) {
        if (product != null && product.getColors() != null && !product.getColors().isEmpty()) {
            ProductColor firstColor = product.getColors().get(0);
            return buildImageUrl(product.getName(), firstColor.getFolderPath(), firstColor.getBaseImage());
        }
        return buildImageUrl("default-product", "default", "default-product");
    }

    public ProductDetailDto getDetail(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found!"));
        return mapToDto(product);
    }

    private ProductDetailDto mapToDto(Product product) {
        var variants = product.getVariants().stream()
                .map(v -> new ProductVariantDto(
                        v.getProduct(),
                        v.getId(),
                        v.getPrice(),
                        v.getSize(),
                        v.getStock(),
                        v.getColor() != null ? v.getColor().getColorName() : "Unknown"
                ))
                .toList();

        var images = product.getImages().stream()
                .map(i -> new ProductImageDto(i.getId(), i.getUrl(), i.getProduct()))
                .toList();

        var colors = product.getColors().stream()
                .map(c -> {
                    String imageUrls = buildImageUrl(product.getName(), c.getFolderPath(), c.getBaseImage());
                    String imageUrl = (imageUrls != null && !imageUrls.isEmpty()) ? imageUrls : null;
                    return new ProductColorDto(c.getId(), c.getColorName(), c.getFolderPath(), c.getBaseImage(), imageUrl);
                })
                .toList();

        return new ProductDetailDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStatus(),
                product.getFavourites(),
                product.getCreateDate(),
                variants,
                images,
                colors,
                product.getCategory()
        );
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategory_NameIgnoreCase(categoryName,
                Sort.by(Sort.Direction.DESC, "createDate"));
    }
}
