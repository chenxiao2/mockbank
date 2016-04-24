package app;

import com.amazon.payments.globalinstallmentlending.protocol.v1.LoanApplicationCreationRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by liang on 4/24/2016.
 */
public class XMLEncryptionResourceTest {

    private static String URL_ENCRYPT = "http://localhost:8080/xml/encrypt";
    private static String URL_DECRYPT = "http://localhost:8080/xml/decrypt";
    private Client client;
    private Unmarshaller unmarshaller;
    @Before
    public void setUp() throws SAXException, JAXBException {
        client = ClientBuilder.newClient();

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
    public void testEncryptAndDecrypt() throws JAXBException, FileNotFoundException {
        InputStream is = new FileInputStream("src/main/resources/configuration/schemas/4CB_Examples/4CB_Step1_loanApplicationCreationRequest.xml");
        LoanApplicationCreationRequest request = (LoanApplicationCreationRequest)unmarshaller.unmarshal(is);
        Response encryptedResponse = client.target(URL_ENCRYPT).request().post(Entity.xml(request));
        assertEquals(200, encryptedResponse.getStatus());
        String encryptedPayload = encryptedResponse.readEntity(String.class);

        Response decryptionResponse =
                client.target(URL_DECRYPT).request().
                        post(Entity.entity(encryptedPayload, MediaType.TEXT_PLAIN));
        assertEquals(200, decryptionResponse.getStatus());
        String decryptedPayload = decryptionResponse.readEntity(String.class);
        LoanApplicationCreationRequest request2 =
                (LoanApplicationCreationRequest) unmarshaller.unmarshal(new StringReader(decryptedPayload));

        assertEquals(request.getLoanApplicationId(), request2.getLoanApplicationId());
    }
}
