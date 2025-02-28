package com.silverviles.af_assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
public class AfAssignmentApplication {

    public static void main(String[] args) {
//        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//        keyGen.init(256); // Key size
//        SecretKey secretKey = keyGen.generateKey();
//        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        System.out.println("Generated Secret Key: " + encodedKey);
        SpringApplication.run(AfAssignmentApplication.class, args);
    }

}
