package com.github.echological.app.service.bdd.steps;

import com.github.echological.app.datasource.entity.UrlShortenerEntity;
import com.github.echological.app.service.bdd.TestContext;
import com.github.echological.app.service.urlredirect.UrlRedirectService;
import com.github.echological.app.service.urlredirect.model.request.UrlRedirectRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

public class UrlRedirectSteps {

    private final TestContext ctx;

    public UrlRedirectSteps() {
        this.ctx = new TestContext();
        this.ctx.repository = Mockito.mock(com.github.echological.app.datasource.repository.UrlShortenerRepository.class);
        this.ctx.redirectService = new UrlRedirectService(this.ctx.repository);
    }

    @Given("a short code {string}")
    public void a_short_code(String code) {
        ctx.shortCode = code;
    }

    @And("repository has mapping code {string} to long url {string}")
    public void repository_has_mapping_code_to_long_url(String code, String longUrl) {
        Mockito.when(ctx.repository.findExistingCode(eq(code)))
                .thenReturn(Optional.of(UrlShortenerEntity.builder().shortUrl(code).longUrl(longUrl).build()));
    }

    @And("repository has no mapping for that code")
    public void repository_has_no_mapping_for_that_code() {
        Mockito.when(ctx.repository.findExistingCode(eq(ctx.shortCode))).thenReturn(Optional.empty());
    }

    @When("redirect is requested")
    public void redirect_is_requested() {
        ctx.redirectRequest = com.github.echological.app.service.urlredirect.model.request.UrlRedirectRequest.builder()
                .code(ctx.shortCode)
                .build();
        try {
            ctx.redirectResponse = ctx.redirectService.execute(ctx.redirectRequest);
        } catch (Exception e) {
            ctx.capturedException = e;
        }
    }

    @Then("redirect url should be {string}")
    public void redirect_url_should_be(String expected) {
        assertNull(ctx.capturedException, () -> "Unexpected exception: " + ctx.capturedException);
        assertNotNull(ctx.redirectResponse);
        assertEquals(expected, ctx.redirectResponse.getRedirectUrl());
    }

}
