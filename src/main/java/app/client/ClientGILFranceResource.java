package app.client;


import com.amazon.payments.globalinstallmentlending.protocol.v1.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static app.util.GILFranceUtil.fillCommonResponse;

@Path("gil/fr")
public class ClientGILFranceResource {

    @POST
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
