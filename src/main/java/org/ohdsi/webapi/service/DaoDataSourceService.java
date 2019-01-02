package org.ohdsi.webapi.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DaoDataSourceService {
    private final ConcurrentHashMap<String, DataSource> dataSources = new ConcurrentHashMap<>();

    public DataSource dataSource(String jdbcUrl, String user, String password) {
        return dataSources.computeIfAbsent(jdbcUrl, url -> {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setMaximumPoolSize(50);
            hikariConfig.setDriverClassName("org.postgresql.Driver");
            Optional.ofNullable(user).ifPresent(hikariConfig::setUsername);
            Optional.ofNullable(password).ifPresent(hikariConfig::setPassword);
            return new HikariDataSource(hikariConfig);
        });
    }
}
