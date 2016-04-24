package app;

import com.amazon.payments.globalinstallmentlending.protocol.v1.LoanApplicationCreationRequest;
import com.amazon.payments.globalinstallmentlending.protocol.v1.LoanApplicationCreationResponse;
import com.amazon.payments.globalinstallmentlending.protocol.v1.ResponseResultType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by liang on 4/24/2016.
 */
public class GILFranceResourceTest {
    private static String URL = "http://localhost:8080/gil/fr";
    private Client client;
    private WebTarget webTarget;
    private Unmarshaller unmarshaller;
    @Before
    public void setUp() throws SAXException, JAXBException {
        client = ClientBuilder.newClient();
        webTarget = client.target(URL);

        //create JAXB context
        JAXBContext jaxbContext = JAXBContext.newInstance("com.amazon.payments.globalinstallmentlending.protocol.v1");
        //create XML Schema object
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("src/main/resources/configuration/schemas/gil_fr.xsd"));

        unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void testLoanApplicationCreation() throws FileNotFoundException, JAXBException {

        InputStream is = new FileInputStream("src/main/resources/configuration/schemas/4CB_Examples/4CB_Step1_loanApplicationCreationRequest.xml");
        LoanApplicationCreationRequest request = (LoanApplicationCreationRequest)unmarshaller.unmarshal(is);
        LoanApplicationCreationResponse response =
                webTarget.request().post(Entity.xml(request), LoanApplicationCreationResponse.class);
        assertEquals(request.getMarketplaceId(),response.getMarketplaceId());
        assertEquals(request.getMerchantId(),response.getMerchantId());
        assertEquals(ResponseResultType.PENDING,response.getResponseResult());
    }
}
