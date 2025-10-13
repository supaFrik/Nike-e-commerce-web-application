package vn.devpro.javaweb32.component;

import org.springframework.stereotype.Component;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductImage;

@Component
public class ProductImageResolver {

    private static final String BASE_PATH = "/images/";
    private static final String DEFAULT_IMAGE = "/images/products/default/default-product-1.avif";

    public String resolveColorImage(Product product, ProductColor color) {
        if (product == null) return DEFAULT_IMAGE;
        // 1. Try exact color match in product images
        if (color != null && product.getImages() != null) {
            for (ProductImage img : product.getImages()) {
                if (img != null && img.getColor() != null && img.getColor().equals(color) && img.getPath() != null) {
                    return toPublicPath(img.getPath());
                }
            }
        }
        // 2. Color swatch fallback
        if (color != null && color.getSwatchPath() != null) {
            return toPublicPath(color.getSwatchPath());
        }
        // 3. First product image
        if (product.getImages() != null && !product.getImages().isEmpty() && product.getImages().get(0) != null) {
            String p = product.getImages().get(0).getPath();
            if (p != null) return toPublicPath(p);
        }
        return DEFAULT_IMAGE;
    }

    private String toPublicPath(String relative) {
        if (relative == null || relative.isBlank()) return DEFAULT_IMAGE;
        if (relative.startsWith("/")) return relative; // Web path
        return BASE_PATH + relative; // BASE_PATH kết thúc với /
    }
}
