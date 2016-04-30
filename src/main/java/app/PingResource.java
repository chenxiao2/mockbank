package app;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by liazhang on 4/29/16.
 */
@Component
@Path("/")
public class PingResource {

    @GET
    public String ping() {
        return "Hello client!";
    }
}
