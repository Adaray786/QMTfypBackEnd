package com.fyp;

import com.fyp.api.*;
import com.fyp.auth.*;
import com.fyp.cli.User;
import com.fyp.db.*;
import com.fyp.resources.*;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class QMTfypBackendApplication extends Application<QMTfypBackendConfiguration> {

    public static void main(final String[] args) throws Exception {
        new QMTfypBackendApplication().run(args);
    }

    @Override
    public String getName() {
        return "QMTBackend";
    }

    @Override
    public void initialize(final Bootstrap<QMTfypBackendConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<QMTfypBackendConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(QMTfypBackendConfiguration configuration) {
                return configuration.getSwagger();
            }
        });
    }

    @Override
    public void run(final QMTfypBackendConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
        JWTAuthenticator jwtAuthenticator = new JWTAuthenticator(new TokenService(new AuthDao(new DatabaseConnector())));
        // Register CORS filter
        environment.jersey().register(new CorsFilter());

        environment.jersey().register(new AuthDynamicFeature(new JWTFilter.Builder().setAuthenticator(jwtAuthenticator).
                setAuthorizer(new JWTAuthorizer()).setPrefix("Bearer").buildAuthFilter()));

        environment.jersey().register(new AuthRoleController(new AuthRoleService(new AuthRoleDao())));
        environment.jersey().register(new AuthController(new AuthService(new AuthDao(new DatabaseConnector()),
                new TokenService(new AuthDao(new DatabaseConnector())))));
        environment.jersey().register(new UserController(new UserService(new UserDao(new DatabaseConnector()))));

        environment.jersey().register(RolesAllowedDynamicFeature.class);

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        environment.jersey().register(new SurahController(new SurahService(new SurahDao(new DatabaseConnector()))));
        environment.jersey().register(new AyahController(new AyahService(new AyahDao(new DatabaseConnector()))));
        environment.jersey().register(new UserScoreController(new UserScoreService(new UserScoreDao(new DatabaseConnector()))));

        environment.jersey().register(new UserSurahProgressController(new UserSurahProgressService(new UserSurahProgressDao(new DatabaseConnector()))));
        environment.jersey().register(new UserAyahProgressController(new UserAyahProgressService(new UserAyahProgressDao(new DatabaseConnector()))));
        environment.jersey().register(new FriendController(new FriendService(new FriendDao(new DatabaseConnector()))));

        // Register Challenge Controller
        DatabaseConnector dbConnector = new DatabaseConnector();
        environment.jersey().register(new ChallengeController(
            new ChallengeService(
                new ChallengeRequestDao(dbConnector),
                new ActiveChallengeDao(dbConnector),
                new CompletedChallengeDao(dbConnector),
                new UserSurahProgressDao(dbConnector)
            )
        ));

        // Register Recommendation Controller
        environment.jersey().register(new RecommendationController(
            new RecommendationService(
                new RecommendationDao(new DatabaseConnector()),
                new SurahDao(new DatabaseConnector())
            )
        ));
    }

}
