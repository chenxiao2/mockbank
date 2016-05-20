package app.interceptor;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by liazhang on 5/20/16.
 */
@Slf4j
@Named
@Provider
public class ResponseLoggingInterceptor implements WriterInterceptor {
    @Override
    public void aroundWriteTo(WriterInterceptorContext ctx) throws IOException, WebApplicationException {
        OutputStream originalStream = ctx.getOutputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ctx.setOutputStream(baos);
        try {
            ctx.proceed();
        } finally {
            log.info(baos.toString(StandardCharsets.UTF_8.name()));
            baos.writeTo(originalStream);
            baos.close();
            ctx.setOutputStream(originalStream);
        }
    }
}
