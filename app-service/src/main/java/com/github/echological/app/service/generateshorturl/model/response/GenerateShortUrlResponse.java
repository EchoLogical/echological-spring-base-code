package com.github.echological.app.service.generateshorturl.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateShortUrlResponse {
    private String urlPath;
    private String shortUrl;
}
