package app.interceptor;

import app.filter.Logged;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by liazhang on 5/20/16.
 */
@Slf4j
@Named
@Provider
public class RequestLoggingInterceptor implements ReaderInterceptor {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext ctx) throws IOException, WebApplicationException {
        InputStream input = ctx.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = input.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        InputStream is1 = new ByteArrayInputStream(baos.toByteArray());
        log.info(IOUtils.toString(is1, StandardCharsets.UTF_8));
        is1.reset();
        ctx.setInputStream(is1);
        return ctx.proceed();
    }
}
