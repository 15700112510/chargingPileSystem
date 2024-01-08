package com.example.chargingPileSystem.util;

import org.apache.catalina.security.SecurityUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class HashEncodeUtil {
    public static  String getEncodePsw(String password,String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes());

        String hashedPassword = new String(md.digest(password.getBytes(StandardCharsets.UTF_8)));

        return hashedPassword;
    }
}
