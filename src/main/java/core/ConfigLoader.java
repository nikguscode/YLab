package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static ConfigLoader instance;
    private final Properties configuration;

    private ConfigLoader(String configFilePath) {
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

    public synchronized static ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader("db/application.properties");
        }
        return instance;
    }

    public String getProperties(String propertyName) {
        return configuration.getProperty(propertyName);
    }
}
