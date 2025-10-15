package org.staticmap.services;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.InputStream;
import java.util.UUID;

@ApplicationScoped
public class StorageService {

    @Inject
    MinioClient minioClient;

    @ConfigProperty(name = "staticmap.bucketname")
    String bucketName;

    public String storeGpx(InputStream file) {
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(UUID.randomUUID().toString())
                            .stream(file, file.available(), -1)
                            .contentType("application/gpx+xml")
                            .build());
            return objectWriteResponse.object();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while uploading file to MinIO: " + e.getMessage(), e);
        }
    }


}
