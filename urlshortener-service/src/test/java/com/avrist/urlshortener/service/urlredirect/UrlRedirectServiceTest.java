package com.avrist.urlshortener.service.urlredirect;

import com.avrist.urlshortener.global.constant.AVRHttpStatus;
import com.avrist.urlshortener.global.exception.AVRBusinessValidationException;
import com.avrist.urlshortener.datasource.config.UrlShortenerDBConfig;
import com.avrist.urlshortener.datasource.db1.entity.UrlShortenerEntity;
import com.avrist.urlshortener.datasource.db1.repository.UrlShortenerRepository;
import com.avrist.urlshortener.service.urlredirect.model.request.UrlRedirectRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {
        UrlRedirectService.class,
        UrlShortenerRepository.class,
        UrlShortenerDBConfig.class
})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UrlRedirectServiceTest {

    @MockBean
    private UrlShortenerRepository urlShortenerRepository;

    @Autowired
    private UrlRedirectService urlRedirectService;

    @Test
    void testFound(){
        var request = UrlRedirectRequest.builder()
                .code("xxxxx")
                .build();
        var longUrl = "http://xxxx.com/xxxxxxxxxxx";
        given(urlShortenerRepository.findExistingCode(anyString())).willReturn(Optional.of(UrlShortenerEntity.builder()
                        .id(1L)
                        .longUrl(longUrl)
                        .shortUrl("tft")
                .build()));

        var actual = urlRedirectService.execute(request);

        Assertions.assertEquals(longUrl, actual.getRedirectUrl());
    }

    @Test
    void testNotFound(){
        var request = UrlRedirectRequest.builder()
                .code("xxxxx")
                .build();
        given(urlShortenerRepository.findExistingCode(anyString())).willReturn(Optional.empty());

        var actual = Assertions.assertThrows(
                AVRBusinessValidationException.class,
                () -> urlRedirectService.execute(request)
        );

        Assertions.assertEquals(AVRHttpStatus.INVALID_ARGUMENT.getCode(), actual.getResponseCode());
    }
}