package com.github.echological.app.service.generateshorturl.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
