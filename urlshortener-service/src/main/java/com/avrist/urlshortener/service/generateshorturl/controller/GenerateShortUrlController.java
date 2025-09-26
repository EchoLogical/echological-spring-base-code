package com.avrist.urlshortener.service.generateshorturl.controller;

import com.avrist.urlshortener.global.constant.AVRHttpStatus;
import com.avrist.urlshortener.global.exception.AVRBusinessValidationException;
import com.avrist.urlshortener.global.model.BaseResponse;
import com.avrist.urlshortener.service.generateshorturl.model.request.GenerateShortUrlRequest;
import com.avrist.urlshortener.service.generateshorturl.model.response.GenerateShortUrlResponse;
import com.avrist.urlshortener.service.generateshorturl.GenerateShortUrlService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/biz/api/v1/msgconv-urlshortener/generate")
@Tag(name = "msgconv-urlshortener", description = "msgconv-urlshortener")
public class GenerateShortUrlController {

    private final GenerateShortUrlService generateShortUrlService;

    @PostMapping("")
    public ResponseEntity<BaseResponse<GenerateShortUrlResponse>> generate(
            @RequestHeader("key") String key,
            @RequestBody @Valid GenerateShortUrlRequest body
    ) throws AVRBusinessValidationException {
        body.setKey(key);
        return ResponseEntity
                .ok()
                .body(BaseResponse.<GenerateShortUrlResponse>builder()
                        .responseCode(AVRHttpStatus.OK.getCode())
                        .responseMessage(AVRHttpStatus.OK.getStatus())
                        .content(generateShortUrlService.execute(body))
                        .build());
    }

}
