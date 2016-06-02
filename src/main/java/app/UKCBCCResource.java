package app;

import app.filter.ClientRequestLoggingFilter;
import app.filter.ClientResponseLoggingFilter;
import app.filter.Logged;
import app.interceptor.RequestLoggingInterceptor;
import app.interceptor.ResponseLoggingInterceptor;
import app.util.SSLUtil;
import app.util.UKCBCCUtil;
import com.amazon.payments.cobrandcreditcard.protocol.v1.ApplicationStateChangeRequest;
import com.amazon.payments.cobrandcreditcard.protocol.v1.ApplicationStateChangeResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.Future;

/**
 * Created by liazhang on 6/1/16.
 */
@Api
@Path("cbcc/uk")
@Named
@Slf4j
public class UKCBCCResource {


    @Path("notify")
    @POST
    @Logged
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    @Produces(MediaType.TEXT_XML)
    public ApplicationStateChangeResponse handle(@HeaderParam("X-Mockbank-NotificationEndpoint") String clientEndpoint,
                                                 @HeaderParam("X-Mockbank-Async") boolean isAsync,
                                                 ApplicationStateChangeRequest request) throws Exception {
//        ClientBuilder clientBuilder = ClientBuilder.newBuilder().trustStore(trustStore);
//        Client client = clientBuilder.newClient();
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(clientEndpoint)) {
            log.error("Invalid URL endpoint: " + clientEndpoint);
            clientEndpoint = "http://localhost:8080/client/cbcc/uk";
        }
        Client client = SSLUtil.IgnoreSSLClient();
        WebTarget webTarget = client.target(clientEndpoint);
        webTarget.register(ClientRequestLoggingFilter.class)
                .register(ClientResponseLoggingFilter.class)
                .register(ResponseLoggingInterceptor.class)
                .register(RequestLoggingInterceptor.class);
        Invocation invocation = webTarget.request().buildPost(Entity.xml(request));
        try {
            if (isAsync) {
                Future<ApplicationStateChangeResponse> futureResponse = invocation.submit(ApplicationStateChangeResponse.class);
                return futureResponse.get();
            } else {
                return invocation.invoke(ApplicationStateChangeResponse.class);
            }
        } finally {
            client.close();
        }
    }

    @GET
    @Logged
    @Produces(MediaType.TEXT_XML)
    public ApplicationStateChangeResponse handle(@HeaderParam("X-Mockbank-NotificationEndpoint") String clientEndpoint,
                                                 @HeaderParam("X-Mockbank-Async") boolean isAsync,
                                                 @QueryParam("ari") String ari) throws Exception{
        ApplicationStateChangeRequest request = UKCBCCUtil.createRequest(ari);
        return handle(clientEndpoint, isAsync, request);
    }
}
