package hr.eestec_zg.file.repository.model.repository;

import hr.eestec_zg.file.repository.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, String> {
}
