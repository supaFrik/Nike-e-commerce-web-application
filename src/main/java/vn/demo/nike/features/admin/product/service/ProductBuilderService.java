package vn.demo.nike.features.admin.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.dto.request.AdminProductColorRequest;
import vn.demo.nike.features.admin.product.dto.request.AdminProductCreateRequest;
import vn.demo.nike.features.admin.product.dto.request.AdminProductImageRequest;
import vn.demo.nike.features.admin.product.dto.request.AdminProductVariantRequest;
import vn.demo.nike.features.admin.product.model.ImageMetaData;
import vn.demo.nike.features.catalog.category.entity.Category;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.entity.ProductColor;
import vn.demo.nike.features.catalog.product.entity.ProductImage;
import vn.demo.nike.features.catalog.product.entity.ProductVariant;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductBuilderService {

    private final ProductImageResolver productImageResolver;

    void applyProductFields(Product product, AdminProductCreateRequest request, Category category) {
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription().trim());
        product.setType(request.getType().trim());
        product.setPrice(request.getPrice());
        product.setSalePrice(request.getSalePrice());
        product.setProductStatus(request.getProductStatus());
        product.setCategory(category);
    }

    void rebuildColorBlocks(Product product,
                            AdminProductCreateRequest request,
                            Map<String, MultipartFile> uploadedFiles,
                            Map<Long, ProductImage> existingImages) {
        for (AdminProductColorRequest colorRequest : request.getColors()) {
            ProductColor color = new ProductColor();
            color.setColorName(colorRequest.getColorName().trim());
            color.setHexCode(colorRequest.getHexCode());
            color.setDisplayOrder(colorRequest.getDisplayOrder());
            color.setProduct(product);

            for (AdminProductVariantRequest variantRequest : colorRequest.getVariants()) {
                ProductVariant variant = new ProductVariant();
                variant.setSku(variantRequest.getSku().trim());
                variant.setSize(variantRequest.getSize().trim());
                variant.setStock(variantRequest.getStock());
                variant.setActive(variantRequest.getActive());
                variant.setColor(color);
                color.getVariants().add(variant);
            }

            for (AdminProductImageRequest imageRequest : colorRequest.getImages()) {
                ProductImage image = new ProductImage();
                ImageMetaData metaData = productImageResolver.resolveImageMetaData(imageRequest, uploadedFiles, existingImages, product.getName(), color.getColorName());
                image.setUrl(metaData.url());
                image.setProviderPublicId(metaData.providerPublicId());
                image.setTitle(imageRequest.getTitle());
                image.setAltText(imageRequest.getAltText());
                image.setOrderIndex(imageRequest.getOrderIndex());
                image.setIsMainForColor(imageRequest.getIsMainForColor());
                image.setColor(color);
                color.getImages().add(image);
            }

            product.getColors().add(color);
        }
    }
}