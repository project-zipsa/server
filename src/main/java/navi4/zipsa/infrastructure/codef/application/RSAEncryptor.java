package navi4.zipsa.infrastructure.codef.application;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class RSAEncryptor {

    public static String encrypt(String plainText, String base64PublicKey) throws Exception {
        PublicKey publicKey = getPublicKeyFromBase64(base64PublicKey);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static PublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
        String cleanedKey = base64PublicKey
                .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(cleanedKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }
}
