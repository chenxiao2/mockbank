package app.util;

import com.amazon.payments.globalinstallmentlending.protocol.v1.Request;
import com.amazon.payments.globalinstallmentlending.protocol.v1.Response;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

/**
 * Created by liang on 4/24/2016.
 */
public class GILFranceUtil {

    public static void fillCommonResponse(Request request, Response response) throws DatatypeConfigurationException {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        response.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(utc)));
        response.setSystemId(request.getSystemId());
        response.setTransactionId(request.getTransactionId());
        response.setRequestId(request.getRequestId());
        response.setMerchantId(request.getMerchantId());
        response.setMarketplaceId(request.getMarketplaceId());
        response.setVersion("1");
    }
}
