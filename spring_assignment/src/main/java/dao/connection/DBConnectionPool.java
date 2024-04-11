package dao.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.config.Config;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
@Log4j2
public class DBConnectionPool {

    private final Config config;

    private final DataSource hikariDataSource;

    @Inject
    public DBConnectionPool(Config config) {
        this.config = config;
        hikariDataSource = getHikariPool();
    }

    public DataSource getHikariPool() {

        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(config.getProperty("urlDB"));
        hikariConfig.setUsername(config.getProperty("user_name"));
        hikariConfig.setPassword(config.getProperty("password"));
        hikariConfig.setDriverClassName(config.getProperty("driver"));
        hikariConfig.setMaximumPoolSize(4);

        hikariConfig.addDataSourceProperty("cachePrepStmts", true);
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", 250);
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

        return new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() {
        Connection c = null;
        try {
            c = hikariDataSource.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return c;
    }

    public DataSource getDataSource() {
        return hikariDataSource;
    }

    public void closeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void closePool() {
        ((HikariDataSource) hikariDataSource).close();
    }


}


