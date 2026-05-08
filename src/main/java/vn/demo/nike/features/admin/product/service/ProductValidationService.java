package vn.demo.nike.features.admin.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.dto.request.AdminProductColorRequest;
import vn.demo.nike.features.admin.product.dto.request.AdminProductCreateRequest;
import vn.demo.nike.features.admin.product.dto.request.AdminProductImageRequest;
import vn.demo.nike.features.admin.product.dto.request.AdminProductVariantRequest;
import vn.demo.nike.features.admin.product.exception.InvalidProductColorException;
import vn.demo.nike.features.admin.product.exception.InvalidSalePriceException;
import vn.demo.nike.features.admin.product.exception.ProductColorNotFoundException;
import vn.demo.nike.features.catalog.product.entity.ProductImage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductValidationService {

    void validateCreateOrUpdateRequest(AdminProductCreateRequest request,
                                       Map<String, MultipartFile> uploadedFiles,
                                       Map<Long, ProductImage> existingImages) {
        validateSalePrice(request);
        validateDuplicateColor(request.getColors());
        validateColorBlocks(request.getColors(), uploadedFiles, existingImages);
    }

    void validateSalePrice(AdminProductCreateRequest request) {
        if (request.getSalePrice() != null && request.getSalePrice().compareTo(request.getPrice()) > 0) {
            throw new InvalidSalePriceException();
        }
    }

    void validateDuplicateColor(List<AdminProductColorRequest> colors) {
        if (colors == null || colors.isEmpty()) {
            throw new ProductColorNotFoundException();
        }

        Set<String> existingColorNames = new HashSet<>();
        for (AdminProductColorRequest color : colors) {
            String normalizedName = vn.demo.nike.shared.util.StringUtil.normalize(color.getColorName());
            if (!existingColorNames.add(normalizedName)) {
                throw new InvalidProductColorException("Duplicate color name: " + normalizedName);
            }
        }
    }

    void validateColorBlocks(List<AdminProductColorRequest> colors,
                             Map<String, MultipartFile> uploadedFiles,
                             Map<Long, ProductImage> existingImages) {
        if (colors == null || colors.isEmpty()) {
            throw new ProductColorNotFoundException();
        }

        Set<String> referencedClientKeys = new HashSet<>();
        Set<Long> referencedExistingImageIds = new HashSet<>();

        for (AdminProductColorRequest color : colors) {
            if (color.getImages() == null || color.getImages().isEmpty()) {
                throw new InvalidProductColorException("Each color must have at least one image.");
            }
            if (color.getVariants() == null || color.getVariants().isEmpty()) {
                throw new InvalidProductColorException("Each color must have at least one variant.");
            }

            long mainImageCount = color.getImages().stream()
                    .filter(image -> Boolean.TRUE.equals(image.getIsMainForColor()))
                    .count();
            if (mainImageCount != 1) {
                throw new InvalidProductColorException("Color " + color.getColorName() + " must have exactly one main image.");
            }

            Set<String> existingSizes = new HashSet<>();
            for (AdminProductVariantRequest variant : color.getVariants()) {
                String size = vn.demo.nike.shared.util.StringUtil.normalize(variant.getSize());
                if (!existingSizes.add(size)) {
                    throw new InvalidProductColorException("Duplicate size '" + variant.getSize() + "' in color " + color.getColorName());
                }
            }

                    for (AdminProductImageRequest image : color.getImages()) {
                        validateImageReference(image, uploadedFiles, existingImages, referencedClientKeys, referencedExistingImageIds);
            }
        }

        for (String uploadedKey : uploadedFiles.keySet()) {
            if (!referencedClientKeys.contains(uploadedKey)) {
                throw new InvalidProductColorException("Uploaded file '" + uploadedKey + "' is not referenced by any colorway.");
            }
        }
    }

    void validateImageReference(AdminProductImageRequest image,
                                Map<String, MultipartFile> uploadedFiles,
                                Map<Long, ProductImage> existingImages,
                                Set<String> referencedClientKeys,
                                Set<Long> referencedExistingImageIds) {
        Long existingImageId = image.getExistingImageId();
        String clientKey = vn.demo.nike.shared.util.StringUtil.normalize(image.getClientKey());

        boolean hasExistingImage = existingImageId != null;
        boolean hasClientKey = !clientKey.isBlank();

        if (hasExistingImage == hasClientKey) {
            throw new InvalidProductColorException("Each image must reference exactly one source: existingImageId or clientKey.");
        }

        if (hasExistingImage) {
            if (!existingImages.containsKey(existingImageId)) {
                throw new InvalidProductColorException("Existing image does not belong to this product: " + existingImageId);
            }
            if (!referencedExistingImageIds.add(existingImageId)) {
                throw new InvalidProductColorException("Duplicate existing image reference: " + existingImageId);
            }
            return;
        }

        if (!uploadedFiles.containsKey(clientKey)) {
            throw new InvalidProductColorException("Image key '" + image.getClientKey() + "' not found in uploaded files.");
        }
        if (!referencedClientKeys.add(clientKey)) {
            throw new InvalidProductColorException("Duplicate uploaded image reference: " + image.getClientKey());
        }
    }
}