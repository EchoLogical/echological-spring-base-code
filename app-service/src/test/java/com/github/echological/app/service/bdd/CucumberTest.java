package com.github.echological.app.service.bdd;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CucumberTest {

    @Test
    void runUrlRedirectFeature() {
        byte exitStatus = io.cucumber.core.cli.Main.run(new String[]{
                "--plugin", "pretty",
                "--plugin", "summary",
                "--glue", "com.github.echological.app.service.bdd.steps",
                "classpath:features/urlredirect/url_redirect.feature"
        }, Thread.currentThread().getContextClassLoader());
        assertEquals(0, exitStatus, "Cucumber scenarios failed");
    }

    @Test
    void runGenerateShortUrlFeature() {
        byte exitStatus = io.cucumber.core.cli.Main.run(new String[]{
                "--plugin", "pretty",
                "--plugin", "summary",
                "--glue", "com.github.echological.app.service.bdd.steps",
                "classpath:features/generateshorturl/generate_short_url.feature"
        }, Thread.currentThread().getContextClassLoader());
        assertEquals(0, exitStatus, "Cucumber scenarios failed");
    }
}


