package com.polytech;

/**
 * TD2 - RSA signature, encryption/decryption
 * <p>
 * asymetric clearTextFile SignatureFile CipheredFile DecipheredFile
 **/

import com.polytech.security.TripleDES;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
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
        Entity Alice = new Entity(null);
        Entity Bob = new Entity(null);

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
            agreement();
//            KeyExchangeProtocol();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("java Asymetric clearTextFile SignatureFile CipheredFile DecipheredFile");
        }


    }

    private static void diffieHellman(Entity alice, Entity bob) throws Exception {
        BigInteger g512 = new BigInteger("1234567890", 16);
        BigInteger p512 = new BigInteger("1234567890", 16);
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        DHParameterSpec dhParams = new DHParameterSpec(p512, g512);
        KeyPairGenerator KeyGen = KeyPairGenerator.getInstance("DH", "BC");
        KeyGen.initialize(dhParams, new SecureRandom());
        alice.setDhKeyPair(KeyGen.generateKeyPair());
        alice.setKeyAgree(KeyAgreement.getInstance("DH", "BC"));

        bob.setDhKeyPair(KeyGen.generateKeyPair());
        bob.setKeyAgree(KeyAgreement.getInstance("DH", "BC"));

        alice.getKeyAgree().doPhase(bob.getDhPubKey(), true);
        bob.getKeyAgree().doPhase(alice.getDhPubKey(), true);









    }

    public static void agreement() {

        try {
            BigInteger g512 = new BigInteger("1234567890", 16);
            BigInteger p512 = new BigInteger("1234567890", 16);
            DHParameterSpec dhParams = new DHParameterSpec(p512, g512);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(dhParams, new SecureRandom());

            Entity Alice, Bob;
            Alice = new Entity(keyGen);
            Bob = new Entity(keyGen);






            Alice.setKeyAgree(KeyAgreement.getInstance("DH", "BC"));
            Bob.setKeyAgree(KeyAgreement.getInstance("DH", "BC"));

            Alice.getKeyAgree().init(Alice.getThePrivateKey());
            Bob.getKeyAgree().init(Bob.getThePrivateKey());

            Alice.getKeyAgree().doPhase(Bob.getDhKeyPair().getPublic(), true);
            Bob.getKeyAgree().doPhase(Alice.getDhKeyPair().getPublic(), true);

            MessageDigest hash = MessageDigest.getInstance("SHA1", "BC");
            System.out.println(new String(hash.digest(Alice.getKeyAgree().generateSecret())));
            System.out.println(new String(hash.digest(Bob.getKeyAgree().generateSecret())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private static void KeyExchangeProtocol() throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        Entity Alice, Bob;
//        Alice = new Entity();
//        Bob = new Entity();
//        Provider prov = new BouncyCastleProvider();
//        Security.addProvider(prov);
//
//        //	Alice sends her public key to Bob.
//        Bob.alicePublicKey = Alice.thePublicKey;
//        //	Bob generate a DES session key.
//        TripleDES sessionKey = new TripleDES();
//
//        //	Bob encrypts it with Alice’s public key.
////        byte[] cipheredKey = Bob.encrypt(sessionKey.encryptCBC(), Alice.thePublicKey);
//        //	Alice decrypts the DES key with her private key.
////        byte[] decipheredKey = Alice.decrypt(cipheredKey);
//        //  Alice sends a message to Bob with her session key
//        String message = "Hello Bob";
//        byte[] cipheredMessage = Alice.encrypt(message.getBytes("UTF-8"), Alice.thePublicKey);
//        //	Bob decrypts the message with the session key.
//        byte[] decipheredMessage = Bob.decrypt(cipheredMessage);
//        System.out.println("Deciphered Message == \n" + new String(decipheredMessage));
//
//    }

}