import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.join;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {

    public static void main(String[] args) throws Exception {
        addBouncyCastle();
        //listProviders();
        //encryptWithBC("plaintext");
        //printUUIDtoString();
        String xmlString = readXMLtoString("/Users/liazhang/code/jca/src/main/resources/configuration/schemas/4CB_Examples/4CB_Step1_loanApplicationCreationRequest.xml");
        encryptWithRSAOAEP(xmlString);
        //System.out.println();
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

    private static void encryptWithRSAOAEP(String plaintext) throws Exception {
        ////OAEPWithSHA256AndMGF1Padding
        //Cipher rsaoaep = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
        //Cipher rsaoaep = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        Cipher rsaoaep = Cipher.getInstance("RSA");
        System.out.println("Cipher algorithm: " + rsaoaep.getAlgorithm());
        System.out.println("Cipher provide name: " + rsaoaep.getProvider().getName() + " " + rsaoaep.getProvider().getVersion());

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());

        KeyPair pair = generator.generateKeyPair();
        PublicKey pubKey = pair.getPublic();
        PrivateKey privKey = pair.getPrivate();

        rsaoaep.init(Cipher.ENCRYPT_MODE,pubKey);
        byte[] cipherText = rsaoaep.doFinal(plaintext.getBytes());
        Base64.Encoder encoder = Base64.getEncoder();
        String cipherTextString = encoder.encodeToString(cipherText);
        System.out.println(cipherTextString);

        rsaoaep.init(Cipher.DECRYPT_MODE,privKey);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] plaintext2Bytes = rsaoaep.doFinal(decoder.decode(cipherTextString));
        String plaintext2String = new String(plaintext2Bytes);
        System.out.println(plaintext2String);

    }

    private static void printUUIDtoString() {
        System.out.println(UUID.randomUUID().toString());
    }

    private static String readXMLtoString(String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))){
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

        }

        return sb.toString();
    }
}
