package hr.eestec_zg.file.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.eestec_zg.file.repository.config.ApplicationConfig;
import hr.eestec_zg.file.repository.model.repository.FileMetadataRepository;
import hr.eestec_zg.file.repository.test_utils.EESTECTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringJUnit4ClassRunner.class)
@EESTECTest
@WebAppConfiguration
public abstract class TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TestBase.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    protected ApplicationConfig applicationConfig;

    private MockMvc mockMvc;

    @Before
    public void beforeTestBase() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.fileMetadataRepository.deleteAll();
    }

    protected String contentOf(String fileName) throws URISyntaxException, IOException {
        //noinspection ConstantConditions
        return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                .getResource(fileName)
                .toURI())));
    }

    protected <K> K readJson(String content, Class<K> clazz) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
