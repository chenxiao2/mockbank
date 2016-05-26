package app;

import app.filter.ClientRequestLoggingFilter;
import app.filter.ClientResponseLoggingFilter;
import app.filter.Logged;
import app.interceptor.RequestLoggingInterceptor;
import app.interceptor.ResponseLoggingInterceptor;
import com.amazon.payments.globalinstallmentlending.protocol.v1.*;
import io.swagger.annotations.Api;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Future;

import static app.util.GILFranceUtil.fillCommonResponse;

/**
 * Created by liang on 4/10/2016.
 */
@Api
@Path("gil/fr")
@Named
public class GILFranceResource {

    @Inject
    private KeyStore trustStore;

    @POST
    @Logged
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    @Produces(MediaType.TEXT_XML)
    public Response handle(Request request) throws Exception {
        if(request instanceof LoanApplicationCreationRequest)
            return handle((LoanApplicationCreationRequest)request);
        else if(request instanceof LoanConfirmationRequest)
            return handle((LoanConfirmationRequest)request);
        else if(request instanceof LoanAdjustmentRequest)
            return handle((LoanAdjustmentRequest)request);
        else if(request instanceof LoanChargeRequest)
            return handle((LoanChargeRequest)request);
        else
            return null;
    }

    @Path("notify")
    @POST
    @Logged
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    @Produces(MediaType.TEXT_XML)
    public LoanStatusNotificationResponse handle(@HeaderParam("X-Mockbank-NotificationEndpoint") String clientEndpoint,
                                                 @HeaderParam("X-Mockbank-Async") boolean isAsync,
                                                 LoanStatusNotificationRequest request) throws Exception {
//        ClientBuilder clientBuilder = ClientBuilder.newBuilder().trustStore(trustStore);
//        Client client = clientBuilder.newClient();
        Client client = IgnoreSSLClient();
        WebTarget webTarget = client.target(clientEndpoint);
        webTarget.register(ClientRequestLoggingFilter.class)
                .register(ClientResponseLoggingFilter.class)
                .register(ResponseLoggingInterceptor.class)
                .register(RequestLoggingInterceptor.class);
        Invocation invocation = webTarget.request().header("Message-Type", "LoanStatusNotificationRequest").buildPost(Entity.xml(request));
        try {
            if (isAsync) {
                Future<LoanStatusNotificationResponse> futureResponse = invocation.submit(LoanStatusNotificationResponse.class);
                return futureResponse.get();
            } else {
                return invocation.invoke(LoanStatusNotificationResponse.class);
            }
        } finally {
            client.close();
        }
    }

//    @POST
//    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
//    @Produces(MediaType.TEXT_XML)
    public LoanApplicationCreationResponse handle(LoanApplicationCreationRequest request) throws Exception {
        LoanApplicationCreationResponse response = new LoanApplicationCreationResponse();
        fillCommonResponse(request, response);
        determineApplicationResult(request, response);
        return response;
    }

//    @POST
//    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
//    @Produces(MediaType.TEXT_XML)
    public LoanConfirmationResponse handle(LoanConfirmationRequest request) throws Exception {
        LoanConfirmationResponse response = new LoanConfirmationResponse();
        fillCommonResponse(request, response);
        determineConfirmationResult(request, response);
        return response;
    }

//    @POST
//    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
//    @Produces(MediaType.TEXT_XML)
    public LoanAdjustmentResponse handle(LoanAdjustmentRequest request) throws Exception {
        LoanAdjustmentResponse response = new LoanAdjustmentResponse();
        fillCommonResponse(request, response);
        determineAdjustmentResult(request, response);
        return response;
    }

//    @POST
//    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
//    @Produces(MediaType.TEXT_XML)
    public LoanChargeResponse handle(LoanChargeRequest request) throws Exception {
        LoanChargeResponse response = new LoanChargeResponse();
        fillCommonResponse(request, response);
        determineChargeResult(request, response);
        return response;
    }

    private void determineApplicationResult(LoanApplicationCreationRequest request, LoanApplicationCreationResponse response) {
        if(request.getCustomer() == null || request.getCustomer().getFamilyName() == null ) {
            response.setResponseResult(ResponseResultType.PROCESSING_ERROR);
            response.setResponseResultCode(300);
            response.setResponseResultDescription("Missing customer info or customer's family name");
        }
        else if(request.getCustomer().getFamilyName().endsWith("AA")) {
            response.setResponseResult(ResponseResultType.ACCEPTED);
            response.setResponseResultCode(0);
            response.setResponseResultDescription("All OK!");
        }
        else if(request.getCustomer().getFamilyName().endsWith("DD")) {
            response.setResponseResult(ResponseResultType.REJECTED);
            response.setResponseResultCode(100);
            response.setResponseResultDescription("Rejected");
        }
        else {
            response.setResponseResult(ResponseResultType.PENDING);
            response.setResponseResultCode(200);
            response.setResponseResultDescription("Pending");
        }
    }

    private void determineConfirmationResult(LoanConfirmationRequest request, LoanConfirmationResponse response) {
        response.setExternalLoanId(request.getExternalLoanId());
        response.setResponseResult(ResponseResultType.ACCEPTED);
        response.setResponseResultCode(0);
        response.setResponseResultDescription("All OK!");
    }

    private void determineAdjustmentResult(LoanAdjustmentRequest request, LoanAdjustmentResponse response) {
        response.setResponseResult(ResponseResultType.ACCEPTED);
        response.setResponseResultCode(0);
        response.setResponseResultDescription("All OK!");
    }

    private void determineChargeResult(LoanChargeRequest request, LoanChargeResponse response) {
        response.setResponseResult(ResponseResultType.ACCEPTED);
        response.setResponseResultCode(0);
        response.setResponseResultDescription("All OK!");
    }

    public static Client IgnoreSSLClient() throws Exception {
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }

        }}, new java.security.SecureRandom());
        return ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true).build();
    }
}
