package com.github.echological.app.service.urlredirect;

import com.github.echological.app.datasource.repository.UrlShortenerRepository;
import com.github.echological.app.global.constant.AVRHttpStatus;
import com.github.echological.app.global.exception.AVRBusinessValidationException;
import com.github.echological.app.global.exception.AVRContentBusinessValidationException;
import com.github.echological.app.service.UrlShortenerService;
import com.github.echological.app.service.urlredirect.model.request.UrlRedirectRequest;
import com.github.echological.app.service.urlredirect.model.response.UrlRedirectResponse;
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
