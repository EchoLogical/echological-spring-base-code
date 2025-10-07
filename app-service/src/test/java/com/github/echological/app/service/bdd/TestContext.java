package com.github.echological.app.service.bdd;

import com.github.echological.app.datasource.repository.UrlShortenerRepository;
import com.github.echological.app.service.generateshorturl.GenerateShortUrlService;
import com.github.echological.app.service.generateshorturl.config.GenerateShortUrlConfig;
import com.github.echological.app.service.generateshorturl.model.request.GenerateShortUrlRequest;
import com.github.echological.app.service.generateshorturl.model.response.GenerateShortUrlResponse;
import com.github.echological.app.service.urlredirect.UrlRedirectService;
import com.github.echological.app.service.urlredirect.model.request.UrlRedirectRequest;
import com.github.echological.app.service.urlredirect.model.response.UrlRedirectResponse;

/**
 * Shared mutable state between Cucumber steps.
 */
public class TestContext {
    // Common
    public UrlShortenerRepository repository;

    // Generate short URL
    public String longUrl;
    public Integer codeLength;
    public Integer codeLoop;
    public GenerateShortUrlService generateService;
    public GenerateShortUrlConfig generateConfig;
    public GenerateShortUrlRequest generateRequest;
    public GenerateShortUrlResponse generateResponse;

    // Redirect
    public String shortCode;
    public UrlRedirectService redirectService;
    public UrlRedirectRequest redirectRequest;
    public UrlRedirectResponse redirectResponse;

    // Captured error
    public Exception capturedException;
}