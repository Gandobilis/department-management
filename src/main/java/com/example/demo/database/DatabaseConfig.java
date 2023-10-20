package com.example.demo.database;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String CONFIG_FILE_PATH = "src/main/java/com/example/demo/config/database.properties";
    @Getter
    private static final Properties properties;

    static {
        properties = loadProperties();
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            props.load(input);
        } catch (IOException e) {
            System.out.println("Filed to load database properties.");
        }
        return props;
    }
}
