package com.github.echological.app.service.urlredirect.controller;

import com.github.echological.akint.global.exception.AVRBusinessValidationException;
import com.github.echological.akint.service.urlredirect.UrlRedirectService;
import com.github.echological.akint.service.urlredirect.model.request.UrlRedirectRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("st")
@Tag(name = "msgconv-urlshortener", description = "msgconv-urlshortener")
public class UrlRedirectController {

    private final UrlRedirectService urlRedirectService;

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(
            HttpServletRequest request,
            @RequestHeader Map<String, String> headers,
            @PathVariable("code") String code
    ) throws AVRBusinessValidationException {
        var res = urlRedirectService.execute( UrlRedirectRequest.builder()
                    .code(code)
                    .request(request)
                .build());

        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(res.getRedirectUrl()))
                .build();
    }

}
