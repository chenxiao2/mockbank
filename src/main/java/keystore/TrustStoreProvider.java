package keystore;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import javax.inject.Provider;

/**
 * Created by liazhang on 5/24/16.
 */
@Slf4j
public class TrustStoreProvider implements Provider<KeyStore> {

    private static final String TRUST_STORE = "truststore";

    private static final String PASSWORD = "password";

    @Override
    public KeyStore get() {

        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            log.error("Failed to create a trust store instance: ", e);
        }
        try (FileInputStream fileInputStream = new FileInputStream(TRUST_STORE)) {
//            trustStore.load(fileInputStream, PASSWORD.toCharArray());
            trustStore.load(fileInputStream, null);
        } catch (CertificateException|NoSuchAlgorithmException|IOException e) {
            log.error("Failed to load the trust store: ", e);
        }
        Enumeration<String> aliases = null;
        try {
            aliases = trustStore.aliases();
        } catch (KeyStoreException e) {
            log.error("Failed to get aliases from the trust store: ", e);
        }
        while(aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            log.info("alias from trust store: " + alias);
            KeyStore.TrustedCertificateEntry entry = null;
            try {
                entry = (KeyStore.TrustedCertificateEntry) trustStore.getEntry(alias, null);
            } catch (NoSuchAlgorithmException|UnrecoverableEntryException|KeyStoreException e) {
                log.error("Failed to get alias entry from the trust store: ", e);
            }
            Certificate cert = entry.getTrustedCertificate();
            log.info(cert.toString());
        }
        return trustStore;
    }
}
