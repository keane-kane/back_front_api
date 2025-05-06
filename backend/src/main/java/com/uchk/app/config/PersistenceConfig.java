package com.uchk.app.config;

import com.uchk.app.util.DbUrlConverter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


public class PersistenceConfig {

    private final Environment env;

    public PersistenceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
        String dbUrl = env.getProperty("DATABASE_URL");
        String username = env.getProperty("PGUSER");
        String password = env.getProperty("PGPASSWORD");
        
        // Convert the URL from postgresql:// format to jdbc:postgresql:// format
        if (dbUrl != null) {
            String jdbcUrl = DbUrlConverter.convertToJdbcUrl(dbUrl);
            System.out.println("Original DB URL: " + dbUrl);
            System.out.println("Converted JDBC URL: " + jdbcUrl);
            dbUrl = jdbcUrl;
        }
        
        System.out.println("Username exists: " + (username != null && !username.isEmpty()));
        System.out.println("Password exists: " + (password != null && !password.isEmpty()));
        
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("org.postgresql.Driver");
        
        return dataSource;
    }
}