package hr.eestec_zg.file.repository.services;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Optional;

public interface FileStorageService {

    Path upload(String uuid, String name, ZonedDateTime createdAt, InputStream inputStream);

    Optional<FileInputStream> getStream(String documentLocation);
}
