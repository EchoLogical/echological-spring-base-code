package com.avrist.urlshortener.global.controller;

import com.avrist.urlshortener.global.constant.AVRHttpStatus;
import com.avrist.urlshortener.global.model.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AppErrorController implements ErrorController {

    private static final Logger log = LoggerFactory.getLogger(AppErrorController.class);

    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        String requestId = UUID.randomUUID().toString();
        Long requestTime = new Date().getTime();

        log.error("request id {}", requestId);
        log.error("request time {}", requestTime);
        log.error("url {}", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
                .responseCode(AVRHttpStatus.NO_HANDLER_FOUND.getCode())
                .responseMessage(AVRHttpStatus.NO_HANDLER_FOUND.getStatus())
                .requestId(requestId)
                .error(Stream.of("No handler found for this path.").collect(Collectors.toList()))
                .requestTimestamp(requestTime)
                .build());
    }
}
