package com.github.echological.app.global.util;

import com.github.echological.app.global.constant.AVRHttpStatus;
import com.github.echological.app.global.exception.AVRBusinessValidationException;
import lombok.experimental.UtilityClass;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@UtilityClass
public class AVREncryptionUtil {

    private static final Logger log = LoggerFactory.getLogger(AVREncryptionUtil.class);

    public static String decrypt(String secretValue, String encryptionPassword){
        try {
            StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
            decryptor.setPassword(encryptionPassword);
            return decryptor.decrypt(secretValue);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AVRBusinessValidationException(
                    AVRHttpStatus.FAILED.getCode(),
                    "Invalid parameter signature",
                    HttpStatus.BAD_REQUEST,
                    new ArrayList<>()
            );
        }
    }

    public static String encrypt(String plainValue, String encryptionPassword) {
        try {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(encryptionPassword);
            return encryptor.encrypt(plainValue);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AVRBusinessValidationException(
                    AVRHttpStatus.FAILED.getCode(),
                    "Invalid parameter signature",
                    HttpStatus.BAD_REQUEST,
                    new ArrayList<>()
            );
        }
    }

}
