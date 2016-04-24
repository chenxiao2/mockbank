package app;

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
 * Encryption and decryption per <a href="https://www.w3.org/TR/2002/REC-xmlenc-core-20021210/Overview.html">
 *     XML Encryption Syntax and Processing</a> standard.
 */
@Path("xml")
public class XMLEncryptionResource {
    private Cryptor cryptor;
    public XMLEncryptionResource() throws Exception {

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
