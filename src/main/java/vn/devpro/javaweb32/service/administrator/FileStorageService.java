package vn.devpro.javaweb32.service;

import org.springframework.web.multipart.MultipartFile;
import vn.devpro.javaweb32.entity.product.Product;
import vn.devpro.javaweb32.entity.product.ProductImage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileStorageService {

    /**
     * Lưu các file upload tạm thời cho một color.
     * Trả về danh sách ProductImage với url tạm (ví dụ: "temp/{uuid}/{filename}")
     */
    List<ProductImage> saveTempFile(List<MultipartFile> files, String folderPath, String baseImagePrefix) throws IOException;

    /**
     * Sau khi DB lưu thành công product (và cascade đã persist ProductImage),
     * chuyển các file từ temp -> thư mục sản phẩm, cập nhật ProductImage.url và persist lại.
     */
    void moveTempToProductFolder(Product product) throws IOException;

    /**
     * Đăng ký một move (src -> dest) để thực hiện **sau khi transaction commit**.
     * Nếu không có transaction active thì thực hiện move ngay lập tức.
     */
    void registerMoveOnCommit(Path src, Path dest);
}
