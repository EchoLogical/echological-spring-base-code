package com.github.echological.app.service.generateshorturl;

import com.github.echological.akint.datasource.entity.UrlShortenerEntity;
import com.github.echological.akint.datasource.repository.UrlShortenerRepository;
import com.github.echological.akint.global.constant.AVRHttpStatus;
import com.github.echological.akint.global.exception.AVRBusinessValidationException;
import com.github.echological.akint.service.UrlShortenerService;
import com.github.echological.akint.service.generateshorturl.config.GenerateShortUrlConfig;
import com.github.echological.akint.service.generateshorturl.model.request.GenerateShortUrlRequest;
import com.github.echological.akint.service.generateshorturl.model.response.GenerateShortUrlResponse;
import com.github.echological.akint.service.generateshorturl.util.GenerateShortUrlUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class GenerateShortUrlService implements
        UrlShortenerService<GenerateShortUrlRequest, GenerateShortUrlResponse> {

    private final GenerateShortUrlConfig generateShortUrlConfig;
    private final UrlShortenerRepository urlShortenerRepository;

    @Override
    public GenerateShortUrlResponse execute(GenerateShortUrlRequest input) throws AVRBusinessValidationException {
        var existingLongUrl = urlShortenerRepository.findExistingLongUrl(input.getLongUrl());

        if(existingLongUrl.isPresent()){
            return GenerateShortUrlResponse.builder()
                    .urlPath(null)
                    .shortUrl(existingLongUrl.get().getShortUrl())
                    .build();
        }

        var shortCode = GenerateShortUrlUtil.generateRandomString(input.getCodeLength());
        var existingShortUrl = urlShortenerRepository.findExistingCode(shortCode);

        int i = 1;
        Integer loop = input.getCodeLoop();

        if(ObjectUtils.isEmpty(loop)){
                loop = generateShortUrlConfig.getDefaultLoop();
        }

        while (existingShortUrl.isPresent() && i < loop) {
            i++;
            shortCode = GenerateShortUrlUtil.generateRandomString(input.getCodeLength());
            existingShortUrl = urlShortenerRepository.findExistingCode(shortCode);
        }

        if(existingShortUrl.isPresent()){
            throw new AVRBusinessValidationException(
                    AVRHttpStatus.FAILED.getCode(),
                    "Failed to generate url.",
                    HttpStatus.BAD_REQUEST,
                    new ArrayList<>()
            );
        }

        urlShortenerRepository.save(UrlShortenerEntity.builder()
                        .longUrl(input.getLongUrl())
                        .shortUrl(shortCode)
                        .createdAt(LocalDateTime.now())
                        .createdBy(null)
                        .updatedAt(LocalDateTime.now())
                .build());

        var shortUrl = UriComponentsBuilder.newInstance()
                .scheme(generateShortUrlConfig.getScheme())
                .host(generateShortUrlConfig.getHost())
                .path(shortCode)
                .build();

        return GenerateShortUrlResponse.builder()
                .urlPath(shortCode)
                .shortUrl(shortUrl.toString())
                .build();
    }
}
