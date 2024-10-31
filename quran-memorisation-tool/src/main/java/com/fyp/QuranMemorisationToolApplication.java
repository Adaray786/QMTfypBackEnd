package com.fyp;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class QuranMemorisationToolApplication extends Application<QuranMemorisationToolConfiguration> {

    public static void main(final String[] args) throws Exception {
        new Quran Memorisation ToolApplication().run(args);
    }

    @Override
    public String getName() {
        return "Quran Memorisation Tool";
    }

    @Override
    public void initialize(final Bootstrap<QuranMemorisationToolConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final QuranMemorisationToolConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
