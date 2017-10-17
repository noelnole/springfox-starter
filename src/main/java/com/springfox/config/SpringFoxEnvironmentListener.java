package com.springfox.config;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.MapPropertySource;

import lombok.extern.slf4j.Slf4j;

/**
 * Modify security.ignored endpoints to add by default swagger endpoints
 *
 */
@Slf4j
public class SpringFoxEnvironmentListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static final String MODIFIED_SECURITY_IGNORED_PROPERTY_SOURCE =
            "ModifiedSecurityIgnoredPropertySource";
    private static final String DEFAULT_SWAGGER_ENDPOINT = "/v2/api-docs";
    private static final String SPRINGFOX_DOCUMENTATION_SWAGGER_V2_PATH_PROPERTY =
            "springfox.documentation.swagger.v2.path";
    private static final String SECURITY_IGNORED_PROPERTY = "security.ignored";

    private Set<String> defaultSwaggerEndpointSet = new LinkedHashSet<String>();

    /**
     * Creates a listener adding all the default swagger endpoints.
     */
    public SpringFoxEnvironmentListener() {
        defaultSwaggerEndpointSet.add("/swagger-ui.html");
        defaultSwaggerEndpointSet.add("/webjars/**");
        defaultSwaggerEndpointSet.add("/swagger-resources");
        defaultSwaggerEndpointSet.add("/configuration/ui");
        defaultSwaggerEndpointSet.add("/configuration/security");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.
     * context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {

        // Manually read spring fox configuration path. (@Value does not work with post
        // processors)If exists use this value to ignore, otherwise set
        // defaults

        String swaggerV2Path = ObjectUtils.defaultIfNull(
                event.getApplicationContext().getEnvironment()
                        .getProperty(SPRINGFOX_DOCUMENTATION_SWAGGER_V2_PATH_PROPERTY),
                DEFAULT_SWAGGER_ENDPOINT);

        // retrieve security ignored paths
        Set<String> securityIgnoredSet = event.getApplicationContext().getEnvironment().getProperty(
                SECURITY_IGNORED_PROPERTY, LinkedHashSet.class, new LinkedHashSet<String>());

        // add swagger v2 path and the other endpoints
        securityIgnoredSet.add(swaggerV2Path);
        securityIgnoredSet.addAll(defaultSwaggerEndpointSet);

        Map<String, Object> securityIgnoredEndpoints = new HashMap<String, Object>();
        securityIgnoredEndpoints.put(SECURITY_IGNORED_PROPERTY, securityIgnoredSet);

        // creates an aditional propertysource with preference over the security property
        event.getApplicationContext().getEnvironment().getPropertySources()
                .addFirst(new MapPropertySource(MODIFIED_SECURITY_IGNORED_PROPERTY_SOURCE,
                        securityIgnoredEndpoints));

        log.info("Swagger-ui endpoints set to security.ignored in the environment {}", event
                .getApplicationContext().getEnvironment().getProperty(SECURITY_IGNORED_PROPERTY));
    }

}
