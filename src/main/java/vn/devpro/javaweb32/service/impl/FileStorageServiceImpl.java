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
import java.io.File;
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

        // tạo 1 temp-folder per call: temp/{uuid}/{folderPath}/
        String uid = UUID.randomUUID().toString();
        Path tempDir = Paths.get(FOLDER_UPLOAD, "temp", uid);
        Files.createDirectories(tempDir);

        int idx = 1;
        for (MultipartFile mf : files) {
            if (mf == null || mf.isEmpty()) continue;
            if (!isImageMimeType(mf)) {
                LOGGER.log(Level.WARNING, "Skipping non-image file: " + mf.getOriginalFilename());
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
            pi.setUrl(relative);
            pi.setStatus("TEMP");
            result.add(pi);
        }
        return result;
    }

    @Override
    public void moveTempToProductFolder(Product product) throws IOException {
        if (product == null) return;

        // Collect move operations in-memory
        List<Runnable> moves = new ArrayList<>();
        List<ProductImage> changedImages = new ArrayList<>();

        if (product.getColors() != null) {
            for (ProductColor color : product.getColors()) {
                if (color.getImages() == null) continue;
                for (ProductImage img : color.getImages()) {
                    String url = img.getUrl();
                    if (url == null) continue;

                    // if URL points to temp: "temp/...."
                    if (url.startsWith("temp/")) {
                        Path src = Paths.get(FOLDER_UPLOAD, url);
                        // ensure dest folder exists: FOLDER_UPLOAD + color.folderPath
                        Path destDir = Paths.get(FOLDER_UPLOAD, color.getFolderPath());
                        if (!Files.exists(destDir)) Files.createDirectories(destDir);

                        Path dest = destDir.resolve(src.getFileName());
                        // schedule move on commit (or execute immediately)
                        registerMoveOnCommit(src, dest);

                        // update ProductImage.url to final relative path: e.g. {color.folderPath}/{filename}
                        String newRelative = color.getFolderPath() + "/" + src.getFileName().toString();
                        img.setUrl(newRelative);
                        img.setStatus("ACTIVE");

                        changedImages.add(img);
                    } else {
                        // already non-temp -> keep as is
                    }
                }
            }
        }

        // persist updated urls for ProductImage (we merge product)
        try {
            // merge product so that updated ProductImage urls persisted
            em.merge(product);
            em.flush();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to persist product image URLs after scheduling file moves: " + ex.getMessage(), ex);
            throw new IOException("Không thể cập nhật đường dẫn ảnh trong DB", ex);
        }
    }

    @Override
    public void registerMoveOnCommit(Path src, Path dest) {
        // If no transaction active, attempt immediate move
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            try {
                Files.createDirectories(dest.getParent());
                Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException amnse) {
                // fallback to non-atomic
                try {
                    Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Immediate move failed: " + src + " -> " + dest, e);
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Immediate move failed: " + src + " -> " + dest, e);
            }
            return;
        }

        // If transaction is active, register to move after commit
        final Path s = src;
        final Path d = dest;
        final List<Exception> errors = new CopyOnWriteArrayList<>();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
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
                    // log — không ném ra để không phá vỡ luồng transaction đã commit
                    LOGGER.log(Level.SEVERE, "Error moving file after commit: " + s + " -> " + d, ex);
                    errors.add(ex);
                }
            }
        });
    }
}
