package com.fyp;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class QMTBackendConfiguration extends Configuration {
    // TODO: implement service configuration
    @Valid
    @NotNull
    private final SwaggerBundleConfiguration swagger = new SwaggerBundleConfiguration();

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration getSwagger() {
        swagger.setResourcePackage("com.fyp.resources");
        String[] schemes = {"http", "https"};
        swagger.setSchemes(schemes);
        return swagger;
    }
}
