package vn.demo.nike.features.admin.product.service;

import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.model.ImageMetaData;

public interface ProductImageStorageService {
    ImageMetaData upload(byte[] content, String productSlug, String colorSlug);

    void delete(String storedPath);
}
