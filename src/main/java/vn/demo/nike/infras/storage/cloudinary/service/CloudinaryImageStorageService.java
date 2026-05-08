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

    private static final String ROOT_FOLDER = "nike/products";

    private final Cloudinary cloudinary;

    @Override
    public ImageMetaData upload(byte[] content, String productSlug, String colorSlug) {
        validateContent(content);

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    content,
                    ObjectUtils.asMap(
                            "folder", buildFolder(productSlug, colorSlug),
                            "resource_type", "image"
                    )
            );

            return new ImageMetaData(
                    requireValue(result.get("secure_url"), "Missing Cloudinary image URL"),
                    requireValue(result.get("public_id"), "Missing Cloudinary public ID")
            );

        } catch (IOException ex) {
            throw new InvalidUploadedImageException("Failed to upload image");
        }
    }

    @Override
    public void delete(String providerPublicId) {
        if (isBlank(providerPublicId)) {
            return;
        }

        try {
            cloudinary.uploader().destroy(
                    providerPublicId,
                    ObjectUtils.asMap("resource_type", "image")
            );
        } catch (IOException ex) {
            throw new InvalidUploadedImageException("Failed to delete image");
        }
    }

    private void validateContent(byte[] content) {
        if (content == null || content.length == 0) {
            throw new InvalidUploadedImageException("Image content must not be empty");
        }
    }

    private String buildFolder(String productSlug, String colorSlug) {
        return ROOT_FOLDER + "/"
                + sanitize(productSlug) + "/"
                + sanitize(colorSlug);
    }

    private String sanitize(String value) {
        if (isBlank(value)) {
            throw new InvalidUploadedImageException("Invalid Cloudinary folder segment");
        }

        String sanitized = value.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");

        if (sanitized.isBlank()) {
            throw new InvalidUploadedImageException("Invalid Cloudinary folder segment");
        }

        return sanitized;
    }

    private String requireValue(Object value, String message) {
        String result = (String) value;

        if (isBlank(result)) {
            throw new InvalidUploadedImageException(message);
        }

        return result;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}