package com.github.echological.app.service.integration.urlredirect;

import com.github.echological.app.datasource.entity.UrlShortenerEntity;
import com.github.echological.app.datasource.repository.UrlShortenerRepository;
import com.github.echological.app.service.TestJpaConfig;
import com.github.echological.app.service.urlredirect.UrlRedirectService;
import com.github.echological.app.service.urlredirect.model.request.UrlRedirectRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(classes = TestJpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlRedirectServiceIT {

    @Autowired
    private UrlRedirectService urlRedirectService;

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @BeforeEach
    void seed() {
        urlShortenerRepository.deleteAll();
        urlShortenerRepository.save(UrlShortenerEntity.builder()
                .longUrl("https://target.example/path")
                .shortUrl("abc123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    @Test
    void shouldResolveRedirectUrl() {
        var req = UrlRedirectRequest.builder().code("abc123").build();
        var res = urlRedirectService.execute(req);
        Assertions.assertEquals("https://target.example/path", res.getRedirectUrl());
    }
}
