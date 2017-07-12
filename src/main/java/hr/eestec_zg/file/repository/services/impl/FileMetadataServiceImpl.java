package hr.eestec_zg.file.repository.services.impl;

import hr.eestec_zg.file.repository.controller.pojo.FileMetadataRequest;
import hr.eestec_zg.file.repository.model.FileMetadata;
import hr.eestec_zg.file.repository.model.repository.FileMetadataRepository;
import hr.eestec_zg.file.repository.services.FileMetadataService;
import hr.eestec_zg.file.repository.services.FileStorageService;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileMetadataServiceImpl implements FileMetadataService {

    private final FileStorageService fileStorageService;

    private final FileMetadataRepository fileMetadataRepository;

    public FileMetadataServiceImpl(
            FileStorageService fileStorageService,
            FileMetadataRepository fileMetadataRepository) {

        Objects.requireNonNull(fileStorageService);
        Objects.requireNonNull(fileMetadataRepository);

        this.fileStorageService = fileStorageService;
        this.fileMetadataRepository = fileMetadataRepository;
    }

    @Override
    public FileMetadata upload(FileMetadataRequest metadata, InputStream inputStream) {

        String fileId = generateUuid();

        ZonedDateTime createdAt = ZonedDateTime.now();

        Path path = fileStorageService.upload(fileId, metadata.getName(), createdAt, inputStream);

        FileMetadata document = new FileMetadata()
                .id(fileId)
                .name(metadata.getName())
                .type(metadata.getFileType())
                .location(path.toString())
                .createdAt(createdAt);

        fileMetadataRepository.save(document);

        return document;
    }

    private String generateUuid() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Optional<FileInputStream> findFileData(String uuid) {
        Optional<FileMetadata> fileMetadataOptional = Optional.of(
                fileMetadataRepository.findOne(uuid));

        return fileMetadataOptional.flatMap(fileMetadata ->
                fileStorageService.getStream(fileMetadata.getLocation())
        );
    }

    @Override
    public Optional<FileMetadata> findFileMetadata(String uuid) {
        return Optional.of(fileMetadataRepository.findOne(uuid));
    }
}