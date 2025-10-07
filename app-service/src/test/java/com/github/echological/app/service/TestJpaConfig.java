package com.github.echological.app.service;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = "com.github.echological.app.datasource.entity")
@EnableJpaRepositories(basePackages = "com.github.echological.app.datasource.repository")
@org.springframework.context.annotation.ComponentScan(basePackages = {
        "com.github.echological.app.service"
})
public class TestJpaConfig {
}
