package hr.eestec_zg.file.repository;

import hr.eestec_zg.file.repository.model.FileMetadata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaRepositories
@EntityScan(basePackageClasses = {
        FileMetadata.class
})
@ComponentScan(basePackageClasses = {
        FileRepositoryRoot.class,
})
public class FileRepositoryRoot {

    private void runSpring(String[] args) {
        SpringApplication springApplication = new SpringApplication(FileRepositoryRoot.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }

    public static void main(String[] args) {
        FileRepositoryRoot application = new FileRepositoryRoot();
        application.runSpring(args);
    }
}