package com.clover.youngchat.global.s3;

import static com.clover.youngchat.domain.user.constant.UserConstant.JPEG;
import static com.clover.youngchat.domain.user.constant.UserConstant.PNG;
import static com.clover.youngchat.global.exception.ResultCode.INVALID_PROFILE_IMAGE_TYPE;
import static com.clover.youngchat.global.exception.ResultCode.NOT_FOUND_FILE;
import static com.clover.youngchat.global.exception.ResultCode.SYSTEM_ERROR;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.clover.youngchat.global.exception.GlobalException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
        if (!Objects.equals(multipartFile.getContentType(), PNG) &&
            !Objects.equals(multipartFile.getContentType(), JPEG)) {
            throw new GlobalException(INVALID_PROFILE_IMAGE_TYPE);
        }

        String fileName = createFileName(
            Objects.requireNonNull(multipartFile.getOriginalFilename()));

        MultipartFile newMultipartFile = resizer(fileName, multipartFile, 400);

        ObjectMetadata metadata = setObjectMetadata(newMultipartFile);

        try {
            amazonS3Client.putObject(
                bucketName, filePath.getPath() + fileName, newMultipartFile.getInputStream(),
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

    @Transactional
    public MultipartFile resizer(String fileName, MultipartFile originalImage,
        int width) {
        try {
            BufferedImage image = ImageIO.read(
                originalImage.getInputStream());
            String fileFormat = Objects.requireNonNull(originalImage.getContentType())
                .substring(originalImage.getContentType().lastIndexOf("/") + 1);

            int originWidth = image.getWidth();
            int originHeight = image.getHeight();

            if (originWidth < width) {
                return originalImage;
            }

            MarvinImage imageMarvin = new MarvinImage(image);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", width);
            scale.setAttribute("newHeight", width * originHeight / originWidth);
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imageNoAlpha, fileFormat, baos);
            baos.flush();

            return new CustomMultipartFile(fileName, fileFormat, originalImage.getContentType(),
                baos.toByteArray());

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 줄이는데 실패했습니다.");
        }
    }

    private String getFileNameFromFileUrl(String fileUrl, FilePath filePath) {
        return fileUrl.substring(
            fileUrl.lastIndexOf(filePath.getPath()) + filePath.getPath().length());
    }

    private String createFileName(String fileName) {
        String substringFile = fileName.substring(fileName.lastIndexOf('.'));
        return UUID.randomUUID().toString().concat(substringFile);
    }

    @Getter
    @RequiredArgsConstructor
    public enum FilePath {
        PROFILE("profile/");

        private final String path;
    }
}
