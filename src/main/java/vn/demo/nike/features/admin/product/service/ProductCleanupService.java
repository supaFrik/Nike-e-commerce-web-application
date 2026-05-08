package vn.demo.nike.features.admin.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import vn.demo.nike.features.admin.product.dto.request.AdminProductCreateRequest;
import vn.demo.nike.features.admin.product.dto.request.AdminProductImageRequest;
import vn.demo.nike.features.catalog.product.entity.Product;
import vn.demo.nike.features.catalog.product.entity.ProductImage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCleanupService {

    private final ProductImageStorageService productImageStorageService;


    List<String> collectProviderIdsToDelete(Product product,
                                            AdminProductCreateRequest request,
                                            Map<Long, ProductImage> existingImages) {
        Set<Long> retainedExistingImageIds = request.getColors().stream()
                .flatMap(color -> color.getImages().stream())
                .map(AdminProductImageRequest::getExistingImageId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return existingImages.entrySet().stream()
                .filter(entry -> !retainedExistingImageIds.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(ProductImage::getProviderPublicId)
                .filter(Objects::nonNull)
                .toList();
    }

    List<String> collectAllProviderIds(Product product) {
        return product.getColors().stream()
                .flatMap(color -> color.getImages().stream())
                .map(ProductImage::getProviderPublicId)
                .filter(Objects::nonNull)
                .toList();
    }

    void scheduleDeleteAfterCommit(Collection<String> providerIdsToDelete) {
        if (providerIdsToDelete == null || providerIdsToDelete.isEmpty()) {
            return;
        }

        List<String> immutablePaths = new ArrayList<>(providerIdsToDelete);
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            deleteProviderIdsBestEffort(immutablePaths);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                deleteProviderIdsBestEffort(immutablePaths);
            }
        });
    }

    void deleteProviderIdsBestEffort(List<String> pathsToDelete) {
        pathsToDelete.forEach(path -> {
            try {
                productImageStorageService.delete(path);
            } catch (RuntimeException ex) {
                log.warn("Failed to delete orphan product image {}", path, ex);
            }
        });
    }
}