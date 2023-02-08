package com.example.excelreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.excelreader.config")
@PropertySource("classpath:config.properties")
public class ExcelReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExcelReaderApplication.class, args);
    }

}
