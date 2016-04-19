package keystore;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Enumeration;

/**
 * Created by liang on 4/19/2016.
 */
public class Main {
    public static final String PASSWORD = "password";

    public static void main(String[] args) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fileInputStream = new FileInputStream("keystore")) {
            keyStore.load(fileInputStream, PASSWORD.toCharArray());
        }
        Enumeration<String> aliases = keyStore.aliases();
        while(aliases.hasMoreElements()) {
            System.out.println(aliases.nextElement());
        }
        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(PASSWORD.toCharArray());
        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("liang", protParam);

        PrivateKey privKey = pkEntry.getPrivateKey();
        System.out.println("PrivateKey: " + privKey.getAlgorithm() + ":::" + privKey.getFormat());

        Certificate[] certs = pkEntry.getCertificateChain();
        System.out.println("Cert length: " + certs.length);
        System.out.println(certs[0].getType());
        PublicKey pubKey = certs[0].getPublicKey();
        System.out.println("PublicKey: " + pubKey.getAlgorithm() + ":::" + pubKey.getFormat());
    }
}
