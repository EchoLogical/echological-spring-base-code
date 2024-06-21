package com.avrist.urlshortener.service.generateshorturl.controller;

import com.avrist.urlshortener.global.advice.ExceptionAdvice;
import com.avrist.urlshortener.global.advice.ResponseAdvice;
import com.avrist.urlshortener.service.generateshorturl.model.request.GenerateShortUrlRequest;
import com.avrist.urlshortener.service.generateshorturl.GenerateShortUrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = {
        GenerateShortUrlController.class,
        GenerateShortUrlService.class
})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class GenerateShortUrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String URL = "/biz/api/v1/msgconv-urlshortener/generate";

    @MockBean
    private GenerateShortUrlService generateShortUrlService;

    @Autowired
    private GenerateShortUrlController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(ExceptionAdvice.class)
                .setControllerAdvice(ResponseAdvice.class)
                .build();
    }

    @SneakyThrows
    @Test
    void testHitSuccess() {
        given(generateShortUrlService.execute(any(GenerateShortUrlRequest.class))).willReturn(null);

        var request = GenerateShortUrlRequest.builder()
                .longUrl("http://xxxxxxxx.com/xxxxxxxxxxxx/xxxxxxxxxxxxxxxxxxxxxx")
                .codeLength(2)
                .codeLoop(10)
                .build();

        MockHttpServletRequestBuilder requestBuilder = post(URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

    }

    @SneakyThrows
    @Test
    void testValidationFailed(){
        given(generateShortUrlService.execute(any(GenerateShortUrlRequest.class))).willReturn(null);

        var request = GenerateShortUrlRequest.builder()
                .longUrl("http://xxxxxxxx.com/xxxxxxxxxxxx/xxxxxxxxxxxxxxxxxxxxxx")
                .codeLoop(10)
                .build();

        MockHttpServletRequestBuilder requestBuilder = post(URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(request));

        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

}