package app.client;


import app.filter.Logged;
import app.util.GILFranceUtil;
import app.util.UKCBCCUtil;
import com.amazon.payments.cobrandcreditcard.protocol.v1.ApplicationStateChangeRequest;
import com.amazon.payments.cobrandcreditcard.protocol.v1.ApplicationStateChangeResponse;
import com.amazon.payments.cobrandcreditcard.protocol.v1.ResponseResult;
import com.amazon.payments.globalinstallmentlending.protocol.v1.LoanStatusNotificationRequest;
import com.amazon.payments.globalinstallmentlending.protocol.v1.LoanStatusNotificationResponse;
import com.amazon.payments.globalinstallmentlending.protocol.v1.ResponseResultType;
import io.swagger.annotations.Api;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api
@Path("/client")
@Named
public class ClientResource {

    @Path("gil/fr")
    @POST
    @Logged
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    @Produces(MediaType.APPLICATION_XML)
    public LoanStatusNotificationResponse handle(LoanStatusNotificationRequest request) throws Exception {
        LoanStatusNotificationResponse response = new LoanStatusNotificationResponse();
        GILFranceUtil.fillCommonResponse(request, response);
        response.setResponseResult(ResponseResultType.ACCEPTED);
        response.setResponseResultCode(0);
        response.setResponseResultDescription("All OK!");
        return response;
    }

    @Path("cbcc/uk")
    @POST
    @Logged
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    @Produces(MediaType.APPLICATION_XML)
    public ApplicationStateChangeResponse handle(ApplicationStateChangeRequest request) throws Exception {
        ApplicationStateChangeResponse response = new ApplicationStateChangeResponse();
        UKCBCCUtil.fillCommonResponse(request,response);
        response.setResponseResult(ResponseResult.ACCEPTED);
        response.setResponseResultCode(0);
        response.setResponseResultDescription("All OK!");
        return response;
    }
}
