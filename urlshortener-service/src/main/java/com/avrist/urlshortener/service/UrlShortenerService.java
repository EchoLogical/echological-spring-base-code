package com.avrist.urlshortener.service;

import com.avrist.urlshortener.global.exception.AVRBusinessValidationException;

public interface UrlShortenerService<I, O> {
    O execute(I input) throws AVRBusinessValidationException;
}
