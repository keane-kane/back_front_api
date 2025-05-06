package com.uchk.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class DatabaseConnectionCheck implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionCheck.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Database URL: {}", env.getProperty("DATABASE_URL"));
        logger.info("PGUSER exists: {}", env.getProperty("PGUSER") != null);
        logger.info("PGPASSWORD exists: {}", env.getProperty("PGPASSWORD") != null);
        
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Database connection successful");
            logger.info("Database product name: {}", connection.getMetaData().getDatabaseProductName());
            logger.info("Database product version: {}", connection.getMetaData().getDatabaseProductVersion());
            
            try (Statement statement = connection.createStatement()) {
                statement.execute("SELECT 1");
                logger.info("Test query executed successfully");
            }
        } catch (Exception e) {
            logger.error("Failed to connect to the database", e);
            throw e;
        }
    }
}