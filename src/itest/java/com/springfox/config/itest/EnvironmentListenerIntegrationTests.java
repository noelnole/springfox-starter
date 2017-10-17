package com.springfox.config.itest;

import java.util.LinkedHashSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EnvironmentListenerIntegrationTests.TestConfiguration.class)
@WebIntegrationTest(randomPort = true)
@TestPropertySource(properties = {
        "spring.application.name=sampleSwagger",
        "swagger.title=Prueba del Starter Swagger",
        "swagger.description=Prueba de autoconfiguración de Swagger",
        "swagger.version=1.0",
        "swagger.paths=/api.*",
        "swagger.license: Apache License Version 2.0"
})
public class EnvironmentListenerIntegrationTests {

    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * Checks that the property "security.ignored" is in the environment after SpringFoxEnvironmentPostProcessor is executed.
     */
    @Test
    public void securityIgnoredPropertyTest(){
        ConfigurableEnvironment environment = context.getEnvironment();

        MutablePropertySources propertySources = environment.getPropertySources();
        PropertySource<?> modifiedSecurityIgnoredPropertySource = propertySources.get("ModifiedSecurityIgnoredPropertySource");
        Object securityIgnoredProperty = modifiedSecurityIgnoredPropertySource.getProperty("security.ignored");

        Assert.assertNotNull("security.gnored property must not be null", securityIgnoredProperty);
    }

    /**
     * Checks that the property "security.ignored" has the correct values
     */
    @Test
    public void securityIgnoredPropertyValuesTest(){
        ConfigurableEnvironment environment = context.getEnvironment();

        MutablePropertySources propertySources = environment.getPropertySources();
        PropertySource<?> modifiedSecurityIgnoredPropertySource = propertySources.get("ModifiedSecurityIgnoredPropertySource");
        Object securityIgnoredPropertyValues = modifiedSecurityIgnoredPropertySource.getProperty("security.ignored");

        LinkedHashSet set = (LinkedHashSet) securityIgnoredPropertyValues;

        Assert.assertTrue("/webjars/** property is not in the security.ignored set", set.contains("/webjars/**"));
        Assert.assertTrue("/swagger-resources property is not in the security.ignored set", set.contains("/swagger-resources"));
        Assert.assertTrue("/v2/api-docs property is not in the security.ignored set", set.contains("/v2/api-docs"));
        Assert.assertTrue("/configuration/ui property is not in the security.ignored set", set.contains("/configuration/ui"));
        Assert.assertTrue("/configuration/security property is not in the security.ignored set", set.contains("/configuration/security"));
        Assert.assertTrue("/swagger-ui.html property is not in the security.ignored set", set.contains("/swagger-ui.html"));
    }


    @SpringBootApplication
    static class TestConfiguration {
        public static void main(String[] args) {
            SpringApplication.run(TestConfiguration.class, args);
        }
    }
}
