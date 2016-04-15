package crypto;

import com.google.common.primitives.Bytes;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;

import static java.lang.String.join;

public class Main {

    public static void main(String[] args) throws Exception {
        addBouncyCastle();
        //listProviders();
        //encryptWithBC("plaintext");
        //printUUIDtoString();
        String xmlString = readTextFromFile("src/main/resources/configuration/schemas/4CB_Examples/4CB_Step1_loanApplicationCreationRequest.xml");
//        System.out.println("Length of xml string: " + xmlString.length());
//        System.out.println(messageDigest(xmlString));
////        encryptWithRSAOAEP(xmlString);
//        Cryptor cryptor = new Cryptor();
//        KeyPair keyPair = cryptor.generateRSAKey(2048);
//        writeRSAKey(keyPair);
//        long start = System.currentTimeMillis();
//        CipherKeyAndData cipherKeyAndData = cryptor.encrypt(xmlString, keyPair.getPublic());
//        long timeOfEncrytion = System.currentTimeMillis() - start;
//        String plaintext = cryptor.decrypt(cipherKeyAndData, keyPair.getPrivate());
//        long timeOfDecryption = System.currentTimeMillis() -start - timeOfEncrytion;
//        System.out.println("TimeOfEncryption: " + timeOfEncrytion);
//        System.out.println("TimeOfDecryption: " + timeOfDecryption);
//        System.out.println(messageDigest(plaintext));
//        System.out.println(plaintext);

//        String encryptedString = readTextFromFile("src/main/resources/encryption/EncryptedLoanApplicationCreationRequest.xml");
        PrivateKey privKey = Cryptor.readPrivateKey("rsa.pk8");
        System.out.println(String.join(":::", privKey.getAlgorithm(), privKey.getFormat()));

        PublicKey pubKey = Cryptor.readPublickey("rsa.x509");
        System.out.println(String.join(":::", pubKey.getAlgorithm(), pubKey.getFormat()));

//        Cryptor cryptor = new Cryptor(pubKey, privKey);
        Cryptor cryptor = new Cryptor();
        String encryptedString = cryptor.encrypt(xmlString);
        String xml = cryptor.decrypt(encryptedString);
        System.out.println(xml);

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
        final String keyTransformation = "RSA/NONE/OAEPWithSHA256AndMGF1Padding";
        //Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        Cipher rsa = Cipher.getInstance(keyTransformation);
        System.out.println("Key Cipher algorithm: " + rsa.getAlgorithm());
        System.out.println("Key Cipher provide name: " + rsa.getProvider().getName() + " " + rsa.getProvider().getVersion());

        final String dataTransformation = "AES/CBC/PKCS5Padding";
        Cipher aes = Cipher.getInstance(dataTransformation);
        System.out.println("Data Cipher algorithm: " + aes.getAlgorithm());
        System.out.println("Data Cipher provide name: " + aes.getProvider().getName() + " " + aes.getProvider().getVersion());

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());

        KeyPair pair = generator.generateKeyPair();
        PublicKey pubKey = pair.getPublic();
        PrivateKey privKey = pair.getPrivate();

        KeyGenerator secretGenerator = KeyGenerator.getInstance("AES");
        secretGenerator.init(128);
        SecretKey secretKey = secretGenerator.generateKey();
        System.out.println("Algorithm: " + secretKey.getAlgorithm() + " Format: " + secretKey.getFormat()
        + " Encoding length: " + secretKey.getEncoded().length);

        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        aes.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherDataBytes = aes.doFinal(plaintext.getBytes());
        String cipherDataString = encoder.encodeToString(cipherDataBytes);
        System.out.println("Length of cipher data: " + cipherDataString.length());
        System.out.println(cipherDataString);

        rsa.init(Cipher.ENCRYPT_MODE,pubKey);
        byte[] iv = aes.getIV();
        System.out.println("IV length: " + iv.length);
        byte[] cipherKeyBytes = rsa.doFinal(secretKey.getEncoded());
        byte[] ivAndCipherKeyBytes = Bytes.concat(iv, cipherKeyBytes);
        String ivAndCipherKeyString = encoder.encodeToString(ivAndCipherKeyBytes);
        System.out.println("Length of cipher key: " + ivAndCipherKeyString.length());
        System.out.println(ivAndCipherKeyString);

        byte[] ivAndCipherKeyBytes2 = decoder.decode(ivAndCipherKeyString);
        rsa.init(Cipher.DECRYPT_MODE,privKey);
        byte[] plainKeyBytes = rsa.doFinal(ivAndCipherKeyBytes2, 16, ivAndCipherKeyBytes2.length-16);

        SecretKeySpec secretKeySpec = new SecretKeySpec(plainKeyBytes, "AES");

        aes.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(ivAndCipherKeyBytes2,0, 16));
        byte[] plainDataBytes = aes.doFinal(decoder.decode(cipherDataString));
        String plainDataString = new String(plainDataBytes);
        System.out.println(plainDataString.length());
        System.out.println(messageDigest(plainDataString));
        System.out.println(plainDataString);

    }

    private static String messageDigest(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] output = md.digest(input.getBytes());
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(output);

    }
    private static void printUUIDtoString() {
        System.out.println(UUID.randomUUID().toString());
    }

    static String readTextFromFile(String path) throws Exception {
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
