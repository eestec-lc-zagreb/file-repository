package hr.eestec_zg.file.repository.controller;

import hr.eestec_zg.file.repository.controller.pojo.FileMetadataRequest;
import hr.eestec_zg.file.repository.controller.resources.FileMetadataResource;
import hr.eestec_zg.file.repository.model.FileMetadata;
import hr.eestec_zg.file.repository.services.FileMetadataService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

@Controller
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileMetadataService fileMetadataService;

    public FileController(FileMetadataService fileMetadataService) {
        Objects.requireNonNull(fileMetadataService);

        this.fileMetadataService = fileMetadataService;
    }

    @RequestMapping(
            value = "/api/v1/files",
            method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity<FileMetadataResource> upload(
            @RequestPart(value = "metadata") @Valid FileMetadataRequest metadata,
            @RequestPart(value = "file") MultipartFile file)
            throws IOException {

        logger.info("received file upload request with metadata: {}", metadata);

        InputStream fileInputStream = file.getInputStream();

        FileMetadata fileMetadata = fileMetadataService.upload(metadata, fileInputStream);
        FileMetadataResource fileMetadataResource = new FileMetadataResource(fileMetadata);

        return ResponseEntity.ok(fileMetadataResource);
    }

    @RequestMapping(
            value = "/api/v1/files/metadata/{uuid}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<FileMetadataResource> getFileMetadata(@PathVariable(name = "uuid") String uuid) {
        Optional<FileMetadata> fileMetadataOptional = fileMetadataService.findFileMetadata(uuid);

        return fileMetadataOptional
                .map(fileMetadata -> ResponseEntity.ok(new FileMetadataResource(fileMetadata)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(
            value = "/api/v1/files/{uuid}",
            method = RequestMethod.GET
    )
    public void getFile(@PathVariable(name = "uuid") String uuid, HttpServletResponse response) {
        Optional<FileInputStream> isOptional = fileMetadataService.findFileData(uuid);

        if (!isOptional.isPresent()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());

            return;
        }

        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setStatus(HttpStatus.OK.value());

            IOUtils.copy(isOptional.get(), response.getOutputStream());
        } catch (IOException e) {
            logger.warn("Cannot send file", e);

            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
