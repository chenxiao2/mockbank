package app.client;


import app.filter.Logged;
import com.amazon.payments.globalinstallmentlending.protocol.v1.*;
import io.swagger.annotations.Api;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static app.util.GILFranceUtil.fillCommonResponse;

@Api
@Path("client/gil/fr")
@Named
public class ClientGILFranceResource {

    @POST
    @Logged
    @Consumes({MediaType.APPLICATION_XML,MediaType.TEXT_XML})
    @Produces(MediaType.APPLICATION_XML)
    public LoanStatusNotificationResponse handle(LoanStatusNotificationRequest request) throws Exception {
        LoanStatusNotificationResponse response = new LoanStatusNotificationResponse();
        fillCommonResponse(request, response);
        response.setResponseResult(ResponseResultType.ACCEPTED);
        response.setResponseResultCode(0);
        response.setResponseResultDescription("All OK!");
        return response;
    }
}
