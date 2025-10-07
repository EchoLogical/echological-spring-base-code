package com.github.echological.app.service.bdd.steps;

import com.github.echological.app.service.TestJpaConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = TestJpaConfig.class)
public class CucumberSpringConfiguration {
}
