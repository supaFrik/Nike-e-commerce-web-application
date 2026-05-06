package vn.demo.nike.infras.storage.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.demo.nike.features.admin.product.exception.InvalidUploadedImageException;
import vn.demo.nike.features.admin.product.model.ImageMetaData;
import vn.demo.nike.features.admin.product.service.ProductImageStorageService;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageStorageService implements ProductImageStorageService {

    private final Cloudinary cloudinary;

    @Override
    public ImageMetaData upload(byte[] content, String productSlug, String colorSlug) {
        if (content == null || content.length == 0) {
            throw new InvalidUploadedImageException("Uploaded image content must not be empty");
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    content,
                    ObjectUtils.asMap(
                            "folder", buildFolder(productSlug, colorSlug),
                            "resource_type", "image"
                    )
            );

            String imageUrl = (String) result.get("secure_url");
            String providerPublicId = (String) result.get("public_id");

            if (imageUrl == null || imageUrl.isBlank() || providerPublicId == null || providerPublicId.isBlank()) {
                throw new InvalidUploadedImageException("Cloudinary did not return complete image metadata");
            }

            return new ImageMetaData(imageUrl, providerPublicId);
        } catch (IOException ex) {
            throw new InvalidUploadedImageException("Failed to upload image to Cloudinary");
        }
    }

    @Override
    public void delete(String providerPublicId) {
        if (providerPublicId == null || providerPublicId.isBlank()) {
            return;
        }

        try {
            cloudinary.uploader().destroy(
                    providerPublicId,
                    ObjectUtils.asMap("resource_type", "image")
            );
        } catch (IOException ex) {
            throw new InvalidUploadedImageException("Failed to delete image from Cloudinary");
        }
    }

    private String buildFolder(String productSlug, String colorSlug) {
        return "nike/products/" + sanitizePathSegment(productSlug) + "/" + sanitizePathSegment(colorSlug);
    }

    private String sanitizePathSegment(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidUploadedImageException("Cloudinary folder segment must not be blank");
        }

        String normalized = value.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+", "")
                .replaceAll("-+$", "");

        if (normalized.isBlank()) {
            throw new InvalidUploadedImageException("Invalid Cloudinary folder segment: " + value);
        }

        return normalized;
    }
}
