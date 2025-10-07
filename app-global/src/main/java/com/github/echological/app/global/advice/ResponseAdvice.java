package com.github.echological.app.global.advice;


import com.github.echological.app.global.model.BaseResponse;
import com.github.echological.app.global.model.PaginatedBaseResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Date;
import java.util.UUID;

@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger log = LoggerFactory.getLogger(ResponseAdvice.class);

    private static final String LOG_MESSAGE = "requestId {}: {} to {}";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        String requestId = UUID.randomUUID().toString();

        if(ObjectUtils.isEmpty(body)) return body;

        if (body.getClass().equals(BaseResponse.class)) {
            BaseResponse<Object> defaultResponse = (BaseResponse<Object>) body;

            requestId = ObjectUtils.isEmpty(defaultResponse.getRequestId()) ?
                    requestId : defaultResponse.getRequestId();

            log.info(LOG_MESSAGE, requestId, request.getMethod(), request.getURI().getPath());

            return BaseResponse.builder()
                    .requestId(requestId)
                    .responseCode(defaultResponse.getResponseCode())
                    .requestTimestamp(new Date().getTime())
                    .requestPath(request.getURI().getPath())
                    .responseMessage(defaultResponse.getResponseMessage())
                    .error(ObjectUtils.isEmpty(defaultResponse.getError()) ? null : defaultResponse.getError())
                    .content(defaultResponse.getContent())
                    .build();
        } else if (body.getClass().equals(PaginatedBaseResponse.class)) {
            PaginatedBaseResponse<Object> defaultResponse = (PaginatedBaseResponse<Object>) body;

            requestId = ObjectUtils.isEmpty(defaultResponse.getRequestId()) ?
                    requestId : defaultResponse.getRequestId();
            log.info(LOG_MESSAGE, requestId, request.getMethod(), request.getURI().getPath());

            return PaginatedBaseResponse.builder()
                    .requestId(requestId)
                    .requestTimestamp(new Date().getTime())
                    .requestPath(request.getURI().getPath())
                    .pageInfo(defaultResponse.getPageInfo())
                    .responseMessage(defaultResponse.getResponseMessage())
                    .error(ObjectUtils.isEmpty(defaultResponse.getError()) ? null : defaultResponse.getError())
                    .content(defaultResponse.getContent())
                    .build();
        } else {
            log.info(LOG_MESSAGE, requestId, request.getMethod(), request.getURI().getPath());
            return body;
        }
    }
}
