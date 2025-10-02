package com.github.echological.akint.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.title}")
    private String springdocTitle;

    @Value("${springdoc.version}")
    private String springdocVersion;

    @Value("${springdoc.desc}")
    private String springdocDesc;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title(springdocTitle)
                .version(springdocVersion)
                .description(springdocDesc));
    }
}
