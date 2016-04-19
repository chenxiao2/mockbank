package keystore;


import java.io.*;
import java.security.*;
import java.security.cert.*;

public class KeyStoreHandler {
    KeyStore ks;
    private char[] pw;

    // We'll use this to look up the keystore in the default location.
    // You can specify a password if you like, but this will also
    // work if you pass null (in which case the keystore isn't
    // verified).
    public KeyStoreHandler(char[] pw) {
        // Make a private copy so the original can be collected so
        // that other objects can't locate it.
        if (pw != null) {
            this.pw = new char[pw.length];
            System.arraycopy(pw, 0, this.pw, 0, pw.length);
        }
        else this.pw = null;
        // Load from the default location
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType(  ));
            String fname = System.getProperty("user.home") +
                    File.separator + ".keystore";
            FileInputStream fis = new FileInputStream(fname);
            ks.load(fis, pw);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.toString(  ));
        }
    }

    public KeyStore getKeyStore(  ) {
        return ks;
    }

    // Store to the default location
    public void store(  ) throws FileNotFoundException,
            KeyStoreException, IOException,
            NoSuchAlgorithmException,
            CertificateException {
        // If we didn't read with a password, we can't store
        if (pw == null) {
            throw new IllegalArgumentException("Can't store w/o pw");
        }
        FileOutputStream fos = new FileOutputStream(
                System.getProperty("user.home") +
                        File.separator + ".keystore");
        ks.store(fos, pw);
        fos.close(  );
    }

    public static void main(String args[]) {
        try {
            KeyStore ks = new KeyStoreHandler(null).getKeyStore(  );
            if (ks.isKeyEntry(args[0])) {
                System.out.println(args[0] +
                        " is a key entry in the keystore");
                char c[] = new char[args[1].length(  )];
                args[1].getChars(0, c.length, c, 0);
                System.out.println("The private key for " + args[0] +
                        " is " + ks.getKey(args[0], c));
                java.security.cert.Certificate certs[] =
                        ks.getCertificateChain(args[0]);
                if (certs[0] instanceof X509Certificate) {
                    X509Certificate x509 = (X509Certificate) certs[0];
                    System.out.println(args[0] + " is really " +
                            x509.getSubjectDN(  ));
                }
                if (certs[certs.length - 1] instanceof
                        X509Certificate) {
                    X509Certificate x509 = (X509Certificate)
                            certs[certs.length - 1];
                    System.out.println(args[0] + " was verified by " +
                            x509.getIssuerDN(  ));
                }
            }
            else if (ks.isCertificateEntry(args[0])) {
                System.out.println(args[0] +
                        " is a certificate entry in the keystore");
                java.security.cert.Certificate c =
                        ks.getCertificate(args[0]);
                if (c instanceof X509Certificate) {
                    X509Certificate x509 = (X509Certificate) c;
                    System.out.println(args[0] + " is really " +
                            x509.getSubjectDN(  ));
                    System.out.println(args[0] + " was verified by " +
                            x509.getIssuerDN(  ));
                }
            }
            else {
                System.out.println(args[0] +
                        " is unknown to this keystore");
            }
        } catch (Exception e) {
            e.printStackTrace(  );
        }
    }
}
