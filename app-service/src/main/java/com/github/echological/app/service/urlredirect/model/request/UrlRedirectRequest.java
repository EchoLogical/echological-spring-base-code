package com.github.echological.app.service.urlredirect.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlRedirectRequest {
    private String code;
    private HttpServletRequest request;
}
