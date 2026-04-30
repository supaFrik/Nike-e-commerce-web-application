package vn.demo.nike.features.admin.product.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageStorageService {
    String store(MultipartFile file, String productSlug, String colorSlug);

    void delete(String storedPath);
}
