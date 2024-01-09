package com.clover.youngchat.global.s3;

import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_FILE;
import static com.clover.youngchat.global.exception.ResultCode.SYSTEM_ERROR;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.clover.youngchat.global.exception.GlobalException;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    private static ObjectMetadata setObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }

    public String uploadFile(MultipartFile multipartFile, FilePath filePath) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String fileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetadata metadata = setObjectMetadata(multipartFile);

        try {
            amazonS3Client.putObject(
                bucketName, filePath.getPath() + fileName, multipartFile.getInputStream(),
                metadata);
        } catch (Exception e) {
            throw new GlobalException(SYSTEM_ERROR);
        }

        return getFileUrl(fileName, filePath);
    }

    public void deleteFile(String fileUrl, FilePath filePath) {
        String fileName = getFileNameFromFileUrl(fileUrl, filePath);
        if (fileName.isBlank()
            || !amazonS3Client.doesObjectExist(bucketName, filePath.getPath() + fileName)) {
            throw new GlobalException(NOT_FOUND_FILE);
        }
        amazonS3Client.deleteObject(bucketName, filePath.getPath() + fileName);
    }

    public String getFileUrl(String fileName, FilePath filePath) {
        return amazonS3Client.getUrl(bucketName, filePath.getPath() + fileName).toString();
    }

    private String getFileNameFromFileUrl(String fileUrl, FilePath filePath) {
        return fileUrl.substring(
            fileUrl.lastIndexOf(filePath.getPath()) + filePath.getPath().length());
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(fileName);
    }

    @Getter
    @RequiredArgsConstructor
    public enum FilePath {
        PROFILE("profile/");

        private final String path;
    }
}
