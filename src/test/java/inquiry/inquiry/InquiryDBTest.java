package inquiry.inquiry;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import inquiry.util.repository.Option;

@Testcontainers
public class InquiryDBTest {
    private InquiryDB db;
    @Container
    private MySQLContainer<?> container;

    @SuppressWarnings({ "resource" })
    public InquiryDBTest() throws Exception {
        container = new MySQLContainer<>("mysql:latest")
                .withDatabaseName("inquiry").withUsername("root")
                .withPassword("").withInitScript("sql/ddl_mysql.sql");
    }

    @BeforeEach
    public void setup() {
        container.start();
        db = new InquiryDB(getDataSource(container));
    }

    @AfterEach
    public void teardown() {
        container.close();
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

    private DataSource getDataSource(JdbcDatabaseContainer<?> container) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(container.getJdbcUrl());
        config.setUsername(container.getUsername());
        config.setPassword(container.getPassword());
        config.setDriverClassName(container.getDriverClassName());
        return new HikariDataSource(config);
    }
}
