package com.github.echological.akint.service.generateshorturl;

import com.github.echological.akint.datasource.entity.UrlShortenerEntity;
import com.github.echological.akint.datasource.repository.UrlShortenerRepository;
import com.github.echological.akint.global.constant.AVRHttpStatus;
import com.github.echological.akint.global.exception.AVRBusinessValidationException;
import com.github.echological.akint.service.generateshorturl.config.GenerateShortUrlConfig;
import com.github.echological.akint.service.generateshorturl.model.request.GenerateShortUrlRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class GenerateShortUrlServiceTest {

    @Mock
    private GenerateShortUrlConfig generateShortUrlConfig;

    @Mock
    private UrlShortenerRepository urlShortenerRepository;

    @InjectMocks
    private GenerateShortUrlService generateShortUrlService;

    @Test
    void testReturnExistingLongUrl() {
        // Arrange
        var input = GenerateShortUrlRequest.builder()
                .longUrl("https://already.exists")
                .codeLength(5)
                .codeLoop(3)
                .build();

        when(urlShortenerRepository.findExistingLongUrl(input.getLongUrl()))
                .thenReturn(Optional.of(
                        UrlShortenerEntity.builder()
                                .id(1L)
                                .longUrl(input.getLongUrl())
                                .shortUrl("abcde")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                ));

        // Act
        var actual = generateShortUrlService.execute(input);

        // Assert
        Assertions.assertNull(actual.getUrlPath());
        Assertions.assertEquals("abcde", actual.getShortUrl());
        verify(urlShortenerRepository, never()).findExistingCode(anyString());
        verify(urlShortenerRepository, never()).save(any());
    }

    @Test
    void testLoopingWithDefaultLoop_Success() {
        // Arrange
        var input = GenerateShortUrlRequest.builder()
                .longUrl("https://google.com/with-collision")
                .codeLength(1)
                .codeLoop(null) // force default loop path
                .build();

        when(generateShortUrlConfig.getDefaultLoop()).thenReturn(2);
        when(generateShortUrlConfig.getScheme()).thenReturn("http");
        when(generateShortUrlConfig.getHost()).thenReturn("localhost");

        when(urlShortenerRepository.findExistingLongUrl(input.getLongUrl())).thenReturn(Optional.empty());
        when(urlShortenerRepository.findExistingCode(any()))
                .thenReturn(Optional.of(UrlShortenerEntity.builder().id(1L).shortUrl("x").build()))
                .thenReturn(Optional.empty());

        when(urlShortenerRepository.save(any())).thenReturn(UrlShortenerEntity.builder()
                .id(10L)
                .longUrl(input.getLongUrl())
                .shortUrl("y")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());

        // Act
        var actual = generateShortUrlService.execute(input);

        // Assert
        Assertions.assertNotNull(actual.getUrlPath());
        Assertions.assertEquals(1, actual.getUrlPath().length());
        Assertions.assertTrue(actual.getShortUrl().startsWith("http://localhost/"));
        verify(urlShortenerRepository, times(1)).save(any());
    }

    @Test
    void testFailWhenCollisionPersists_Throws() {
        // Arrange
        var input = GenerateShortUrlRequest.builder()
                .longUrl("https://example.com/fail")
                .codeLength(1)
                .codeLoop(1)
                .build();

        when(urlShortenerRepository.findExistingLongUrl(input.getLongUrl())).thenReturn(Optional.empty());
        when(urlShortenerRepository.findExistingCode(any())).thenReturn(Optional.of(
                UrlShortenerEntity.builder().id(2L).shortUrl("z").build()
        ));

        // Act + Assert
        var ex = Assertions.assertThrows(AVRBusinessValidationException.class, () ->
                generateShortUrlService.execute(input)
        );
        Assertions.assertEquals(AVRHttpStatus.FAILED.getCode(), ex.getResponseCode());

        verify(urlShortenerRepository, never()).save(any());
    }
}
