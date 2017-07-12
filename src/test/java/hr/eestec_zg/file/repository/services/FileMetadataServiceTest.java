package hr.eestec_zg.file.repository.services;

import hr.eestec_zg.file.repository.TestBase;
import hr.eestec_zg.file.repository.controller.pojo.FileMetadataRequest;
import hr.eestec_zg.file.repository.model.FileMetadata;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FileMetadataServiceTest extends TestBase {

    private static final String TEST_CONTENT = "TEST_CONTENT";

    @Autowired
    private FileMetadataService fileMetadataService;

    private static List<Path> files = new LinkedList<>();

    @AfterClass
    public static void cleanUp() {
        files.forEach(file -> deleteQuietly(file.toFile()));
    }

    @Test
    public void testUploadingFile() {
        ByteArrayInputStream is = new ByteArrayInputStream(TEST_CONTENT.getBytes());

        FileMetadataRequest fileMetadataRequest = new FileMetadataRequest("example.txt", "TYPE");

        // testing file upload
        FileMetadata fileMetadata = fileMetadataService.upload(fileMetadataRequest, is);

        files.add(Paths.get(this.applicationConfig.getStorage().getPath() + fileMetadata.getLocation()));

        assertNotNull(fileMetadata);

        assertEquals("example.txt", fileMetadata.getName());
        assertEquals("TYPE", fileMetadata.getType());

        assertNotNull(fileMetadata.getId());
        assertNotNull(fileMetadata.getLocation());
        assertNotNull(fileMetadata.getCreatedAt());

        // testing metadata fetching
        Optional<FileMetadata> storedFileMetadataOptional = fileMetadataService.findFileMetadata(fileMetadata.getId());

        assertTrue(storedFileMetadataOptional.isPresent());

        FileMetadata storedFileMetadata = storedFileMetadataOptional.get();

        assertEquals(fileMetadata, storedFileMetadata);

        // testing file fetching
        Optional<FileInputStream> storedFile = fileMetadataService.findFileData(fileMetadata.getId());

        assertTrue(storedFile.isPresent());
    }

}
