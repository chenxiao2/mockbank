package crypto;

import com.google.common.primitives.Bytes;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.security.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liang on 4/8/2016.
 */
public class Cryptor {

    private KeyPair keyPair;

    public Cryptor() throws Exception {
        addBouncyCastle();
        this.keyPair = generateRSAKey(2048);
        writeRSAKey();
    }



    public String encrypt(String plaintext) throws Exception {
        CipherKeyAndData cipherKeyAndData = encrypt(plaintext, keyPair.getPublic());
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<EncryptedData xmlns=\"http://www.w3.org/2001/04/xmlenc#\"\n");
        sb.append("               xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"\n");
        sb.append("               MimeType=\"application/xml\">\n");
        sb.append("  <EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes128-cbc\"/>\n");
        sb.append("  <ds:KeyInfo>\n");
        sb.append("    <EncryptedKey>\n");
        sb.append("      <EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\"/>\n");
        sb.append("        <CipherData>\n");
        sb.append("          <CipherValue>" + cipherKeyAndData.getCipherKey() +"</CipherValue>\n");
        sb.append("        </CipherData>\n");
        sb.append("    </EncryptedKey>\n");
        sb.append("  </ds:KeyInfo>\n");
        sb.append("  <CipherData>\n");
        sb.append("    <CipherValue>" + cipherKeyAndData.getCipherData() + "</CipherValue>\n");
        sb.append("  </CipherData>\n");
        sb.append("</EncryptedData>\n");
        return sb.toString();
    }

    public String decrypt(String ciphertext) throws Exception {
        final String pattern = ".*<CipherValue>(.*)</CipherValue>.*<CipherValue>(.*)</CipherValue>.*";
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher matcher = p.matcher(ciphertext);
        if (!matcher.matches()) {
            return "Invalid XML Enc format";
        }
        CipherKeyAndData cipherKeyAndData = new CipherKeyAndData(matcher.group(1), matcher.group(2));
        return decrypt(cipherKeyAndData, keyPair.getPrivate());
    }

    CipherKeyAndData encrypt(String plaintext, PublicKey pubKey) throws Exception {
//        long start = System.currentTimeMillis();
        final String keyTransformation = "RSA/NONE/OAEPWithSHA256AndMGF1Padding";
        Cipher rsa = Cipher.getInstance(keyTransformation);
//        System.out.println("Key Cipher algorithm: " + rsa.getAlgorithm());
////        System.out.println("Key Cipher provide name: " + rsa.getProvider().getName() + " " + rsa.getProvider().getVersion());
//        System.out.println("Time elapsed for generate AES Key: " + (System.currentTimeMillis()-start));
        final String dataTransformation = "AES/CBC/PKCS5Padding";
        Cipher aes = Cipher.getInstance(dataTransformation);
        Base64.Encoder encoder = Base64.getEncoder();
        // use secret key to encrypt plaintext
        SecretKey secretKey = generateAESKey(128);
        aes.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherDataBytes = aes.doFinal(plaintext.getBytes());
        String cipherDataString = encoder.encodeToString(cipherDataBytes);
        rsa.init(Cipher.ENCRYPT_MODE,pubKey);
        byte[] iv = aes.getIV();
        byte[] cipherKeyBytes = rsa.doFinal(secretKey.getEncoded());
        byte[] ivAndCipherKeyBytes = Bytes.concat(iv, cipherKeyBytes);
        String ivAndCipherKeyString = encoder.encodeToString(ivAndCipherKeyBytes);

        return new CipherKeyAndData(ivAndCipherKeyString, cipherDataString);
    }

    String decrypt(CipherKeyAndData cipherKeyAndData, PrivateKey privKey) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();

        final String keyTransformation = "RSA/NONE/OAEPWithSHA256AndMGF1Padding";
        Cipher rsa = Cipher.getInstance(keyTransformation);
        byte[] ivAndCipherKeyBytes2 = decoder.decode(cipherKeyAndData.getCipherKey());
        rsa.init(Cipher.DECRYPT_MODE,privKey);
        byte[] plainKeyBytes = rsa.doFinal(ivAndCipherKeyBytes2, 16, ivAndCipherKeyBytes2.length-16);

        SecretKeySpec secretKeySpec = new SecretKeySpec(plainKeyBytes, "AES");

        final String dataTransformation = "AES/CBC/PKCS5Padding";
        Cipher aes = Cipher.getInstance(dataTransformation);
        aes.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(ivAndCipherKeyBytes2,0, 16));
        byte[] plainDataBytes = aes.doFinal(decoder.decode(cipherKeyAndData.getCipherData()));
        String plainDataString = new String(plainDataBytes);

        return plainDataString;
    }

    KeyPair generateRSAKey(int length) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());

        KeyPair keyPair = generator.generateKeyPair();
        System.out.println(String.join(":::", keyPair.getPublic().getAlgorithm(), keyPair.getPublic().getFormat()) );

        System.out.println(String.join(":::", keyPair.getPrivate().getAlgorithm(), keyPair.getPrivate().getFormat()) );
        return keyPair;
    }

    private SecretKey generateAESKey(int length) throws Exception {
//        long start = System.currentTimeMillis();
        KeyGenerator secretGenerator = KeyGenerator.getInstance("AES");
        secretGenerator.init(128);
        SecretKey secretKey = secretGenerator.generateKey();
//        System.out.println("Time elapsed for generate AES Key: " + (System.currentTimeMillis()-start));
//        System.out.println("Algorithm: " + secretKey.getAlgorithm() + " Format: " + secretKey.getFormat()
//                + " Encoding length: " + secretKey.getEncoded().length);
        return secretKey;
    }

    private void writeRSAKey() throws Exception {
        final String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";
        final String RSA_PUBLIC_KEY = "PUBLIC KEY";
        PemObject pemObject = new PemObject(RSA_PUBLIC_KEY, this.keyPair.getPublic().getEncoded());
        PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream("id_rsa.pub")));
        try {
            pemWriter.writeObject(pemObject);
        } finally {
            pemWriter.close();
        }

        pemObject = new PemObject(RSA_PRIVATE_KEY, this.keyPair.getPrivate().getEncoded());
        pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream("id_rsa")));
        try {
            pemWriter.writeObject(pemObject);
        } finally {
            pemWriter.close();
        }
    }


    private void addBouncyCastle() {
        Provider bcprov = new BouncyCastleProvider();
        int precedenceOrder = Security.addProvider(bcprov);
        System.out.println("added  " + bcprov.getName() + " " + bcprov.getVersion() + " as precedence " + precedenceOrder);
    }

}
