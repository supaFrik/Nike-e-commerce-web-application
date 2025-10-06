package vn.devpro.javaweb32.service.administrator;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    /**
     * Lưu 1 file vào thư mục tạm và trả về Path tới file temp.
     */
    Path saveTempFile(MultipartFile file) throws IOException;

    /**
     * Đăng ký di chuyển tempFile -> finalAbsolutePath vào sau khi transaction commit.
     * Nếu rollback sẽ auto xóa tempFile.
     */
    void registerMoveOnCommit(Path tempFile, Path finalAbsolutePath);

    /**
     * Xóa temp file (dùng khi cần).
     */
    void deleteIfExists(Path path);

    /**
     * Trả về thư mục gốc cho ảnh sản phẩm (ABSOLUTE)
     */
    Path getProductBaseDir();
}
