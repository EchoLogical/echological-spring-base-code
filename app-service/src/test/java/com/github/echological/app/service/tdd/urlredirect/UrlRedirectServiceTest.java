package com.github.echological.app.service.tdd.urlredirect;

import com.github.echological.app.global.constant.AVRHttpStatus;
import com.github.echological.app.global.exception.AVRContentBusinessValidationException;
import com.github.echological.app.datasource.entity.UrlShortenerEntity;
import com.github.echological.app.datasource.repository.UrlShortenerRepository;
import com.github.echological.app.service.urlredirect.UrlRedirectService;
import com.github.echological.app.service.urlredirect.model.request.UrlRedirectRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UrlRedirectServiceTest {

    @Mock
    private UrlShortenerRepository urlShortenerRepository;

    @InjectMocks
    private UrlRedirectService urlRedirectService;

    @Test
    void testFound() {
        // Arrange
        var code = "abc123";
        var entity = UrlShortenerEntity.builder()
                .id(100L)
                .longUrl("https://target.example/path?x=1")
                .shortUrl(code)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        given(urlShortenerRepository.findExistingCode(code)).willReturn(Optional.of(entity));

        var input = UrlRedirectRequest.builder().code(code).build();

        // Act
        var actual = urlRedirectService.execute(input);

        // Assert
        Assertions.assertEquals(entity.getLongUrl(), actual.getRedirectUrl());
        verify(urlShortenerRepository).findExistingCode(code);
    }

    @Test
    void testNotFound_ThrowsBusinessValidation() {
        // Arrange
        var code = "notfound";
        given(urlShortenerRepository.findExistingCode(anyString())).willReturn(Optional.empty());

        var input = UrlRedirectRequest.builder().code(code).build();

        // Act + Assert
        var ex = Assertions.assertThrows(AVRContentBusinessValidationException.class, () ->
                urlRedirectService.execute(input)
        );
        Assertions.assertEquals(AVRHttpStatus.INVALID_ARGUMENT.getCode(), ex.getResponseCode());
        Assertions.assertEquals("Invalid url", ex.getResponseMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        Assertions.assertTrue(ex.getError().isEmpty());
        verify(urlShortenerRepository).findExistingCode(code);
    }
}