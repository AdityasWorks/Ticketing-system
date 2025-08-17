package com.tickets.ticketingsystem.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.tickets.ticketingsystem.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AzureBlobStorageServiceImpl implements FileStorageService {

    @Value("${azure.blob.connection-string}")
    private String connectionString;

    private final String containerName = "attachments";

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString).buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            // Generate a unique name for the blob to avoid overwrites
            String blobName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            BlobClient blobClient = containerClient.getBlobClient(blobName);

            blobClient.upload(file.getInputStream(), file.getSize(), true);

            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Azure Blob Storage", e);
        }
    }
}