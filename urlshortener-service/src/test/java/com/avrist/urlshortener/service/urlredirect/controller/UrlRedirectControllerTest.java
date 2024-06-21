package com.avrist.urlshortener.service.urlredirect.controller;

import com.avrist.urlshortener.global.advice.ExceptionAdvice;
import com.avrist.urlshortener.global.advice.ResponseAdvice;
import com.avrist.urlshortener.service.urlredirect.UrlRedirectService;
import com.avrist.urlshortener.service.urlredirect.model.request.UrlRedirectRequest;
import com.avrist.urlshortener.service.urlredirect.model.response.UrlRedirectResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = {
        UrlRedirectController.class,
        UrlRedirectService.class
})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UrlRedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlRedirectService urlRedirectService;

    @Autowired
    private UrlRedirectController urlRedirectController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(urlRedirectController)
                .setControllerAdvice(ExceptionAdvice.class)
                .setControllerAdvice(ResponseAdvice.class)
                .build();
    }

    @SneakyThrows
    @Test
    void testRedirect() {
        var redirectUrl = "http://xxxxx.com/xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        given(urlRedirectService.execute(any(UrlRedirectRequest.class))).willReturn(UrlRedirectResponse.builder()
                        .redirectUrl(redirectUrl)
                .build());

        MockHttpServletRequestBuilder requestBuilder = get(
                "/st/{code}",
                "testCode"
        );

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));
    }

}