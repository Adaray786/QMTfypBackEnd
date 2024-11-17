package com.fyp;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class QMTBackendApplication extends Application<QMTBackendConfiguration> {

    public static void main(final String[] args) throws Exception {
        new QMTBackendApplication().run(args);
    }

    @Override
    public String getName() {
        return "QMTBackend";
    }

    @Override
    public void initialize(final Bootstrap<QMTBackendConfiguration> bootstrap) {
        // TODO: application initialization
        bootstrap.addBundle(new SwaggerBundle<QMTBackendConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(QMTBackendConfiguration configuration) {
                return configuration.getSwagger();
            }
        });
    }

    @Override
    public void run(final QMTBackendConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
