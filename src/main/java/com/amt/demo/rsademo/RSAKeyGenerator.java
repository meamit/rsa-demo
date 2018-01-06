package com.amt.demo.rsademo;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * This class generates key pair and saves in file in current directory
 * 
 * @author amit
 * 
 */
public class RSAKeyGenerator {

	public void generateKeyPair(String publicKeyFileName,
			String privateKeyFileName) {
		KeyPairGenerator kpg = null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kpg.initialize(2048);
		KeyPair kp = kpg.genKeyPair();
		Key publicKey = kp.getPublic();
		Key privateKey = kp.getPrivate();
		try {
			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(publicKey,
					RSAPublicKeySpec.class);
			RSAPrivateKeySpec priv = fact.getKeySpec(privateKey,
					RSAPrivateKeySpec.class);

			saveToFile(publicKeyFileName, pub.getModulus(),
					pub.getPublicExponent());
			System.out.println("public key saved");
			saveToFile(privateKeyFileName, priv.getModulus(),
					priv.getPrivateExponent());
			System.out.println("private key saved");
		} catch (Exception e) {
			System.out.println("error ");
		}
	}

	private void saveToFile(String fileName, BigInteger mod, BigInteger exp)
			throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(fileName)));
		try {
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (Exception e) {
			throw new IOException("Unexpected error", e);
		} finally {
			oout.close();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RSAKeyGenerator t = new RSAKeyGenerator();
		t.generateKeyPair("public.key", "private.key");

	}

}
