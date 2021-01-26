package com.datdev.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Configuration
public class DBConfig {
    @Bean
    public DriverManagerDataSource dataSource() {
        String path = "./mysql.properties";
        if (Files.isReadable(Paths.get(path))) {
            Properties mysqlProps = new Properties();
            try {
                mysqlProps.load(new FileInputStream(path));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String db = mysqlProps.getProperty("db", "localhost");
            String port = mysqlProps.getProperty("port", "3306");
            String schema = mysqlProps.getProperty("schema", "battlemap");
            String dbUser = mysqlProps.getProperty("dbUsername");
            String dbPassword = mysqlProps.getProperty("dbPassword");

            DriverManagerDataSource ds = new DriverManagerDataSource();
            ds.setDriverClassName("org.mariadb.jdbc.Driver");

            ds.setUrl("jdbc:mariadb://" + db + ":" + port + "/" + schema);
            ds.setUsername(dbUser);
            ds.setPassword(dbPassword);

            return ds;
        } else {
            throw new RuntimeException("Place your username and passwords in file " + path);
        }
    }
}
