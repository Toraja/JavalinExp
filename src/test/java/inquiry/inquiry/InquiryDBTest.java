package inquiry.inquiry;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import inquiry.helper.DockerfileMySQLContainer;
import inquiry.util.repository.Option;

@Testcontainers
public class InquiryDBTest {
    private InquiryDB db;
    @Container
    private DockerfileMySQLContainer<?> container;

    @SuppressWarnings({ "resource" })
    public InquiryDBTest() throws Exception {
        // original way using public image
        // container = new MySQLContainer<>("mysql:latest")
        // .withDatabaseName("inquiry").withUsername("root")
        // .withPassword("").withInitScript("sql/ddl_mysql.sql");

        ImageFromDockerfile img = new ImageFromDockerfile()
                .withFileFromPath(".", Paths.get("./"));
        // using `withDockerfile()`, it ends up as NullPointerException if not
        // absolute path. However, With absolute path, it hangs and never ends.
        // .withDockerfile(Paths.get("db.Dockerfile").toAbsolutePath());

        // 'withExposedPorts' is required or `container.start()` will fail
        container = new DockerfileMySQLContainer<>(img)
                .withDatabaseName("inquiry").withUsername("root")
                .withPassword("").withExposedPorts(3306);
    }

    @BeforeEach
    public void setup() {
        container.start();
        // must be after container started or will be error
        container.followOutput(getLogConsumer());
        db = new InquiryDB(container.getDataSource());
    }

    @AfterEach
    public void teardown() {
        container.close();
    }

    private Consumer<OutputFrame> getLogConsumer() {
        Logger logger = LoggerFactory.getLogger(InquiryDBTest.class);
        return new Slf4jLogConsumer(logger);
    }

    @Test
    public void testSaveFindByIDFindAll() throws Exception {
        Inquiry[] inqs = new Inquiry[] {
                new Inquiry("John", "john@email.com", "Hi, I'm John",
                        "Here is my situation."),
                new Inquiry("Eric", "eric@email.com", "Hi, I'm Eric",
                        "Here is my situation."),
                new Inquiry("Pete", "pete@email.com", "Hi, I'm Pete",
                        "Here is my situation.") };
        long n = 0;
        for (Inquiry inq : inqs) {
            db.save(inq);
            inq.id = ++n;
        }

        Inquiry inqExpected = inqs[1];
        Inquiry inqActual = db.findByID(2);
        assertEquals(inqExpected, inqActual, "findByID");

        Inquiry[] inqsActual = db.findAll(new Option(0, 0));
        for (Inquiry inq : inqs) {
            inq.detail = null;
        }
        assertArrayEquals(inqs, inqsActual, "findAll");
    }

    @Test
    public void testSaveGetTotalCount() throws Exception {
        Inquiry[] inqs = new Inquiry[] {
                new Inquiry("aaa", "aaa@email.com", "aaa", "aaa"),
                new Inquiry("bbb", "bbb@email.com", "bbb", "bbb"),
                new Inquiry("ccc", "ccc@email.com", "ccc", "ccc"),
                new Inquiry("ddd", "ddd@email.com", "ddd", "ddd"),
                new Inquiry("eee", "eee@email.com", "eee", "eee") };
        long n = 0;
        for (Inquiry inq : inqs) {
            db.save(inq);
            inq.id = ++n;
        }

        int count = db.getTotalCount();
        assertEquals(inqs.length, count);
    }
}
