package hr.eestec_zg.file.repository.config.pojo;

import hr.eestec_zg.file.repository.config.interfaces.StorageConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "eestec.file.repository.storage")
public class StorageConfigObject implements StorageConfig {

    private String path = "/tmp/document_storage";

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "StorageConfigObject{" +
                "path='" + path + '\'' +
                '}';
    }
}
