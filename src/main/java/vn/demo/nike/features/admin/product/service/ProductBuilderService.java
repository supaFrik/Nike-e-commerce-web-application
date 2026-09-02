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
            applyColorFields(color, colorRequest, product);
            for (AdminProductVariantRequest vr : colorRequest.getVariants()) {
                color.getVariants().add(newVariant(color, vr));
            }
            for (AdminProductImageRequest ir : colorRequest.getImages()) {
                color.getImages().add(newImage(color, ir, uploadedFiles, existingImages, product.getName()));
            }
            product.getColors().add(color);
        }
    }

    void rebuildColorBlocksKeepingExistingIds(Product product,
                                              AdminProductCreateRequest request,
                                              Map<String, MultipartFile> uploadedFiles,
                                              Map<Long, ProductImage> existingImages) {
        Map<String, ProductColor> existingColors = new HashMap<>();
        for (ProductColor c : product.getColors()) {
            existingColors.put(StringUtil.normalize(c.getColorName()), c);
        }
        Set<ProductColor> keptColors = new HashSet<>();
        for (AdminProductColorRequest cr : request.getColors()) {
            ProductColor color = existingColors.getOrDefault(StringUtil.normalize(cr.getColorName()), new ProductColor());
            applyColorFields(color, cr, product);
            syncVariants(color, cr);
            syncImages(color, cr, uploadedFiles, existingImages, product.getName());
            if (!product.getColors().contains(color)) {
                product.getColors().add(color);
            }
            keptColors.add(color);
        }
        product.getColors().removeIf(c -> !keptColors.contains(c));
    }

    // ponytail: shared helpers — create vs sync reuse same field mapping; add new field here once
    private void applyColorFields(ProductColor color, AdminProductColorRequest req, Product product) {
        color.setColorName(req.getColorName().trim());
        color.setHexCode(req.getHexCode());
        color.setDisplayOrder(req.getDisplayOrder());
        color.setProduct(product);
    }

    private ProductVariant newVariant(ProductColor color, AdminProductVariantRequest req) {
        ProductVariant v = new ProductVariant();
        fillVariant(v, req, color);
        return v;
    }

    private ProductImage newImage(ProductColor color, AdminProductImageRequest req,
                                  Map<String, MultipartFile> uploadedFiles,
                                  Map<Long, ProductImage> existingImages,
                                  String productName) {
        ProductImage img = new ProductImage();
        fillImage(img, color, req, uploadedFiles, existingImages, productName);
        return img;
    }

    private void syncVariants(ProductColor color, AdminProductColorRequest req) {
        Map<String, ProductVariant> existing = new HashMap<>();
        for (ProductVariant v : color.getVariants()) {
            existing.put(StringUtil.normalize(v.getSize()), v);
        }
        Set<ProductVariant> kept = new HashSet<>();
        for (AdminProductVariantRequest vr : req.getVariants()) {
            ProductVariant v = existing.getOrDefault(StringUtil.normalize(vr.getSize()), new ProductVariant());
            fillVariant(v, vr, color);
            if (!color.getVariants().contains(v)) {
                color.getVariants().add(v);
            }
            kept.add(v);
        }
        color.getVariants().removeIf(v -> !kept.contains(v));
    }

    private void syncImages(ProductColor color, AdminProductColorRequest req,
                            Map<String, MultipartFile> uploadedFiles,
                            Map<Long, ProductImage> existingImages,
                            String productName) {
        Set<ProductImage> kept = new HashSet<>();
        for (AdminProductImageRequest ir : req.getImages()) {
            ProductImage img = ir.getExistingImageId() == null ? new ProductImage() : existingImages.get(ir.getExistingImageId());
            if (img == null) img = new ProductImage();
            fillImage(img, color, ir, uploadedFiles, existingImages, productName);
            if (!color.getImages().contains(img)) {
                color.getImages().add(img);
            }
            kept.add(img);
        }
        color.getImages().removeIf(img -> !kept.contains(img));
    }

    private void fillVariant(ProductVariant v, AdminProductVariantRequest req, ProductColor color) {
        v.setSku(req.getSku().trim());
        v.setSize(req.getSize().trim());
        v.setStock(req.getStock());
        v.setActive(req.getActive());
        v.setColor(color);
    }

    private void fillImage(ProductImage img, ProductColor color, AdminProductImageRequest req,
                           Map<String, MultipartFile> uploadedFiles,
                           Map<Long, ProductImage> existingImages,
                           String productName) {
        ImageMetaData meta = productImageResolver.resolveImageMetaData(req, uploadedFiles, existingImages, productName, color.getColorName());
        img.setUrl(meta.url());
        img.setProviderPublicId(meta.providerPublicId());
        img.setTitle(req.getTitle());
        img.setAltText(req.getAltText());
        img.setOrderIndex(req.getOrderIndex());
        img.setIsMainForColor(req.getIsMainForColor());
        img.setColor(color);
    }
}
