package vn.devpro.javaweb32.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import vn.devpro.javaweb32.common.constant.Jw32Contant;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductColor;
import vn.devpro.javaweb32.entity.product.ProductImage;
import vn.devpro.javaweb32.service.FileStorageService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FileStorageServiceImpl implements FileStorageService, Jw32Contant {

    private static final Logger LOGGER = Logger.getLogger(FileStorageServiceImpl.class.getName());

    @PersistenceContext
    private EntityManager em;

    // helper: kiểm tra mime-type đơn giản
    private boolean isImageMimeType(MultipartFile f) {
        String ct = f.getContentType();
        return ct != null && ct.startsWith("image/");
    }

    private String getExtension(String name) {
        if (name == null) return ".avif";
        int idx = name.lastIndexOf('.');
        if (idx == -1) return ".avif";
        String ext = name.substring(idx).toLowerCase();
        if (!ext.matches("\\.(jpg|jpeg|png|webp|avif|gif)")) return ".avif";
        return ext;
    }

    @Override
    public List<ProductImage> saveTempFile(List<MultipartFile> files, String folderPath, String baseImagePrefix) throws IOException {
        if (files == null) return new ArrayList<>();
        List<ProductImage> result = new ArrayList<>();
        String uid = UUID.randomUUID().toString();
        Path tempDir = Paths.get(FOLDER_UPLOAD, "temp", uid);
        Files.createDirectories(tempDir);
        int idx = 1;
        for (MultipartFile mf : files) {
            if (mf == null || mf.isEmpty()) continue;
            if (!isImageMimeType(mf)) {
                LOGGER.log(Level.WARNING, () -> "Skipping non-image file: " + mf.getOriginalFilename());
                continue;
            }
            String ext = getExtension(mf.getOriginalFilename());
            String fname = baseImagePrefix + "-" + (idx++) + ext;
            Path target = tempDir.resolve(fname);
            try (InputStream in = mf.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
            ProductImage pi = new ProductImage();
            // lưu url relative so với FOLDER_UPLOAD, ví dụ: temp/{uid}/{fname}
            String relative = "temp/" + uid + "/" + fname;
            pi.setPath(relative);
            pi.setStatus("TEMP");
            result.add(pi);
        }
        return result;
    }

    @Override
    public void moveTempToProductFolder(Product product) throws IOException {
        if (product == null) return;
        if (product.getImages() == null) return; // product-level images only; color images optional

        // For simplicity: final folder is products/{productId}/ (ensure id available)
        Long pid = product.getId();
        if (pid == null) {
            // persist first to get ID
            em.persist(product);
            em.flush();
            pid = product.getId();
        }
        final String productFolder = "products/" + pid;
        Path destRoot = Paths.get(FOLDER_UPLOAD, productFolder);
        if (!Files.exists(destRoot)) Files.createDirectories(destRoot);

        for (ProductImage img : product.getImages()) {
            if (img == null) continue;
            String rel = img.getPath();
            if (rel == null) continue;
            if (rel.startsWith("temp/")) {
                Path src = Paths.get(FOLDER_UPLOAD, rel);
                if (!Files.exists(src)) {
                    LOGGER.log(Level.WARNING, () -> "Temp file missing, skip move: " + src);
                    continue;
                }
                Path dest = destRoot.resolve(src.getFileName());
                registerMoveOnCommit(src, dest);
                String newRelative = productFolder + "/" + src.getFileName();
                img.setPath(newRelative);
                img.setStatus("ACTIVE");
            }
        }

        try {
            em.merge(product);
            em.flush();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to persist product image paths: " + ex.getMessage(), ex);
            throw new IOException("Cannot update image paths in DB", ex);
        }
    }

    @Override
    public void registerMoveOnCommit(Path src, Path dest) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            try {
                Files.createDirectories(dest.getParent());
                try {
                    Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException amnse) {
                    Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Immediate move failed: " + src + " -> " + dest, e);
            }
            return;
        }
        final Path s = src;

        // If transaction is active, register to move after commit
        final Path d = dest;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                try {
                    if (s != null && d != null) {
                        Files.createDirectories(d.getParent());
                        try {
                            Files.move(s, d, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                        } catch (AtomicMoveNotSupportedException amnse) {
                            Files.move(s, d, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error moving file after commit: " + s + " -> " + d, ex);
                }
            }
        });
    }
}
