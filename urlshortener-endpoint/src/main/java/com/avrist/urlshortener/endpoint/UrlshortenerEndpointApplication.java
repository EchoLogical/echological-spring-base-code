package com.avrist.urlshortener.endpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
		"com.avrist.urlshortener"
})
public class UrlshortenerEndpointApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(UrlshortenerEndpointApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(UrlshortenerEndpointApplication.class, args);
	}
}
