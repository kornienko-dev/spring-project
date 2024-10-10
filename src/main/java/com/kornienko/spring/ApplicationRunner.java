package com.kornienko.spring;

import com.kornienko.spring.config.DatabaseProperties;
import lombok.Lombok;
import lombok.NonNull;
import lombok.Synchronized;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApplicationRunner {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationRunner.class, args);
        DatabaseProperties databaseProperties = context.getBean("db-com.kornienko.spring.config.DatabaseProperties", DatabaseProperties.class);
        System.out.println(databaseProperties);


    }
}
