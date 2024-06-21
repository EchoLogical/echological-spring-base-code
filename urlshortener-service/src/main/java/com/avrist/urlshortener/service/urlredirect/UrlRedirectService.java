package com.avrist.urlshortener.service.urlredirect;

import com.avrist.urlshortener.global.constant.AVRHttpStatus;
import com.avrist.urlshortener.global.exception.AVRBusinessValidationException;
import com.avrist.urlshortener.global.exception.AVRContentBusinessValidationException;
import com.avrist.urlshortener.datasource.db1.repository.UrlShortenerRepository;
import com.avrist.urlshortener.service.urlredirect.model.request.UrlRedirectRequest;
import com.avrist.urlshortener.service.urlredirect.model.response.UrlRedirectResponse;
import com.avrist.urlshortener.service.UrlShortenerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UrlRedirectService
        implements UrlShortenerService<UrlRedirectRequest, UrlRedirectResponse> {

    private final UrlShortenerRepository urlShortenerRepository;

    @Override
    public UrlRedirectResponse execute(UrlRedirectRequest input) throws AVRBusinessValidationException {
            var fullLink = urlShortenerRepository.findExistingCode(input.getCode()).orElseThrow(() -> new AVRContentBusinessValidationException(
                    AVRHttpStatus.INVALID_ARGUMENT.getCode(),
                    "Invalid url",
                    HttpStatus.BAD_REQUEST,
                    new ArrayList<>()
            ));

        return UrlRedirectResponse.builder()
                .redirectUrl(fullLink.getLongUrl())
                .build();
    }
}
