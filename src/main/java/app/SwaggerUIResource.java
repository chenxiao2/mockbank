package app;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * Created by liazhang on 5/23/16.
 */
@Slf4j
@Named
@Path("swagger-ui")
public class SwaggerUIResource {

    private static final String UI_BASE_PATH = "swagger-ui/";

    @GET
    @Path("{path : (.*)}")
    public Response get(@PathParam("path") String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resource = classLoader.getResourceAsStream(UI_BASE_PATH + path);
        return Response.status(Response.Status.OK)
                .type(URLConnection.guessContentTypeFromName(path))
                .entity(resource)
                .build();
    }
}
