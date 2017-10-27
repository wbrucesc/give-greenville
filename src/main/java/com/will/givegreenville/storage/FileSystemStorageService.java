package com.will.givegreenville.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


    @Service
    public class FileSystemStorageService implements StorageService {

        private final Path rootLocation;

        @Autowired
        public FileSystemStorageService(StorageProperties properties) {
            this.rootLocation = Paths.get(properties.getLocation());
        }

        @Override
        public void store(MultipartFile file) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if (file.isEmpty()) {
                    throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
                }
//                Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));

                BasicAWSCredentials awsCreds = new BasicAWSCredentials(System.getenv("awskey"), System.getenv("awssecret"));
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                        .withRegion("us-east-1")
                        .build();

                String bucketName = "ggimageuploads";

                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentLength(file.getSize());


                 PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file.getInputStream(), meta)
                        .withCannedAcl(CannedAccessControlList.PublicRead);

                 s3Client.putObject(request);

            } catch (IOException e) {
                throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
            }
        }

        @Override
        public Stream<Path> loadAll() {
            try {
                return Files.walk(this.rootLocation, 1)
                        .filter(path -> !path.equals(this.rootLocation))
                        .map(path -> this.rootLocation.relativize(path));
            } catch (IOException e) {
                throw new StorageException("Failed to read stored files", e);
            }

        }

        @Override
        public Path load(String filename) {
            return rootLocation.resolve(filename);
        }

        @Override
        public Resource loadAsResource(String filename) {
            try {
                Path file = load(filename);
                Resource resource = new UrlResource(file.toUri());
                if(resource.exists() || resource.isReadable()) {
                    return resource;
                }
                else {
                    throw new StorageFileNotFoundException("Could not read file: " + filename);

                }
            } catch (MalformedURLException e) {
                throw new StorageFileNotFoundException("Could not read file: " + filename, e);
            }
        }

        @Override
        public void deleteAll() {
            FileSystemUtils.deleteRecursively(rootLocation.toFile());
        }

        @Override
        public void init() {
            try {
                Files.createDirectory(rootLocation);
            } catch (IOException e) {
                throw new StorageException("Could not initialize storage", e);
            }
        }
    }

