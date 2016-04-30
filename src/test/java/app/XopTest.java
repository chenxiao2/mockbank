package app;

import app.xop.Customer;
import app.xop.XopData;
import app.xop.XopInterface;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by liazhang on 4/29/16.
 */
public class XopTest {

    private Client client;
    private XopInterface xopInterface;

    @Before
    public void setUp() {
        client = ClientBuilder.newClient();
        ResteasyWebTarget target = (ResteasyWebTarget)client.target("http://localhost:8080/");
        xopInterface = target.proxy(XopInterface.class);
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void testGet() {
        XopData xopData = xopInterface.getXop();
        assertEquals(xopData.getBill().getName(), "bill");
        assertEquals(xopData.getMonica().getName(), "monica");
        String both = new String(xopData.getMyBinary());
        assertEquals(both, "bill and monica");

    }

    @Test
    public void testPut() {
        XopData request = new XopData();
        Customer bill = new Customer("bill");
        request.setBill(bill);
        Customer monica = new Customer("monica");
        request.setMonica(monica);
        String billAndMonica = "bill and monica";
        request.setMyBinary(billAndMonica.getBytes());

        XopData response = xopInterface.putXop(request);

        assertNotNull(response);
        assertEquals(response.getBill(), monica);
        assertEquals(response.getMonica(), bill);
        assertEquals(billAndMonica, new String(response.getMyBinary()));
    }
}
