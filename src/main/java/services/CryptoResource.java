package services;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by liang on 4/7/2016.
 */
@Path("/")
public class CryptoResource {
    @POST
    //@Consumes("application/xml")
    @Path("encrypt")
    public StreamingOutput encrypt(InputStream inputStream) {

        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException
            {
                PrintStream writer = new PrintStream(outputStream);
                writer.println("hello");

            }
        };
    }

}
