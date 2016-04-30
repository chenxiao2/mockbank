package app.xop;

import org.jboss.resteasy.annotations.providers.multipart.XopWithMultipartRelated;
import org.springframework.stereotype.Component;

/**
 * Created by liazhang on 4/29/16.
 */
@Component
public class XopService implements XopInterface {
    @Override
    public XopData putXop(@XopWithMultipartRelated XopData bean) {
        XopData response = new XopData();
        response.setBill(bean.getMonica());
        response.setMonica(bean.getBill());
        response.setMyBinary(bean.getMyBinary());
        return response;
    }

    @Override
    public XopData getXop() {
        XopData response = new XopData();
        response.setBill(new Customer("bill"));
        response.setMonica(new Customer("monica"));
        response.setMyBinary("bill and monica".getBytes());
        return response;
    }
}
