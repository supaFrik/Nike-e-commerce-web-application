package vn.devpro.javaweb32.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.devpro.javaweb32.dto.product.ProductColorDto;
import vn.devpro.javaweb32.dto.product.ProductDetailDto;
import vn.devpro.javaweb32.dto.product.ProductImageDto;
import vn.devpro.javaweb32.dto.product.ProductVariantDto;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.repository.ProductRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String pathSegment(String s) {
        if (s == null) return null;
        return URLDecoder.decode(s.trim(), StandardCharsets.UTF_8);
    }

    private List<String> buildImageUrl(String productName, String colorFolderPath, String baseImage, int imageCount) {
        List<String> imageUrls = new ArrayList<>();

        if (productName == null || colorFolderPath == null || baseImage == null) {
            return null;
        }

        for (int i = 1; i <= imageCount; i++) {
            String productFolder = pathSegment(productName);
            String colorFolder = pathSegment(colorFolderPath);
            String fileName = pathSegment(baseImage.toUpperCase().replace("%20", "+"));


            StringBuilder urlBuilder = new StringBuilder("/images/products/");
            urlBuilder.append(productFolder);
            urlBuilder.append("/").append(colorFolder);
            urlBuilder.append("/").append(fileName);
            urlBuilder.append("-").append(i);
            if (!fileName.toLowerCase().endsWith(".avif")) {
                urlBuilder.append(".avif");
            }

            // URL: /images/product/Nike Air Max Dn8/Black/AIR+MAX+DN8-1.avif
            imageUrls.add(urlBuilder.toString());
        }
        return imageUrls;
    }

    public List<String> getProductMainImageUrl(Product product) {
        if (product != null && product.getColors() != null && !product.getColors().isEmpty()) {
            ProductColor firstColor = product.getColors().get(0);
            return buildImageUrl(product.getName(), firstColor.getFolderPath(), firstColor.getBaseImage(), 1);
        }
        return buildImageUrl("default-product", "default", "default-product.avif", 1);
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
                        v.getColorName()
                ))
                .toList();

        var images = product.getImages().stream()
                .map(i -> new ProductImageDto(i.getId(), i.getUrl(), i.getProduct()))
                .toList();

        var colors = product.getColors().stream()
                .map(c -> {
                    List<String> imageUrls = buildImageUrl(product.getName(), c.getFolderPath(), c.getBaseImage(), 1);
                    String imageUrl = (imageUrls != null && !imageUrls.isEmpty()) ? imageUrls.get(0) : null;
                    return new ProductColorDto(c.getId(), c.getColorName(), c.getFolderPath(), imageUrl);
                })
                .toList();

        return new ProductDetailDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStatus(),
                product.isFavourites(),
                product.getCreatedAt(),
                variants,
                images,
                colors,
                product.getCategory()
        );
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategory_NameIgnoreCase(categoryName,
                Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
