package app;

import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liang on 4/7/2016.
 */
@ApplicationPath("/")
public class Application extends javax.ws.rs.core.Application {
    private Set<Object> singletons = new HashSet<>();

    public Application() throws Exception {
        singletons.add(new XMLEncryptionResource());
        singletons.add(new GILFranceResource());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
