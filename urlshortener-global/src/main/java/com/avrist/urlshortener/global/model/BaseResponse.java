package com.avrist.urlshortener.global.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    String responseCode;
    String responseMessage;
    @Builder.Default
    String requestId = UUID.randomUUID().toString();
    @Builder.Default
    Long requestTimestamp = new Date().getTime();
    String requestPath;
    Object error;
    T content;



}
