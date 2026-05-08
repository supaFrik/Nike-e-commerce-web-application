package vn.demo.nike.features.admin.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.demo.nike.features.admin.product.exception.InvalidFileMappingException;
import vn.demo.nike.features.admin.product.exception.InvalidUploadedImageException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductFileMapService {
    
    Map<String, MultipartFile> buildFileMap(List<MultipartFile> files, List<String> fileClientKeys) {
        List<MultipartFile> safeFiles = files == null ? List.of() : files;
        List<String> safeKeys = fileClientKeys == null ? List.of() : fileClientKeys;

        if (safeFiles.size() != safeKeys.size()) {
            throw new InvalidUploadedImageException("files and fileClientKeys must have same size");
        }

        Map<String, MultipartFile> uploadedFiles = new LinkedHashMap<>();
        for (int i = 0; i < safeFiles.size(); i++) {
            String clientKey = vn.demo.nike.shared.util.StringUtil.normalize(safeKeys.get(i));
            if (clientKey.isBlank()) {
                throw new InvalidFileMappingException("File client key at index " + i + " must not be empty");
            }

            MultipartFile previous = uploadedFiles.put(clientKey, safeFiles.get(i));
            if (previous != null) {
                throw new InvalidFileMappingException("File client key at index " + i + " already exists");
            }
        }
        return uploadedFiles;
    }
}