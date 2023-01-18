package com.polytech;

/**
 * TD2 - RSA signature, encryption/decryption
 * <p>
 * asymetric clearTextFile SignatureFile CipheredFile DecipheredFile
 **/

import com.polytech.security.TripleDES;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.*;
import java.io.*;

public class Asymetric {

    static public void main(String argv[]) {

        // INITIALIZATION

        // load the bouncycastle provider
        Provider prov = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        Security.addProvider(prov);

        // create two new entity
        Entity Alice = new Entity();
        Entity Bob = new Entity();

        try {

            // GET THE CLEAR TEXT
            File aFile = new File(argv[0]);
            FileInputStream in = new FileInputStream(aFile);
            byte[] aMessage = new byte[(int) aFile.length()];
            in.read(aMessage);
            in.close();

            // RSA SIGNATURE
            System.out.println("\nRSA SIGNATURE\n");
            // MAKE ALICE SIGN IT
            // display the clear text
            System.out.println("Message == \n" + new String(aMessage));
            // sign it
            byte[] aSignature = Alice.sign(aMessage);
            // display and store the signature
            System.out.println("Alice Signature == \n" + new String(aSignature));
            FileOutputStream out = new FileOutputStream(new File(argv[1]));
            out.write(aSignature);
            out.close();

            // BOB CHECKS THE ALICE SIGNATURE
            System.out.println("Bob signature verification == \n" + Bob.checkSignature(aMessage, aSignature, Alice.thePublicKey));

            // MY RSA SIGNATURE
            System.out.println("\nMY RSA SIGNATURE\n");
            // MAKE ALICE SIGN IT
            // display the clear text
            System.out.println("Message == \n" + new String(aMessage));
            // sign it
            aSignature = Alice.mySign(aMessage);
            // display and store the signature
            System.out.println("Alice Signature == \n" + new String(aSignature));
            out = new FileOutputStream(new File(argv[1]));
            out.write(aSignature);
            out.close();

            // BOB CHECKS THE ALICE SIGNATURE
            System.out.println("Bob signature verification == " + Bob.myCheckSignature(aMessage, aSignature, Alice.thePublicKey));

            // RSA ENCRYPTION/DECRYPTION
            System.out.println("\nRSA ENCRYPTION\n");
            // bob encrypt a message with the alice public key
            System.out.println("Clear Text == \n" + new String(aMessage));
            byte[] aCiphered = Bob.encrypt(aMessage, Alice.thePublicKey);
            System.out.println("Ciphered Text== \n" + new String(aCiphered) + "\n");
            out = new FileOutputStream(new File(argv[2]));
            out.write(aCiphered);
            out.close();

            // alice decrypt the message
            byte[] aDeciphered = Alice.decrypt(aCiphered);
            System.out.println("Deciphered Text== \n" + new String(aDeciphered));
            out = new FileOutputStream(new File(argv[3]));
            out.write(aDeciphered);
            out.close();
//			 PROTOCOL IMPLEMENTATION
            KeyExchangeProtocol();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("java Asymetric clearTextFile SignatureFile CipheredFile DecipheredFile");
        }
    }

    private static void KeyExchangeProtocol() {
        Entity Alice, Bob;
        Alice = new Entity();
        Bob = new Entity();
        Provider prov = new BouncyCastleProvider();
        Security.addProvider(prov);

        //	Alice sends her public key to Bob.
        Bob.alicePublicKey = Alice.thePublicKey;
        //	Bob generate a DES session key.
        SecretKey sessionKey = Bob.generateSessionKey();
        Bob.regenIV();
        byte[] encIV = Bob.encryptIV();
        Alice.decryptIV(encIV);
        //	Bob encrypts the session key with Alice's public key.
        byte[] encryptedSessionKey = Bob.encryptSessionKey(sessionKey, Alice.thePublicKey);
        //	Alice decrypts the DES key with her private key.
        Alice.decryptSessionKey(encryptedSessionKey);
        //  Alice sends a message to Bob with her session key
        String message = "Hello Bob";
        byte[] encryptedMessage = Alice.encryptDES(message.getBytes());
        System.out.println("Encrypted message: " + new String(encryptedMessage));
        // Bob decrypts the message with his session key
        byte[] decipheredMessage = Bob.decryptDES(encryptedMessage);
        System.out.println("Deciphered Message == \n" + new String(decipheredMessage));

    }

}