package com.github.echological.app.service;

import com.github.echological.app.global.exception.AVRBusinessValidationException;

public interface UrlShortenerService<I, O> {
    O execute(I input) throws AVRBusinessValidationException;
}
