package services;

import com.amazon.payments.globalinstallmentlending.protocol.v1.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.InputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

/**
 * Created by liang on 4/10/2016.
 */
@Path("/")
public class LoanApplicationResource {

    @POST
    @Consumes({MediaType.APPLICATION_XML,"text/xml"})
    @Produces(MediaType.TEXT_XML)
    @Path("")
    public LoanApplicationCreationResponse handle(LoanApplicationCreationRequest request) throws Exception {
        LoanApplicationCreationResponse response = new LoanApplicationCreationResponse();
        fillCommonResponse(request, response);
        determineApplicationResult(request, response);
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
        else if(request.getCustomer().getFamilyName().endsWith("RR")) {
            response.setResponseResult(ResponseResultType.PENDING);
            response.setResponseResultCode(200);
            response.setResponseResultDescription("Pending");
        }
        else if(request.getCustomer().getFamilyName().endsWith("DD")) {
            response.setResponseResult(ResponseResultType.REJECTED);
            response.setResponseResultCode(100);
            response.setResponseResultDescription("Rejected");
        }
    }
}
