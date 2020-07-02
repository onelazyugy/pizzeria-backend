package com.vietle.pizzeria.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;

@Component
public class HelperBean {
    @Value("secret")
    private String secret;

    @Value(("salt"))
    private String salt;

    @Bean("objectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public String encrypt(String textToEncrypt) {
        String salt = KeyGenerators.string().generateKey();
        TextEncryptor encryptor = Encryptors.text(this.secret, convertStringToHex(this.salt));
        String encryptedText = encryptor.encrypt(textToEncrypt);
        return encryptedText;
    }

    public String decrypt(String encryptedText) {
        TextEncryptor decryptor = Encryptors.text(this.secret, convertStringToHex(this.salt));
        String decryptedText = decryptor.decrypt(encryptedText);
        return decryptedText;
    }

    private String convertStringToHex(String salt) {
        char[] chars = Hex.encodeHex(salt.getBytes(StandardCharsets.UTF_8));
        return  String.valueOf(chars);
    }
}
