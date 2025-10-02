package com.github.echological.akint.service;

import com.github.echological.akint.global.exception.AVRBusinessValidationException;

public interface UrlShortenerService<I, O> {
    O execute(I input) throws AVRBusinessValidationException;
}
