package com.github.echological.akint.service.integration.generateshorturl;

import com.github.echological.akint.datasource.repository.UrlShortenerRepository;
import com.github.echological.akint.service.TestJpaConfig;
import com.github.echological.akint.service.generateshorturl.GenerateShortUrlService;
import com.github.echological.akint.service.generateshorturl.model.request.GenerateShortUrlRequest;
import com.github.echological.akint.service.generateshorturl.model.response.GenerateShortUrlResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestJpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenerateShortUrlServiceIT {

    @Autowired
    private GenerateShortUrlService generateShortUrlService;

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @Test
    void shouldGenerateAndPersistShortUrl() {
        // Arrange
        var input = GenerateShortUrlRequest.builder()
                .longUrl("https://integration.example/page")
                .codeLength(6)
                .codeLoop(10)
                .build();

        // Act
        GenerateShortUrlResponse result = generateShortUrlService.execute(input);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getUrlPath());
        Assertions.assertNotNull(result.getShortUrl());
        Assertions.assertTrue(result.getShortUrl().startsWith("http://localhost/"));

        var saved = urlShortenerRepository.findExistingCode(result.getUrlPath());
        Assertions.assertTrue(saved.isPresent(), "Saved entity should be present in DB");
        Assertions.assertEquals("https://integration.example/page", saved.get().getLongUrl());
    }
}
