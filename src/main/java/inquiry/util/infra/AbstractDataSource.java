package inquiry.util.infra;

import javax.sql.DataSource;

public abstract class AbstractDataSource {
    protected static DataSource ds;

    public DataSource getDataSource() {
        return ds;
    };
}
