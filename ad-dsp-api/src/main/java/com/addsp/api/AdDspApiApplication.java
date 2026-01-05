package com.addsp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.addsp")
@EntityScan(basePackages = "com.addsp.domain")
@ConfigurationPropertiesScan
@EnableJpaAuditing
public class AdDspApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdDspApiApplication.class, args);
    }
}
