package app.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.inject.Named;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Created by liazhang on 5/19/16.
 */
@Slf4j
//@Logged //if not using name binding, apply to all resource methods.
@PreMatching //filter before resource matching
@Named
@Provider
public class RequestLoggingFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("========== Request ==========");
        log.info(requestContext.getUriInfo().getRequestUri().toString());
        log.info(requestContext.getMethod());
        requestContext.getHeaders().forEach((key, values) -> log.info(String.join(":", key, values.toString())));
    }
}
