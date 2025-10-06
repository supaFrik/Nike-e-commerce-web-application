package vn.devpro.javaweb32.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.devpro.javaweb32.service.administrator.FileStorageService;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path tempDir;
    private final Path productBaseDir;

    public FileStorageServiceImpl(
            @Value("${app.upload.temp-dir:uploads/tmp}") String tempDir,
            @Value("${app.upload.product-dir:uploads/products}") String productBaseDir
    ) throws IOException {
        this.tempDir = Paths.get(tempDir).toAbsolutePath().normalize();
        this.productBaseDir = Paths.get(productBaseDir).toAbsolutePath().normalize();
        Files.createDirectories(this.tempDir);
        Files.createDirectories(this.productBaseDir);
    }

    @Override
    public Path saveTempFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IOException("Empty file");
        String orig = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int idx = orig.lastIndexOf('.');
        if (idx >= 0) ext = orig.substring(idx).toLowerCase();
        String filename = UUID.randomUUID().toString() + ext;
        Path target = tempDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }

    @Override
    public void registerMoveOnCommit(final Path tempFile, final Path finalAbsolutePath) {
        if (tempFile == null || finalAbsolutePath == null) return;

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    Files.createDirectories(finalAbsolutePath.getParent());
                    // move temp -> final (overwrite if exists)
                    Files.move(tempFile, finalAbsolutePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // log, but cannot rollback transaction here (already committed)
                    e.printStackTrace();
                }
            }

            @Override
            public void afterCompletion(int status) {
                // if rollback or temp still exists, try to delete temp file to avoid rác
                try {
                    if (Files.exists(tempFile)) {
                        Files.deleteIfExists(tempFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void deleteIfExists(Path path) {
        if (path == null) return;
        try { Files.deleteIfExists(path); } catch (IOException ignored) {}
    }

    @Override
    public Path getProductBaseDir() {
        return productBaseDir;
    }
}
