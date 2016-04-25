package app.client;

import app.XMLEncryptionResource;

import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liang on 4/24/2016.
 */

@ApplicationPath("/client")
public class ClientApplication extends javax.ws.rs.core.Application {
    private Set<Object> singletons = new HashSet<>();

    public ClientApplication() throws Exception {
        singletons.add(new ClientGILFranceResource());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
