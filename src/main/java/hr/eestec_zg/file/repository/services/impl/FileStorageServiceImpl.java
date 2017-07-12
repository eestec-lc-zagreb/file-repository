package hr.eestec_zg.file.repository.services.impl;

import hr.eestec_zg.file.repository.config.ApplicationConfig;
import hr.eestec_zg.file.repository.ex.FileStorageException;
import hr.eestec_zg.file.repository.services.FileStorageService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final ApplicationConfig applicationConfig;

    public FileStorageServiceImpl(ApplicationConfig applicationConfig) {
        Objects.requireNonNull(applicationConfig);

        this.applicationConfig = applicationConfig;
    }

    @PostConstruct
    public void init() throws IOException {
        createStorageDirectory();
    }

    private void createStorageDirectory() throws IOException {

        File storageDirectory = new File(this.applicationConfig.getStorage().getPath());

        if (storageDirectory.exists()) {
            return;
        }

        Files.createDirectories(storageDirectory.toPath());
    }

    @Override
    public Path upload(String uuid, String name, ZonedDateTime createdAt, InputStream inputStream) {

        int year = createdAt.getYear();
        int month = createdAt.getMonthValue();
        int day = createdAt.getDayOfMonth();
        int hour = createdAt.getHour();

        final String fileName = name.toLowerCase() + "-" + uuid;

        String fileDirectoryPath = "" +
                File.separator +
                year +
                File.separator +
                month +
                File.separator +
                day +
                File.separator +
                hour;

        Path completeFilePath = Paths.get(applicationConfig.getStorage().getPath(), fileDirectoryPath);
        Path completePath = Paths.get(applicationConfig.getStorage().getPath(), fileDirectoryPath, fileName);

        try {
            Files.createDirectories(completeFilePath);
        } catch (IOException e) {
            throw new FileStorageException("Cannot create directory; " + completeFilePath, e);
        }

        try {
            FileUtils.copyInputStreamToFile(inputStream, completePath.toFile());
        } catch (IOException e) {
            throw new FileStorageException("Cannot copy file to directory: " + completePath, e);
        }

        return Paths.get(fileDirectoryPath, fileName);
    }

    @Override
    public Optional<FileInputStream> getStream(String fileLocation) {

        Path filePath = Paths.get(this.applicationConfig.getStorage().getPath(), fileLocation);

        if (!filePath.toFile().exists()) {
            return Optional.empty();
        }

        try {
            return Optional.of(new FileInputStream(filePath.toFile()));
        } catch (FileNotFoundException e) {
            logger.warn("cannot find file at: " + filePath, e);

            return Optional.empty();
        }
    }
}
