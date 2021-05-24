package com.datdev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DBConfig {
    @Bean
    public DriverManagerDataSource dataSource() {
        Map<String, String> envVariables = System.getenv();

        String db = envVariables.get("dbAddress");
        String port = envVariables.get("dbPort");
        String schema = envVariables.get("dbSchema");
        String dbUser = envVariables.get("dbUser");
        String dbPassword = envVariables.get("dbPassword");

        if (db == null || port == null || schema == null || dbUser == null || dbPassword == null) {
            String log = "An environment variable required for the database is missing, make sure dbAddress, dbPort, dbSchema, dbUser, and dbPassword is set";
            System.out.println(log);
            throw new RuntimeException(log);
        }

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.mariadb.jdbc.Driver");

        ds.setUrl("jdbc:mariadb://" + db + ":" + port + "/" + schema);
        ds.setUsername(dbUser);
        ds.setPassword(dbPassword);

        return ds;
    }
}
