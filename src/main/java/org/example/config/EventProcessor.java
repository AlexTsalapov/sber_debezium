package org.example.config;

import org.example.model.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.data.Struct;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventProcessor {
    private SessionFactory sessionFactory;

    public EventProcessor() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/java_debezium");
        configuration.setProperty("hibernate.connection.username", "postgres");
        configuration.setProperty("hibernate.connection.password", "qwerty9551");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");

        configuration.addAnnotatedClass(Event.class);

        sessionFactory = configuration.buildSessionFactory();
    }

    public void processEvent(SourceRecord record) {
        // Извлечение данных из ивента
        Struct value = (Struct) record.value();
        Struct valueStruct = (Struct) value.get("after");
        String name = valueStruct.getString("name");
        Integer years = valueStruct.getInt32("years");
        String city = valueStruct.getString("city");
        Integer times = valueStruct.getInt32("times");
        String gender = valueStruct.getString("gender");
        java.util.Date dateOfBirth = (java.util.Date) valueStruct.get("date_of_birth");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


// Преобразование даты в нужный формат
        String formattedDate = dateFormat.format(dateOfBirth);


        // Создание объекта Event
        Event event = Event.builder()
                .name(name)
                .years(years)
                .city(city)
                .times(times)
                .gender(gender)
                .date_of_birth(dateFormat.get2DigitYearStart())
                .build();

        // Сохранение объекта Event в базе данных
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(event);
        transaction.commit();
        session.close();
    }
}
