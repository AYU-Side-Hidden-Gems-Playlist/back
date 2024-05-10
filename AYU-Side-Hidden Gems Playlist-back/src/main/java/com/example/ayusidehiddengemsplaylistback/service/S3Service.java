package com.example.ayusidehiddengemsplaylistback.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class S3Service {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public String uploadFile(MultipartFile multipartFile) {
        File file = null;
        try {
            // MultipartFile을 File로 변환
            file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);

            // S3에 파일 업로드
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));

            return fileName;
        } catch (AmazonServiceException e) {
            // AWS S3 업로드 중 발생하는 예외 처리
            e.printStackTrace();
            throw new RuntimeException("Error while uploading the file to S3", e);
        } catch (IOException e) {
            // 파일 변환 중 발생하는 예외 처리
            e.printStackTrace();
            throw new RuntimeException("Error while converting the MultipartFile to File", e);
        } finally {
            // 임시 파일 삭제 (null 체크를 추가하여 NullPointerException 방지)
            if (file != null) file.delete();
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}