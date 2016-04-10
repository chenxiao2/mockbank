package jaxb;

import com.amazon.payments.globalinstallmentlending.protocol.v1.LoanApplicationCreationRequest;
import com.amazon.payments.globalinstallmentlending.protocol.v1.LoanApplicationCreationResponse;
import com.amazon.payments.globalinstallmentlending.protocol.v1.ObjectFactory;
import com.amazon.payments.globalinstallmentlending.protocol.v1.ResponseResultType;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

/**
 * Created by liang on 4/10/2016.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        //create JAXB context
        JAXBContext jaxbContext = JAXBContext.newInstance("com.amazon.payments.globalinstallmentlending.protocol.v1");
        //create XML Schema object
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("src/main/resources/configuration/schemas/gil_fr.xsd"));

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        InputStream is = new FileInputStream("src/main/resources/configuration/schemas/4CB_Examples/4CB_Step1_loanApplicationCreationRequest.xml");
        LoanApplicationCreationRequest request = (LoanApplicationCreationRequest)unmarshaller.unmarshal(is);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setSchema(schema);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://payments.amazon.com/globalinstallmentlending/protocol/v1 ../gil_fr.xsd" );
        marshaller.marshal(handle(request), System.out);

    }

    private static LoanApplicationCreationResponse handle(LoanApplicationCreationRequest request) throws Exception {
        ObjectFactory objectFactory = new ObjectFactory();
        LoanApplicationCreationResponse response = objectFactory.createLoanApplicationCreationResponse();
        //common properties, to be refactored
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        response.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(utc)));
        response.setSystemId(request.getSystemId());
        response.setTransactionId(request.getTransactionId());
        response.setRequestId(request.getRequestId());
        response.setMerchantId(request.getMerchantId());
        response.setMarketplaceId(request.getMarketplaceId());
        response.setVersion("1");
        //response result, to be refactored to generate different response result based on request.
        response.setResponseResult(ResponseResultType.ACCEPTED);
        response.setResponseResultCode(0);
        response.setResponseResultDescription("All Ok.");

        return response;
    }
}
