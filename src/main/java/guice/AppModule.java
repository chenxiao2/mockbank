package guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.swagger.config.Scanner;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.AcceptHeaderApiListingResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import javax.inject.Named;

/**
 * Created by liazhang on 5/8/16.
 */
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(ApiListingResource.class);
        bind(AcceptHeaderApiListingResource.class);
        bind(SwaggerSerializers.class);

        bind(Scanner.class).toInstance(provideBeanConfig());

        install(new ComponentScanModule("app", Named.class));
    }

//    @Provides
    Scanner provideBeanConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Mockbank");
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[]{"http"});
//        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("app");
        beanConfig.setScan(true);
        return beanConfig;
    }

}
