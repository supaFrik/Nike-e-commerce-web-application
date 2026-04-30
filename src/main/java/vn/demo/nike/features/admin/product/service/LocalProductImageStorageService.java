package vn.demo.nike.features.admin.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.exception.InvalidUploadedImageException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalProductImageStorageService implements ProductImageStorageService {

    private final ProductImageStorageProperties storageProperties;

    @Override
    public String store(MultipartFile file, String productSlug, String colorSlug) {
        if(file == null || file.isEmpty()) {
            throw new InvalidUploadedImageException("Uploaded file must not be null or empty");
        }
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidUploadedImageException(
                    "Only image content types are allowed"
            );
        }
        String originalFilename = file.getOriginalFilename();
        String extension = resolveAndValidateExtension(originalFilename);

        String safeProductSlug = sanitizePathSegment(productSlug);
        String safeColorSlug = sanitizePathSegment(colorSlug);

        String safeFilename = UUID.randomUUID() + "." + extension;

        try {
            Path rootPath = Paths.get(storageProperties.getRootDirectory().toUri())
                    .toAbsolutePath()
                    .normalize();

            Path relativePath = Paths.get(
                    safeProductSlug,
                    safeColorSlug,
                    safeFilename
            );

            Path targetPath = rootPath
                    .resolve(relativePath)
                    .normalize();

            if (!targetPath.startsWith(rootPath)) {
                throw new InvalidUploadedImageException(
                        "Invalid storage path"
                );
            }

            Files.createDirectories(targetPath.getParent());

            file.transferTo(targetPath.toFile());

            return relativePath
                    .toString()
                    .replace(File.separatorChar, '/');

        } catch (IOException ex) {
            throw new InvalidUploadedImageException(
                    "Failed to store uploaded image: " + originalFilename
            );
        }
    }

    @Override
    public void delete(String storedPath) {
        if (storedPath == null || storedPath.isBlank()) {
            return;
        }

        Path rootPath = Paths.get(storageProperties.getRootDirectory().toUri())
                .toAbsolutePath()
                .normalize();

        Path targetPath = rootPath.resolve(storedPath).normalize();
        if (!targetPath.startsWith(rootPath)) {
            throw new InvalidUploadedImageException("Invalid storage path");
        }

        try {
            Files.deleteIfExists(targetPath);
        } catch (IOException ex) {
            throw new InvalidUploadedImageException("Failed to delete stored image: " + storedPath);
        }
    }

    private static String resolveAndValidateExtension(String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename);

        if (extension == null) {
            throw new InvalidUploadedImageException(
                    "File extension is required"
            );
        }

        extension = extension.toLowerCase(Locale.ROOT);

        Set<String> allowedExtensions = Set.of(
                "jpg",
                "jpeg",
                "png",
                "webp",
                "avif"
        );

        if (!allowedExtensions.contains(extension)) {
            throw new InvalidUploadedImageException(
                    "Unsupported image extension: " + extension
            );
        }
        return extension;
    }

    private String sanitizePathSegment(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new InvalidUploadedImageException(
                    "Path segment must not be blank"
            );
        }

        String sanitized = slug.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+", "")
                .replaceAll("-+$", "");

        if (sanitized.isEmpty()) {
            throw new InvalidUploadedImageException(
                    "Invalid path segment: " + slug
            );
        }
        return sanitized;
    }

}
