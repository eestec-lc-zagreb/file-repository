package hr.eestec_zg.file.repository.config;

import hr.eestec_zg.file.repository.config.interfaces.StorageConfig;
import hr.eestec_zg.file.repository.config.pojo.StorageConfigObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties( prefix = "eestec.file.repository")
public class ApplicationConfig {

    private StorageConfig storage;

    public ApplicationConfig() {
        this.storage = new StorageConfigObject();
    }

    public StorageConfig getStorage() {
        return storage;
    }

    public void setStorage(StorageConfig storage) {
        this.storage = storage;
    }

}
