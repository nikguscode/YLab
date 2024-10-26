package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс, реализующий загрузку информации из конфигурацинного файла
 */
public class ConfigLoaderService {
    /**
     * Текущий экзмепляр класса
     */
    private static ConfigLoaderService instance;

    /**
     * Данные из конфигурационного файла
     */
    private final Properties configuration;

    /**
     * Конструктор, выполняющий загрузку информации из конфигурационного файла
     * @param configFilePath путь до конфигурационного файла
     */
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

    /**
     * Возвращает экземпляр сервиса
     * @return экзмепляр сервиса
     */
    public synchronized static ConfigLoaderService getInstance() {
        if (instance == null) {
            instance = new ConfigLoaderService("db/application.properties");
        }
        return instance;
    }

    /**
     * Возвращает извлечённое из файла свойство
     * @param propertyName название свойства
     * @return свойство в строковом представлении
     */
    public String getProperties(String propertyName) {
        return configuration.getProperty(propertyName);
    }
}