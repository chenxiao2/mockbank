package app.xop;

import org.jboss.resteasy.annotations.providers.multipart.XopWithMultipartRelated;
import org.jboss.resteasy.plugins.providers.multipart.MultipartConstants;

import javax.ws.rs.*;

/**
 * Created by liazhang on 4/29/16.
 */

@Path("mime")
public interface XopInterface {
    @Path("xop")
    @PUT
    @Consumes(MultipartConstants.MULTIPART_RELATED)
    @Produces(MultipartConstants.MULTIPART_RELATED)
    public @XopWithMultipartRelated XopData putXop(@XopWithMultipartRelated XopData bean);

    @Path("xop")
    @GET
    @Produces(MultipartConstants.MULTIPART_RELATED)
    public @XopWithMultipartRelated XopData getXop();
}