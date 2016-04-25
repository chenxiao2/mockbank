package app;

import com.amazon.payments.globalinstallmentlending.protocol.v1.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

/**
 * Created by liang on 4/10/2016.
 */
@Path("gil/fr")
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

    private void fillCommonResponse(Request request, Response response) throws DatatypeConfigurationException {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        response.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(utc)));
        response.setSystemId(request.getSystemId());
        response.setTransactionId(request.getTransactionId());
        response.setRequestId(request.getRequestId());
        response.setMerchantId(request.getMerchantId());
        response.setMarketplaceId(request.getMarketplaceId());
        response.setVersion("1");
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
