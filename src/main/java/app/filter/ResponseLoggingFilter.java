package app.filter;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by liazhang on 5/20/16.
 */
@Slf4j
@Logged
@Named
@Provider
public class ResponseLoggingFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        log.info("========== Response ==========");
        log.info("Status code: " + responseContext.getStatusInfo().getStatusCode());
        log.info(responseContext.getStatusInfo().getReasonPhrase());
        responseContext.getHeaders().forEach((key, values) -> log.info(String.join(":", key, values.toString())));
    }
}
