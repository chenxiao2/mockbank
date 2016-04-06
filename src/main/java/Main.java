import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Set;

import static java.lang.String.join;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {

    public static void main(String[] args) {
        addBouncyCastle();
        //listProviders();
        encryptWithBC("plaintext");
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
        Set<Provider.Service> services = jce.getServices();
        for (Provider.Service service : services) {
            System.out.println(join(":::", service.getAlgorithm(), service.getType()));
        }
    }

    private static void addBouncyCastle() {
        Provider bcprov = new BouncyCastleProvider();
        int precedenceOrder = Security.addProvider(bcprov);
        System.out.println("added  " + bcprov.getName() + " " + bcprov.getVersion() + " as precedence " + precedenceOrder);
    }

    private static void encryptWithBC(String plaintext) {
        // This will generate a random key, and encrypt the data
        Cipher encrypt = null;
        try {
            encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding", "BC");
            System.out.println("Cipher algorithm: " + encrypt.getAlgorithm());
            System.out.println("Cipher provide name: " + encrypt.getProvider().getName() + " " + encrypt.getProvider().getVersion());
        } catch (NoSuchAlgorithmException|NoSuchProviderException|NoSuchPaddingException e) {
            e.printStackTrace();
        }

        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("DES", "BC");         // "BC" is the name of the BouncyCastle provider
        } catch (NoSuchAlgorithmException|NoSuchProviderException e) {
            e.printStackTrace();
        }
        keyGen.init(new SecureRandom());

        Key key = keyGen.generateKey();


        try {
            encrypt.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        OutputStream cOut = new CipherOutputStream(bout, encrypt);

        try {
            cOut.write(plaintext.getBytes());
            cOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Hex.encodeHexString(bout.toByteArray()));
        // bOut now contains the cipher text
    }
}
