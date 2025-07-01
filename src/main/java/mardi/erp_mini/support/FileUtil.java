package mardi.erp_mini.support;

import org.springframework.web.multipart.MultipartFile;

public interface FileUtil {
    String upload(MultipartFile file);
    String uploadWithRandomName(MultipartFile file);

    void delete(String url);

    String replace(String originUrl, MultipartFile file);

    String replaceWithRandomName(String originUrl, MultipartFile file);
}
