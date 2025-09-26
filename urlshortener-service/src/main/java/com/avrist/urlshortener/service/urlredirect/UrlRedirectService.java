package com.avrist.urlshortener.service.urlredirect;

import com.avrist.urlshortener.datasource.db1.entity.HistoryAccessEntity;
import com.avrist.urlshortener.datasource.db1.repository.HistoryAccessRepository;
import com.avrist.urlshortener.datasource.db1.repository.UrlShortenerRepository;
import com.avrist.urlshortener.global.constant.AVRHttpStatus;
import com.avrist.urlshortener.global.constant.AppContant;
import com.avrist.urlshortener.global.exception.AVRBusinessValidationException;
import com.avrist.urlshortener.global.exception.AVRContentBusinessValidationException;
import com.avrist.urlshortener.service.UrlShortenerService;
import com.avrist.urlshortener.service.urlredirect.model.request.UrlRedirectRequest;
import com.avrist.urlshortener.service.urlredirect.model.response.UrlRedirectResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UrlRedirectService
        implements UrlShortenerService<UrlRedirectRequest, UrlRedirectResponse> {

    private final UrlShortenerRepository urlShortenerRepository;
    private final HistoryAccessRepository historyAccessRepository;

    private void logSuccess(Long id, String partnerCode, String accessedBy) {
        historyAccessRepository.save(HistoryAccessEntity.builder()
                .partnerCode(partnerCode)
                .idUrlShort(id)
                .messages("SUCCESS HIT")
                .accessDate(LocalDateTime.now())
                .accessBy(accessedBy)
                .build());
    }

    private String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For can contain multiple IPs -> first one is client
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    @Override
    public UrlRedirectResponse execute(UrlRedirectRequest input) throws AVRBusinessValidationException {
        var fullLink = urlShortenerRepository.findExistingCode(input.getCode()).orElseThrow(() -> new AVRContentBusinessValidationException(
                AVRHttpStatus.INVALID_ARGUMENT.getCode(),
                "Invalid url",
                HttpStatus.BAD_REQUEST,
                new ArrayList<>()
        ));

        logSuccess(fullLink.getId(), fullLink.getCreatedBy(), getRealIp(input.getRequest()));

        return UrlRedirectResponse.builder()
                .redirectUrl(fullLink.getLongUrl())
                .build();
    }
}
