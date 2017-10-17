package com.springfox.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * SpringFox properties
 */
@ConfigurationProperties(prefix = SpringFoxProperties.SWAGGER_PREFIX)
@Data
public class SpringFoxProperties {

    public static final String SWAGGER_PREFIX = "swagger";

    @Value("${spring.application.name:''}")
    private String appName;

    /**
     * Swagger page title.
     * If there is no title defined it will be the value of spring.application.name property
     */
    private String title = appName;

    /**
     * Description
     */
    private String description = "";

    /**
     * API version
     */
    private String version = "";

    /**
     * Swagger paths
     */
    private String paths = "/api.*";

    /**
     * License
     */
    private String license = "Apache License Version 2.0";
}
