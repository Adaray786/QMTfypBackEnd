package com.fyp;

import com.fyp.api.AuthRoleService;
import com.fyp.api.AuthService;
import com.fyp.auth.*;
import com.fyp.cli.User;
import com.fyp.db.AuthDao;
import com.fyp.db.AuthRoleDao;
import com.fyp.db.DatabaseConnector;
import com.fyp.resources.AuthController;
import com.fyp.resources.AuthRoleController;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

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
        JWTAuthenticator jwtAuthenticator = new JWTAuthenticator(new TokenService(new AuthDao(new DatabaseConnector())));

        environment.jersey().register(new AuthDynamicFeature(new JWTFilter.Builder().setAuthenticator(jwtAuthenticator).
                setAuthorizer(new JWTAuthorizer()).setPrefix("Bearer").buildAuthFilter()));

        environment.jersey().register(new AuthRoleController(new AuthRoleService(new AuthRoleDao())));
        environment.jersey().register(new AuthController(new AuthService(new AuthDao(new DatabaseConnector()),
                new TokenService(new AuthDao(new DatabaseConnector())))));

        environment.jersey().register(RolesAllowedDynamicFeature.class);

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

    }

}
