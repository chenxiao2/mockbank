package app;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.junit.After;
import org.junit.Before;
import org.xml.sax.SAXException;

public class UKCBCCResourceTest {
    private static String URL = "http://localhost:8080/cbcc/uk";
    private Client client;
    private WebTarget webTarget;
    private Unmarshaller unmarshaller;
    @Before
    public void setUp() throws SAXException, JAXBException {
        client = ClientBuilder.newClient();
        webTarget = client.target(URL);

        //create JAXB context
        JAXBContext jaxbContext = JAXBContext.newInstance("com.amazon.payments.cobrandcreditcard.protocol.v1");
        //create XML Schema object
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("src/main/resources/configuration/schemas/UK-CBCC-online.xsd"));

        unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
    }

    @After
    public void tearDown() {
        client.close();
    }

}
