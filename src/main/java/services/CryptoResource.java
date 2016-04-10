package services;

import crypto.Cryptor;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by liang on 4/7/2016.
 */
@Path("/")
public class CryptoResource {
    private Cryptor cryptor;
    public CryptoResource() throws Exception {
        cryptor = new Cryptor();
    }

    @POST
    //@Consumes("application/xml")
    @Path("encrypt")
    public StreamingOutput encrypt(InputStream inputStream) throws Exception {
        String plainText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        String cipherText = cryptor.encrypt(plainText);
        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException
            {
                PrintStream writer = new PrintStream(outputStream);
                writer.println(cipherText);

            }
        };
    }

    @POST
    //@Consumes("application/xml")
    @Path("decrypt")
    public StreamingOutput decrypt(InputStream inputStream) throws Exception {
        String cipherText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        String plainText = cryptor.decrypt(cipherText);
        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException
            {
                PrintStream writer = new PrintStream(outputStream);
                writer.println(plainText);
            }
        };
    }

}