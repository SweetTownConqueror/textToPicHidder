package crypter;

import java.security.*;

import javax.crypto.*; 
import javax.crypto.spec.*; 
import java.io.*;
import org.apache.commons.codec.binary.Base64;

public class AESCrypter {
	
	public static String encrypt(String key, byte[] initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector/*.getBytes("UTF-8")*/);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, byte[] initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector/*.getBytes("UTF-8")*/);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            //System.out.println("decrypted string : "+original);
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String key = "Bar12345Bar12345Bar12345Bar12345"; // 256 bit key
        //String initVector = "RandomInitVector"; // 16 bytes IV
        
        SecureRandom random = new SecureRandom();
        //byte bytes[] = new byte[16];
        byte[] iv = new byte[128/8];
        random.nextBytes(iv);
        /*
        String s="";
        for(int i=0;i<iv.length;i++) {
        	s=s+iv[i];
        	System.out.println(iv[i]); 
        	System.out.println("length : "+i); 
        }*/
        //System.out.println("IV : "+s); 
        //System.out.println("IV: "+iv[1]);
        //System.out.println(encrypt(key, initVector, "Hello World"));
        System.out.println(decrypt(key, iv,encrypt(key, iv, "Hello World")));
    }
}


