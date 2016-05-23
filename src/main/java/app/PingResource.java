package app;

import app.filter.Logged;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by liazhang on 4/29/16.
 */
@Slf4j
@Named
@Api
@Path("/")
public class PingResource {

    @GET
    @Logged
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        log.info("pinged");
        return "Hello client!";
    }

    @POST
    @Logged
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String ping(String input) {
        log.info("input: " + input);
        return input;
    }
}
