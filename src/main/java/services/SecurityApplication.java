package services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liang on 4/7/2016.
 */
@ApplicationPath("/services")
public class SecurityApplication extends Application {
    private Set<Object> singletons = new HashSet<>();

    public SecurityApplication() throws Exception {
        singletons.add(new CryptoResource());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
