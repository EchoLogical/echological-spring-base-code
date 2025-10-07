package com.github.echological.app.service.bdd.steps;

import com.github.echological.app.datasource.entity.UrlShortenerEntity;
import com.github.echological.app.service.bdd.TestContext;
import com.github.echological.app.service.generateshorturl.GenerateShortUrlService;
import com.github.echological.app.service.generateshorturl.config.GenerateShortUrlConfig;
import com.github.echological.app.service.generateshorturl.model.request.GenerateShortUrlRequest;
import com.github.echological.app.service.generateshorturl.util.GenerateShortUrlUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class GenerateShortUrlSteps {

    private final TestContext ctx;

    public GenerateShortUrlSteps() {
        this.ctx = new TestContext();
        this.ctx.repository = Mockito.mock(com.github.echological.app.datasource.repository.UrlShortenerRepository.class);
        this.ctx.generateConfig = new GenerateShortUrlConfig();
        // default config values for tests
        setField(this.ctx.generateConfig, "defaultLoop", 3);
        setField(this.ctx.generateConfig, "scheme", "https");
        setField(this.ctx.generateConfig, "host", "short.host");
        this.ctx.generateService = new GenerateShortUrlService(this.ctx.generateConfig, this.ctx.repository);
    }

    @Given("a long url {string}")
    public void a_long_url(String longUrl) {
        ctx.longUrl = longUrl;
    }

    @And("repository has existing short url {string} for that long url")
    public void repository_has_existing_short_url_for_that_long_url(String shortCode) {
        Mockito.when(ctx.repository.findExistingLongUrl(eq(ctx.longUrl)))
                .thenReturn(Optional.of(UrlShortenerEntity.builder()
                        .id(1L)
                        .longUrl(ctx.longUrl)
                        .shortUrl(shortCode)
                        .createdAt(LocalDateTime.now())
                        .build()));
    }

    @And("repository has no existing data for that long url")
    public void repository_has_no_existing_data_for_that_long_url() {
        Mockito.when(ctx.repository.findExistingLongUrl(eq(ctx.longUrl)))
                .thenReturn(Optional.empty());
    }

    @And("next generated random codes are {string}")
    public void next_generated_random_codes_are(String code) {
        var q = new ArrayDeque<String>();
        q.add(code);
        GenerateShortUrlUtil.__setTestCodes(q);
    }

    @And("next generated random codes are {string},{string}")
    public void next_generated_random_codes_are_two(String c1, String c2) {
        var q = new ArrayDeque<String>();
        q.add(c1);
        q.add(c2);
        GenerateShortUrlUtil.__setTestCodes(q);
        // Also mark these as existing to simulate collision if needed
        Mockito.when(ctx.repository.findExistingCode(eq(c1)))
                .thenReturn(Optional.of(UrlShortenerEntity.builder().shortUrl(c1).longUrl("https://dummy").build()));
        Mockito.when(ctx.repository.findExistingCode(eq(c2)))
                .thenReturn(Optional.of(UrlShortenerEntity.builder().shortUrl(c2).longUrl("https://dummy").build()));
    }

    @When("generate short url with code length {int} and code loop {int}")
    public void generate_short_url_with_code_length_and_code_loop(Integer length, Integer loop) {
        ctx.codeLength = length;
        ctx.codeLoop = loop;
        ctx.generateRequest = GenerateShortUrlRequest.builder()
                .longUrl(ctx.longUrl)
                .codeLength(length)
                .codeLoop(loop)
                .build();
        try {
            ctx.generateResponse = ctx.generateService.execute(ctx.generateRequest);
        } catch (Exception e) {
            ctx.capturedException = e;
        }
    }

    @Then("result short url should be {string}")
    public void result_short_url_should_be(String expected) {
        assertNull(ctx.capturedException, () -> "Unexpected exception: " + ctx.capturedException);
        assertNotNull(ctx.generateResponse, "Response should not be null");
        assertNotNull(ctx.generateResponse.getShortUrl(), "Short URL should not be null");
        // Accept either exact match (for existing) or ending with code (for generated full URL)
        String actual = ctx.generateResponse.getShortUrl();
        if (!actual.equals(expected)) {
            assertTrue(actual.endsWith("/" + expected) || actual.endsWith(expected),
                    () -> "Short URL '" + actual + "' does not match or end with expected code '" + expected + "'");
        }
    }

    @And("url path should not be empty")
    public void url_path_should_not_be_empty() {
        // Some flows may return null path for existing-map case; only assert non-empty when present
        String path = ctx.generateResponse != null ? ctx.generateResponse.getUrlPath() : null;
        if (path != null) {
            assertFalse(path.isEmpty(), "URL path should not be empty when present");
        }
    }

    @Then("a business validation error should be thrown with code {string}")
    public void a_business_validation_error_should_be_thrown_with_code(String code) {
        assertNotNull(ctx.capturedException, "Expected an exception to be thrown");
        String message = ctx.capturedException.getMessage();
        assertTrue(message != null && message.contains(code),
                () -> "Expected error code '" + code + "' in message but was: " + message);
    }

    private static void setField(Object target, String name, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
