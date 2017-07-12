package hr.eestec_zg.file.repository.services;

import hr.eestec_zg.file.repository.controller.pojo.FileMetadataRequest;
import hr.eestec_zg.file.repository.model.FileMetadata;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

public interface FileMetadataService {

    FileMetadata upload(FileMetadataRequest metadata, InputStream inputStream);

    Optional<FileInputStream> findFileData(String uuid);

    Optional<FileMetadata> findFileMetadata(String uuid);
}
