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
import vn.demo.nike.shared.util.StringUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    void rebuildColorBlocksKeepingExistingIds(Product product,
                                              AdminProductCreateRequest request,
                                              Map<String, MultipartFile> uploadedFiles,
                                              Map<Long, ProductImage> existingImages) {
        Map<String, ProductColor> existingColors = new HashMap<>();
        for (ProductColor color : product.getColors()) {
            existingColors.put(StringUtil.normalize(color.getColorName()), color);
        }

        Set<ProductColor> keptColors = new HashSet<>();
        for (AdminProductColorRequest colorRequest : request.getColors()) {
            ProductColor color = existingColors.getOrDefault(
                    StringUtil.normalize(colorRequest.getColorName()),
                    new ProductColor()
            );
            color.setColorName(colorRequest.getColorName().trim());
            color.setHexCode(colorRequest.getHexCode());
            color.setDisplayOrder(colorRequest.getDisplayOrder());
            color.setProduct(product);

            rebuildVariantsKeepingExistingIds(color, colorRequest);
            rebuildImagesKeepingExistingIds(color, colorRequest, uploadedFiles, existingImages, product.getName());

            if (!product.getColors().contains(color)) {
                product.getColors().add(color);
            }
            keptColors.add(color);
        }

        product.getColors().removeIf(color -> !keptColors.contains(color));
    }

    private void rebuildVariantsKeepingExistingIds(ProductColor color, AdminProductColorRequest colorRequest) {
        Map<String, ProductVariant> existingVariants = new HashMap<>();
        for (ProductVariant variant : color.getVariants()) {
            existingVariants.put(StringUtil.normalize(variant.getSize()), variant);
        }

        Set<ProductVariant> keptVariants = new HashSet<>();
        for (AdminProductVariantRequest variantRequest : colorRequest.getVariants()) {
            ProductVariant variant = existingVariants.getOrDefault(
                    StringUtil.normalize(variantRequest.getSize()),
                    new ProductVariant()
            );
            variant.setSku(variantRequest.getSku().trim());
            variant.setSize(variantRequest.getSize().trim());
            variant.setStock(variantRequest.getStock());
            variant.setActive(variantRequest.getActive());
            variant.setColor(color);

            if (!color.getVariants().contains(variant)) {
                color.getVariants().add(variant);
            }
            keptVariants.add(variant);
        }

        color.getVariants().removeIf(variant -> !keptVariants.contains(variant));
    }

    private void rebuildImagesKeepingExistingIds(ProductColor color,
                                                 AdminProductColorRequest colorRequest,
                                                 Map<String, MultipartFile> uploadedFiles,
                                                 Map<Long, ProductImage> existingImages,
                                                 String productName) {
        Set<ProductImage> keptImages = new HashSet<>();
        for (AdminProductImageRequest imageRequest : colorRequest.getImages()) {
            ProductImage image = imageRequest.getExistingImageId() == null
                    ? new ProductImage()
                    : existingImages.get(imageRequest.getExistingImageId());

            if (image == null) {
                image = new ProductImage();
            }

            ImageMetaData metaData = productImageResolver.resolveImageMetaData(
                    imageRequest,
                    uploadedFiles,
                    existingImages,
                    productName,
                    color.getColorName()
            );
            image.setUrl(metaData.url());
            image.setProviderPublicId(metaData.providerPublicId());
            image.setTitle(imageRequest.getTitle());
            image.setAltText(imageRequest.getAltText());
            image.setOrderIndex(imageRequest.getOrderIndex());
            image.setIsMainForColor(imageRequest.getIsMainForColor());
            image.setColor(color);

            if (!color.getImages().contains(image)) {
                color.getImages().add(image);
            }
            keptImages.add(image);
        }

        color.getImages().removeIf(image -> !keptImages.contains(image));
    }
}
