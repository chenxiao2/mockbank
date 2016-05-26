package app.filter;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by liazhang on 5/23/16.
 */
@Slf4j
@Named
@Provider
public class ClientRequestLoggingFilter implements ClientRequestFilter{
    @Override
    public void filter(ClientRequestContext ctx) throws IOException {
        log.info("========== Client Request ==========");
        log.info(ctx.getUri().toString());
        log.info(ctx.getMethod());
        ctx.getHeaders().forEach((key, values) -> log.info(String.join(":", key, values.toString())));

//        StringBuilder b = new StringBuilder();
//        final OutputStream stream = new LoggingStream(b, ctx.getEntityStream());
//        ctx.setEntityStream(stream);
//        log.info(b.toString());
//        OutputStream originalStream = ctx.getEntityStream();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ctx.setEntityStream(baos);
//
//        log.info(baos.toString(StandardCharsets.UTF_8.name()));
//        baos.writeTo(originalStream);
//        baos.close();
//        ctx.setEntityStream(originalStream);
    }

    private static class LoggingStream extends FilterOutputStream {

        private final StringBuilder b;
        private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        LoggingStream(final StringBuilder b, final OutputStream inner) {
            super(inner);

            this.b = b;
        }

        StringBuilder getStringBuilder(final Charset charset) {
            // write entity to the builder
            final byte[] entity = baos.toByteArray();

            b.append(new String(entity, 0, entity.length, charset));
            b.append('\n');
            return b;
        }

        @Override
        public void write(final int i) throws IOException {
            baos.write(i);
            out.write(i);
        }
    }
}

