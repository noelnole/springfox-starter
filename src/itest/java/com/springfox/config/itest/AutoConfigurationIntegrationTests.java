package com.springfox.config.itest;

import com.springfox.config.SpringFoxAutoConfiguration;
import com.springfox.config.SpringFoxProperties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Integration test for SpringFoxAutoConfiguration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AutoConfigurationIntegrationTests.TestConfiguration.class)
@WebIntegrationTest(randomPort = true)
@TestPropertySource(properties = {
        "spring.application.name=sampleSwagger",
        "swagger.title=Prueba del Starter Swagger",
        "swagger.description=Prueba de autoconfiguración de Swagger",
        "swagger.version=1.0",
        "swagger.paths=/api.*",
        "swagger.license: Apache License Version 2.0"
})
public class AutoConfigurationIntegrationTests {

    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * Checks that the Docket is not null.
     */
    @Test
    public void autoconfigurationNotNullTest(){
        SpringFoxAutoConfiguration conf = context.getBean(SpringFoxAutoConfiguration.class);
        Assert.assertNotNull("SpringFoxAutoConfiguration must not be null", conf);
    }

    /**
     * Checks version, license and title.
     */
    @Test
    public void ApiInfoTest(){
        SpringFoxAutoConfiguration conf = context.getBean(SpringFoxAutoConfiguration.class);

        ApiInfo apiInfo = conf.apiInfo();

        Assert.assertEquals("1.0", apiInfo.getVersion());
        Assert.assertEquals("Apache License Version 2.0", apiInfo.getLicense());
        Assert.assertEquals("Prueba del Starter Swagger", apiInfo.getTitle());
        Assert.assertEquals("Prueba de autoconfiguración de Swagger", apiInfo.getDescription());
    }

    /**
     * Checks DocumentationType, groupName and that the docket is enabled.
     */
    @Test
    public void autoconfigurationDocumentationTypeTest(){
        SpringFoxAutoConfiguration conf = context.getBean(SpringFoxAutoConfiguration.class);

        Docket docket = conf.api();

        Assert.assertEquals("swagger", docket.getDocumentationType().getName());
        Assert.assertEquals("2.0", docket.getDocumentationType().getVersion());
        Assert.assertEquals("default", docket.getGroupName());
        Assert.assertEquals(true, docket.isEnabled());
    }

    /**
     * Checks the application name.
     */
    @Test
    public void propertiesTest(){
        SpringFoxProperties conf = context.getBean(SpringFoxProperties.class);
        Assert.assertEquals("sampleSwagger", conf.getAppName());
    }

    @SpringBootApplication
    static class TestConfiguration {
        public static void main(String[] args) {
            SpringApplication.run(TestConfiguration.class, args);
        }
    }
}
