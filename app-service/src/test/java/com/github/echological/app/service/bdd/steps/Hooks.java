package com.github.echological.app.service.bdd.steps;

import com.github.echological.app.service.generateshorturl.util.GenerateShortUrlUtil;
import io.cucumber.java.After;

import java.util.ArrayDeque;

public class Hooks {

    @After
    public void tearDown() {
        // Clear any injected codes after each scenario
        GenerateShortUrlUtil.__setTestCodes(null);
    }
}
