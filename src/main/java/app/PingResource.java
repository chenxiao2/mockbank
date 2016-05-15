package app;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by liazhang on 4/29/16.
 */
@Named
@Path("/")
public class PingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "Hello client!";
    }
}
