package com.avrist.urlshortener.service.generateshorturl;

import com.avrist.urlshortener.global.constant.AVRHttpStatus;
import com.avrist.urlshortener.global.exception.AVRBusinessValidationException;
import com.avrist.urlshortener.datasource.config.UrlShortenerDBConfig;
import com.avrist.urlshortener.datasource.db1.entity.UrlShortenerEntity;
import com.avrist.urlshortener.datasource.db1.repository.UrlShortenerRepository;
import com.avrist.urlshortener.service.generateshorturl.model.request.GenerateShortUrlRequest;
import com.avrist.urlshortener.service.generateshorturl.util.GenerateShortUrlUtil;
import com.avrist.urlshortener.service.generateshorturl.config.GenerateShortUrlConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        GenerateShortUrlService.class,
        UrlShortenerRepository.class,
        UrlShortenerDBConfig.class,
        GenerateShortUrlConfig.class
})
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class GenerateShortUrlServiceTest {

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @MockBean
    private UrlShortenerRepository urlShortenerRepositoryMock;

    @Autowired
    private GenerateShortUrlService generateShortUrlService;

    private static final String LINK = "https://www.bing.com/search?q=hello+world&qs=n&form=QBRE&sp=-1&lq=0&pq=hello+worl&sc=10-10&sk=&cvid=141739F6225440A7AE5E4D89CF11CDC1&ghsh=0&ghacc=0&ghpl=";

    void initData(){
        urlShortenerRepository.deleteAllInBatch();
        urlShortenerRepository.saveAll(Stream.of(
                UrlShortenerEntity.builder()
                        .longUrl(LINK)
                        .shortUrl(GenerateShortUrlUtil.generateRandomString(2))
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).collect(Collectors.toList()));
    }

    @Test
    void testSuccess(){
        initData();
        var actual = generateShortUrlService.execute(GenerateShortUrlRequest.builder()
                        .longUrl("https://en.wikipedia.org/wiki/%22Hello,_World!%22_program")
                        .codeLength(1)
                        .codeLoop(10)
                .build());
        Assertions.assertNotNull(actual.getShortUrl());
    }

    @Test
    void testLongUrlAlreadyExists(){
        initData();
        ReflectionTestUtils.setField(
                generateShortUrlService,
                "urlShortenerRepository",
                urlShortenerRepositoryMock
        );
        when(urlShortenerRepository.findExistingLongUrl(anyString())).thenReturn(Optional.of(UrlShortenerEntity.builder()
                .id(1L)
                .build()));
        var request = GenerateShortUrlRequest.builder()
                .longUrl(LINK)
                .codeLength(1)
                .codeLoop(1)
                .build();
        var actual = Assertions.assertThrows(
                AVRBusinessValidationException.class,
                () -> generateShortUrlService.execute(request)
        );
        Assertions.assertEquals(AVRHttpStatus.INTERRUPTED.getCode(), actual.getResponseCode());
    }

    @Test
    void testMaxLoopAndExhausted(){
        initData();
        ReflectionTestUtils.setField(
                generateShortUrlService,
                "urlShortenerRepository",
                urlShortenerRepositoryMock
        );

        when(urlShortenerRepository.findExistingLongUrl(anyString())).thenReturn(Optional.empty());
        when(urlShortenerRepository.findExistingCode(anyString())).thenReturn(Optional.of(UrlShortenerEntity.builder()
                        .id(1L)
                .build()));

        var request = GenerateShortUrlRequest.builder()
                .longUrl(LINK)
                .codeLength(1)
                .codeLoop(5)
                .build();

        var actual = Assertions.assertThrows(
                AVRBusinessValidationException.class,
                () -> generateShortUrlService.execute(request)
        );

        Assertions.assertEquals(AVRHttpStatus.FAILED.getCode(), actual.getResponseCode());
    }

}