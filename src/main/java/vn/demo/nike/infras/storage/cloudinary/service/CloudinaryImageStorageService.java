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
    private static final int MAX_IMAGE_BYTES = 5 * 1024 * 1024;

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
        if (content.length > MAX_IMAGE_BYTES) {
            throw new InvalidUploadedImageException("Image must not exceed 5 MB");
        }
        if (!looksLikeSupportedImage(content)) {
            throw new InvalidUploadedImageException("Only JPEG, PNG, GIF, and WebP images are supported");
        }
    }

    private boolean looksLikeSupportedImage(byte[] content) {
        return hasPrefix(content, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})
                || hasPrefix(content, new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47})
                || hasPrefix(content, new byte[]{0x47, 0x49, 0x46, 0x38})
                || isWebp(content);
    }

    private boolean isWebp(byte[] content) {
        return content.length >= 12
                && content[0] == 0x52
                && content[1] == 0x49
                && content[2] == 0x46
                && content[3] == 0x46
                && content[8] == 0x57
                && content[9] == 0x45
                && content[10] == 0x42
                && content[11] == 0x50;
    }

    private boolean hasPrefix(byte[] content, byte[] prefix) {
        if (content.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (content[i] != prefix[i]) {
                return false;
            }
        }
        return true;
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
