package com.springfox.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Autoconfiguration for SpringFox.
 */
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@ConditionalOnWebApplication
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(SpringFoxProperties.class)
@EnableSwagger2
@Configuration
public class SpringFoxAutoConfiguration {

    @Autowired
    private SpringFoxProperties springfoxProperties;

    /**
     * Publish a bean to generate swagger2 endpoints
     * @return a swagger configuration bean
     */
    @Bean
    @ConditionalOnMissingBean(Docket.class)
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(paths())
                .apis(RequestHandlerSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    /**
     * Api info
     * @return ApiInfo
     */
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(springfoxProperties.getTitle())
                .version(springfoxProperties.getVersion())
                .license(springfoxProperties.getLicense())
                .description(springfoxProperties.getDescription())
                .build();
    }

    /**
     * Config paths.
     *
     * @return the predicate
     */
    private Predicate<String> paths() {
        return regex(springfoxProperties.getPaths());
    }

    /**
     * Expands the operation list by default
     *
     * @return the {@link UiConfiguration} with expanded operation list
     */
    @Bean
    @ConditionalOnMissingBean(UiConfiguration.class)
    public UiConfiguration uiConfiguration() {
        return new UiConfiguration(null, "list", "alpha", "schema", false, true);
    }
}
