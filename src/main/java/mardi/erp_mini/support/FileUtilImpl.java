package mardi.erp_mini.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mardi.erp_mini.support.aws.AwsS3Client;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtilImpl implements FileUtil {

    private static final Pattern S3_HTTPS_URL_PATTERN = Pattern.compile(
            "https://([a-zA-Z0-9.-]+)\\.s3\\.([a-zA-Z0-9-]+)\\.amazonaws\\.com/(.*)|" +
                    "https://s3\\.([a-zA-Z0-9-]+)\\.amazonaws\\.com/([a-zA-Z0-9.-]+)/(.*)"
    );

    private final AwsS3Client awsS3Client;

    @Override
    public String upload(MultipartFile file) {
        String imageUrl = awsS3Client.upload(file, null);
        log.info("[s3 image upload] imageUrl:{}", imageUrl);

        return imageUrl;
    }

    @Override
    public String uploadWithRandomName(MultipartFile file) {
        String imageUrl = awsS3Client.uploadWithRandomName(file, null);
        log.info("[s3 image upload] imageUrl:{}", imageUrl);

        return imageUrl;
    }

    @Override
    public void delete(String url) {
        if (!StringUtils.hasText(url)) return;

        String s3Url = isHttpsUrl(url) ? convertToS3Url(url) : url;
        awsS3Client.deleteByUrl(s3Url);

        log.info("[s3 image delete] imageUrl:{}", s3Url);
    }

    @Override
    public String replace(String originUrl, MultipartFile file) {
        delete(originUrl);
        return upload(file);
    }

    @Override
    public String replaceWithRandomName(String originUrl, MultipartFile file) {
        delete(originUrl);
        return uploadWithRandomName(file);
    }


    public boolean isHttpsUrl(String url) {
        return S3_HTTPS_URL_PATTERN.matcher(url).matches();
    }

    private String convertToS3Url(String httpsUrl) {
        Matcher matcher = S3_HTTPS_URL_PATTERN.matcher(httpsUrl);

        if (matcher.matches()) {
            String bucketName;
            String objectKey;

            // bucket-name.s3.region.amazonaws.com/object-key 형식
            if (matcher.group(1) != null) {
                bucketName = matcher.group(1);
                objectKey = matcher.group(3);
            }
            // s3.region.amazonaws.com/bucket-name/object-key 형식
            else {
                bucketName = matcher.group(5);
                objectKey = matcher.group(6);
            }

            return "s3://" + bucketName + "/" + objectKey;
        }

        return httpsUrl;
    }
}
