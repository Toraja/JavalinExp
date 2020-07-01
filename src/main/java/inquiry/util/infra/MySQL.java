package inquiry.util.infra;

import java.time.Duration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MySQL extends AbstractDataSource {

    public MySQL(String host, int port, String database) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(
                String.format("jdbc:mysql://%s:%d/%s", host, port, database));
        config.setUsername("root");
        config.setPassword("");
        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(Duration.ofSeconds(30).toMillis());
        config.setIdleTimeout(Duration.ofSeconds(180).toMillis());
        ds = new HikariDataSource(config);
    }
}
