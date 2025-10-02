package com.github.echological.akint.service.generateshorturl.controller;

import com.github.echological.akint.global.constant.AVRHttpStatus;
import com.github.echological.akint.global.exception.AVRBusinessValidationException;
import com.github.echological.akint.global.model.BaseResponse;
import com.github.echological.akint.service.generateshorturl.GenerateShortUrlService;
import com.github.echological.akint.service.generateshorturl.model.request.GenerateShortUrlRequest;
import com.github.echological.akint.service.generateshorturl.model.response.GenerateShortUrlResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("generate")
@Tag(name = "msgconv-urlshortener", description = "msgconv-urlshortener")
public class GenerateShortUrlController {

    private final GenerateShortUrlService generateShortUrlService;

    @PostMapping("")
    public ResponseEntity<BaseResponse<GenerateShortUrlResponse>> generate(
            @RequestBody @Valid GenerateShortUrlRequest body
    ) throws AVRBusinessValidationException {
        return ResponseEntity
                .ok()
                .body(BaseResponse.<GenerateShortUrlResponse>builder()
                        .responseCode(AVRHttpStatus.OK.getCode())
                        .responseMessage(AVRHttpStatus.OK.getStatus())
                        .content(generateShortUrlService.execute(body))
                        .build());
    }

}
