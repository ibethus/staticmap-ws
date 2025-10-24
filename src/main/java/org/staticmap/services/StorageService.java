package org.staticmap.services;

import io.minio.*;
import io.minio.messages.Item;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class StorageService {

    private final MinioClient minioClient;

    @ConfigProperty(name = "staticmap.bucketname")
    String bucketName;

    public StorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String storeGpx(InputStream file) {
        verifyBucketExists();
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(UUID.randomUUID().toString())
                            .stream(file, -1, 10485760)
                            .contentType("application/xml")
                            .build());
            return objectWriteResponse.object();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while uploading file to MinIO: " + e.getMessage(), e);
        }
    }

    public InputStream retrieveGpx(String objectName) {
        verifyBucketExists();
        try {
            return minioClient.getObject(
                    io.minio.GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while retrieving file from MinIO: " + e.getMessage(), e);
        }
    }

    public List<Item> listAllGpxFiles() {
        List<Item> items = new ArrayList<>();
        minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .build()).spliterator().forEachRemaining(objects -> {
            try {
                items.add(objects.get());
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while listing files from MinIO: " + e.getMessage(), e);
            }
        });
        return items;
    }

    private void verifyBucketExists() {
        try {
            boolean bucketExists =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                System.out.printf("Bucket %s already exists.%n", bucketName);
            }
        } catch (
                Exception e) {
            throw new RuntimeException("Error occurred while checking if bucket exists: " + e.getMessage(), e);
        }
    }
}
