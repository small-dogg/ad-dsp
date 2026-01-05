package com.addsp.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration.
 * Enables JPA repositories in infrastructure layer.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.addsp.api.infrastructure.repository")
public class JpaConfig {
}
