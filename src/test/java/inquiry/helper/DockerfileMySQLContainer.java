package inquiry.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import org.testcontainers.containers.JdbcDatabaseContainer;

import org.testcontainers.images.builder.ImageFromDockerfile;

/**
 * Manually implemented version of MySQL container which builds image from
 * dockerfile
 */
public class DockerfileMySQLContainer<SELF extends DockerfileMySQLContainer<SELF>>
        extends JdbcDatabaseContainer<SELF> {
    String username;
    String password;
    String databaseName;

    public DockerfileMySQLContainer(ImageFromDockerfile img) {
        super(img);
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String getDriverClassName() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return "com.mysql.cj.jdbc.Driver";
        } catch (ClassNotFoundException e) {
            return "com.mysql.jdbc.Driver";
        }
    }

    @Override
    public String getJdbcUrl() {
        return String.format("jdbc:mysql://%s:%d/%s", getHost(),
                getFirstMappedPort(), getDatabaseName());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getTestQueryString() {
        return "SELECT 1";
    }

    @Override
    public SELF withDatabaseName(final String databaseName) {
        this.databaseName = databaseName;
        return self();
    }

    @Override
    public SELF withUsername(final String username) {
        this.username = username;
        return self();
    }

    @Override
    public SELF withPassword(final String password) {
        this.password = password;
        return self();
    }

    public DataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(getUsername());
        config.setPassword(getPassword());
        config.setDriverClassName(getDriverClassName());

        return new HikariDataSource(config);
    }
}
