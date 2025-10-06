package com.github.echological.app.service.generateshorturl.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class GenerateShortUrlConfig {

    @Value("${service.urlshortener.generateshorturl.defaultloop}")
    private Integer defaultLoop;
    @Value("${service.urlshortener.generateshorturl.redirect.scheme}")
    private String scheme;
    @Value("${service.urlshortener.generateshorturl.redirect.host}")
    private String host;

}
