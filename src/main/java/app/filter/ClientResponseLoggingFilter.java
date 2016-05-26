package app.filter;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by liazhang on 5/23/16.
 */
@Slf4j
@Named
@Provider
public class ClientResponseLoggingFilter implements ClientResponseFilter {
    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        log.info("========== Client Response ==========");
        log.info("Status code: " + responseContext.getStatusInfo().getStatusCode());
        log.info(responseContext.getStatusInfo().getReasonPhrase());
        //TODO: remove it
        responseContext.getHeaders().putSingle("Content-Type", MediaType.APPLICATION_XML);
        responseContext.getHeaders().forEach((key, values) -> log.info(String.join(":", key, values.toString())));
    }
}
