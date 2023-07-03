package org.example;

import io.debezium.connector.postgresql.PostgresConnectorConfig;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.config.Configuration;
import org.example.config.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebeziumApp {
    public static void main(String[] args) {
         ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(ch.qos.logback.classic.Level.INFO);
        // Конфигурация Debezium
        Configuration config = Configuration.create()
                .with("name", "my-connector")
                .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
                .with("tasks.max", "1")
                .with("database.hostname", "localhost")
                .with("database.port", "5432")
                .with("database.user", "postgres")
                .with("database.password", "qwerty9551")
                .with("database.dbname", "java_debezium")
                .with("database.server.name", "my-postgres-db")
                .with("database.whitelist", "mydatabase")
                .with("topic.prefix", "my-prefix-")
                .with("value.converter", "org.apache.kafka.connect.json.JsonConverter")
                .with("value.converter.schemas.enable", "false")
                .with("offset.storage", "org.apache.kafka.connect.storage.MemoryOffsetBackingStore")
                .with(PostgresConnectorConfig.PLUGIN_NAME, "pgoutput")
                .build();


        // Создание и запуск EmbeddedEngine Debezium
        EmbeddedEngine engine = EmbeddedEngine.create()
                .using(config)
                .notifying(record -> {
                    // Обработка и сохранение ивента с помощью EventProcessor
                    EventProcessor eventProcessor = new EventProcessor();
                    eventProcessor.processEvent(record);
                })
                .using((success, message, error) -> {
                    if (success) {
                        System.out.println("Завершено успешно");
                    } else {
                        System.err.println("Ошибка: " + message);
                        if (error != null) {
                            error.printStackTrace();
                            System.out.println(111);
                        }
                    }
                })
                .build();

        engine.run();
    }
}
