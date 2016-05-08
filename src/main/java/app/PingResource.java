package app;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by liazhang on 4/29/16.
 */
@Named
@Path("/")
public class PingResource {

    @GET
    public String ping() {
        return "Hello client!";
    }
}
