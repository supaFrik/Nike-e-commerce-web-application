package vn.demo.nike.features.admin.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.dto.request.AdminProductImageRequest;
import vn.demo.nike.features.admin.product.exception.InvalidProductColorException;
import vn.demo.nike.features.admin.product.exception.InvalidUploadedImageException;
import vn.demo.nike.features.admin.product.model.ImageMetaData;
import vn.demo.nike.features.catalog.product.entity.ProductImage;
import vn.demo.nike.shared.util.StringUtil;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductImageResolver {

    private final ProductImageStorageService productImageStorageService;

    ImageMetaData resolveImageMetaData(AdminProductImageRequest imageRequest,
                                       Map<String, MultipartFile> uploadedFiles,
                                       Map<Long, ProductImage> existingImages,
                                       String productName,
                                       String colorName) {
        if (imageRequest.getExistingImageId() != null) {
            ProductImage existingImage = existingImages.get(imageRequest.getExistingImageId());
            if (existingImage == null) {
                throw new InvalidProductColorException("Existing image does not belong to this product: " + imageRequest.getExistingImageId());
            }
            return new ImageMetaData(
                    existingImage.getUrl(),
                    existingImage.getProviderPublicId()
            );
        }

        String clientKey = StringUtil.normalize(imageRequest.getClientKey());
        MultipartFile file = uploadedFiles.get(clientKey);
        if (file == null) {
            throw new InvalidProductColorException("Image key '" + imageRequest.getClientKey() + "' not found in uploaded files.");
        }

        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException ex) {
            throw new InvalidUploadedImageException("Failed to read uploaded image bytes: " + file.getOriginalFilename());
        }

        return productImageStorageService.upload(
                content,
                productName,
                colorName
        );
    }
}