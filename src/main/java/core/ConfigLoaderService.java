package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoaderService {
    private static ConfigLoaderService instance;
    private final Properties configuration;

    private ConfigLoaderService(String configFilePath) {
        Properties properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configFilePath);

        if (inputStream == null) {
            throw new RuntimeException("Ошибка: файл не найден: " + configFilePath);
        }

        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.configuration = properties;
    }

    public synchronized static ConfigLoaderService getInstance() {
        if (instance == null) {
            instance = new ConfigLoaderService("db/application.properties");
        }
        return instance;
    }

    public String getProperties(String propertyName) {
        return configuration.getProperty(propertyName);
    }
}
