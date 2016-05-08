package app;

import com.amazon.payments.globalinstallmentlending.protocol.v1.*;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.Future;

import static app.util.GILFranceUtil.fillCommonResponse;

/**
 * Created by liang on 4/10/2016.
 */
@Path("gil/fr")
@Named
public class GILFranceResource {

    @POST
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
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    @Produces(MediaType.TEXT_XML)
    public LoanStatusNotificationResponse handle(@HeaderParam("X-Mockbank-NotificationEndpoint") String clientEndpoint,
                                                 @HeaderParam("X-Mockbank-Async") boolean isAsync,
                                                 LoanStatusNotificationRequest request) throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(clientEndpoint);
        Invocation invocation = webTarget.request().buildPost(Entity.xml(request));
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
}
