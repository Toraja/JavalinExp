package inquiry.util.infra;

import java.time.Duration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class SQLite extends AbstractDataSource {

    public SQLite() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:inquiry.db");
        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(Duration.ofSeconds(30).toMillis());
        config.setIdleTimeout(Duration.ofSeconds(180).toMillis());
        ds = new HikariDataSource(config);
    }
}
