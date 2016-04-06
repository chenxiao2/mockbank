import java.security.Provider;
import java.security.Security;
import java.util.Set;

import static java.lang.String.join;

/**
 * Created by liazhang on 4/5/16.
 */
public class Main {

    public static void main(String[] args) {
        listProviders();

    }

    private static void listProviders() {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println(join(":::", provider.getName(), String.valueOf(provider.getVersion()), provider.getInfo()));
            provider.getServices();
        }
    }

    private static void listServiceFromJCE() {
        Provider jce = Security.getProvider("SunJCE");
        Set<Provider.Service> services =  jce.getServices();
        for(Provider.Service service : services) {
            System.out.println(join(":::", service.getAlgorithm(), service.getType()));
        }
    }
}
