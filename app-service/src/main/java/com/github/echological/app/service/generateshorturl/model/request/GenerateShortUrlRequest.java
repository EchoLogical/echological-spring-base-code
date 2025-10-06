package com.github.echological.app.service.generateshorturl.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateShortUrlRequest {
    @NotBlank(message = "longUrl must not be blank for this request")
    private String longUrl;
    @NotNull(message = "codeLength must not be blank for this request")
    @Min(message = "codeLength in 2", value = 2)
    private Integer codeLength;
    private Integer codeLoop;
}
