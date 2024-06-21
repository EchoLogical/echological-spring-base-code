package com.avrist.urlshortener.service.generateshorturl;

import com.avrist.urlshortener.global.constant.AVRHttpStatus;
import com.avrist.urlshortener.global.exception.AVRBusinessValidationException;
import com.avrist.urlshortener.datasource.db1.entity.UrlShortenerEntity;
import com.avrist.urlshortener.datasource.db1.repository.UrlShortenerRepository;
import com.avrist.urlshortener.service.UrlShortenerService;
import com.avrist.urlshortener.service.generateshorturl.config.GenerateShortUrlConfig;
import com.avrist.urlshortener.service.generateshorturl.model.request.GenerateShortUrlRequest;
import com.avrist.urlshortener.service.generateshorturl.model.response.GenerateShortUrlResponse;
import com.avrist.urlshortener.service.generateshorturl.util.GenerateShortUrlUtil;
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
            throw new AVRBusinessValidationException(
                    AVRHttpStatus.INTERRUPTED.getCode(),
                    "Url already exists in database.",
                    HttpStatus.BAD_REQUEST,
                    new ArrayList<>()
            );
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
                    "Failed to generate url, loop reached limit, code already full reserved.",
                    HttpStatus.BAD_REQUEST,
                    new ArrayList<>()
            );
        }

        urlShortenerRepository.save(UrlShortenerEntity.builder()
                        .longUrl(input.getLongUrl())
                        .shortUrl(shortCode)
                        .createdAt(LocalDateTime.now())
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
