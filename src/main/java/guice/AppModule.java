package guice;

import com.google.inject.AbstractModule;

import javax.inject.Named;

/**
 * Created by liazhang on 5/8/16.
 */
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ComponentScanModule("app", Named.class));
    }
}
