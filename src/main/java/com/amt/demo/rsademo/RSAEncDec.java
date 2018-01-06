package com.amt.demo.rsademo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import java.math.BigInteger;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class RSAEncDec {

    private PublicKey readPublicKeyFromFile(final String fileloc) throws IOException {
        InputStream in = new FileInputStream(new File(fileloc));

        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }

    private PrivateKey readPrivateKeyFromFile(final String fileLoc) throws IOException {
        InputStream in = new FileInputStream(new File(fileLoc));

        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey prvKey = fact.generatePrivate(keySpec);
            return prvKey;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }

    public byte[] rsaEncrypt(final byte[] data, final String fileLoc) {
        try {
            PublicKey pubKey = readPublicKeyFromFile(fileLoc);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            byte[] cipherData = cipher.doFinal(data);
            return cipherData;
        } catch (Exception e) {
            System.out.println("some thing went wrong");
        }

        return null;
    }

    public byte[] rsaDecrypt(final byte[] data, final String fileLoc) {
        try {
            PrivateKey pubKey = readPrivateKeyFromFile(fileLoc);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);

            byte[] cipherData = cipher.doFinal(data);
            return cipherData;
        } catch (Exception e) {
            System.out.println("some thing went wrong");
        }

        return null;
    }

    /*
     * string
     *
     * @param data (normal string)
     *
     * @return
     */
    public String encrypt(final String data, final String fileLoc) {
        byte[] enc = rsaEncrypt(data.getBytes(), fileLoc);
        return Base64.encode(enc);
    }

    /**
     * return the normal string.
     *
     * @param   data  (base64 encoded string)
     *
     * @return
     *
     * @throws  Base64DecodingException
     */
    public String decrypt(final String data, final String fileLoc) throws Base64DecodingException {
        byte[] enc = Base64.decode(data);
        byte[] decryptedData = rsaDecrypt(enc, fileLoc);
        return new String(decryptedData);
    }

    /**
     * @param  args
     */
    public static void main(final String[] args) {

        // TODO Auto-generated method stub
        RSAEncDec obj = new RSAEncDec();
        String enc = obj.encrypt("hello world", "./public.key");
        System.out.println(enc);

        String dat = null;
        try {
            dat = obj.decrypt(enc, "./private.key");
        } catch (Base64DecodingException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(dat);

    }

}
