package mardi.erp_mini.support.aws;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import mardi.erp_mini.exception.FileDeleteFailedException;
import mardi.erp_mini.exception.FileUploadFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class AwsS3Client {
    private final String environments;
    private final String ROOT_PACKAGE_PREFIX;
    private final String bucketName;
    private final S3Operations s3Operations;

    public AwsS3Client(
            @Value("${spring.cloud.aws.s3.bucket}") String bucketName,
            @Value("${application.env}") String environments,
            S3Operations s3Operations
    ) {
        this.bucketName = bucketName;
        this.environments = environments;
        this.s3Operations = s3Operations;
        this.ROOT_PACKAGE_PREFIX = environments + "/uploads/";
    }

    @Nullable
    public String upload(@Nonnull MultipartFile file, @Nullable String prefix) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            return null;
        }

        String s3Key = generateS3Key(file.getOriginalFilename(), prefix);

        try (InputStream is = file.getInputStream()) {
            ObjectMetadata metadata = ObjectMetadata.builder()
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            S3Resource uploadedResource = s3Operations.upload(bucketName, s3Key, is, metadata);
            String imageUrl = uploadedResource.getURL().toString();

            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (S3Exception | SdkClientException e) {
            throw new FileUploadFailedException();
        }
    }

    @Nullable
    public String uploadWithRandomName(@Nonnull MultipartFile file, @Nullable String prefix) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            return null;
        }

        String s3Key = generateS3Key(prefix);

        try (InputStream is = file.getInputStream()) {
            ObjectMetadata metadata = ObjectMetadata.builder()
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            S3Resource uploadedResource = s3Operations.upload(bucketName, s3Key, is, metadata);
            String imageUrl = uploadedResource.getURL().toString();

            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (S3Exception | SdkClientException e) {
            throw new FileUploadFailedException();
        }
    }


    public void deleteByS3Key(String s3Key) {
        if (!StringUtils.hasText(s3Key)) {
            return;
        }

        if (!s3Operations.objectExists(bucketName, s3Key)) {
            return;
        }

        try {
            s3Operations.deleteObject(bucketName, s3Key);
        } catch (S3Exception | SdkClientException e) {
            throw new FileDeleteFailedException();
        }
    }

    public void deleteByUrl(String s3Url) {
        if (!StringUtils.hasText(s3Url)) {
            return;
        }

        try {
            s3Operations.deleteObject(s3Url);
        } catch (S3Exception | SdkClientException e) {
            throw new FileDeleteFailedException();
        }
    }

    @Nonnull
    public String generateS3Key(@Nullable String originalFilename, @Nullable String prefix) {
        String prefixPath = resolvePrefixPath(prefix);
        String filename = resolveFilename(originalFilename);

        String s3Key;
        do {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyymmddhhmmss"));
            s3Key = String.format("%s%s%s_%s", ROOT_PACKAGE_PREFIX, prefixPath, timestamp, filename);
        } while (s3Operations.objectExists(bucketName, s3Key));

        return s3Key;
    }

    @Nonnull
    public String generateS3Key(@Nullable String prefix) {
        String prefixPath = resolvePrefixPath(prefix);
        String filename = resolveFilename(null);

        String s3Key;
        do {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyymmddhhmmss"));
            s3Key = String.format("%s%s%s_%s", ROOT_PACKAGE_PREFIX, prefixPath, timestamp, filename);
        } while (s3Operations.objectExists(bucketName, s3Key));

        return s3Key;
    }

    @Nonnull
    public String generateS3Key(@Nullable String originalFilename, @Nullable String prefix, String rootPrefix, LocalDate date, String fileType) {
        String prefixPath = resolvePrefixPath(prefix);
        String filename = resolveFilename(originalFilename);
        String timestamp = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        return String.format("%s%s%s/%s_%s", rootPrefix, prefixPath, fileType, timestamp, filename);
    }

    private String resolvePrefixPath(@Nullable String prefix) {
        if (!StringUtils.hasText(prefix)) {
            return "";
        }

        char lastCharacter = prefix.charAt(prefix.length() - 1);
        if (lastCharacter != '/') {
            return prefix + "/";
        }
        return prefix;
    }

    private String resolveFilename(@Nullable String originalFilename) {
        if (StringUtils.hasText(originalFilename)) {
            return StringUtils.replace(originalFilename, " ", "_");
        }
        return UUID.randomUUID().toString();
    }

}
