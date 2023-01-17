package com.polytech;


import org.bouncycastle.asn1.pkcs.DHParameter;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import javax.crypto.*;

import java.io.*;
import java.util.Arrays;

public class Entity {
    // keypair
    public PublicKey thePublicKey;
    private PrivateKey thePrivateKey;

    /**
     * Entity Constructor
     * Public / Private Key generation
     **/
    public Entity() {
        // INITIALIZATION

        // generate a public/private key
        try {
            KeyPairGenerator KeyGen = java.security.KeyPairGenerator.getInstance("RSA");
            KeyGen.initialize(1024);
            // get an instance of KeyPairGenerator  for RSA
            // Initialize the key pair generator for 1024 length
            // Generate the key pair
            KeyPair pair = KeyGen.generateKeyPair();
            // save the public/private key
            this.thePublicKey = pair.getPublic();
            this.thePrivateKey = pair.getPrivate();

        } catch (Exception e) {
            System.out.println("Signature error");
            e.printStackTrace();
        }
    }

    /**
     * Sign a message
     * Parameters
     * aMessage : byte[] to be signed
     * Result : signature in byte[]
     **/
    public byte[] sign(byte[] aMessage) {

        try {
            // use of java.security.Signature
            // Init the signature with the private key
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initSign(this.thePrivateKey);


            // update the message
            // sign
            sig.update(aMessage);
            return sig.sign();
        } catch (Exception e) {
            System.out.println("Signature error");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Check aSignature is the signature of aMessage with aPK
     * Parameters
     * aMessage : byte[] to be signed
     * aSignature : byte[] associated to the signature
     * aPK : a public key used for the message signature
     * Result : signature true or false
     **/
    public boolean checkSignature(byte[] aMessage, byte[] aSignature, PublicKey aPK) {
        try {
            // use of java.security.Signature
            // init the signature verification with the public key
            Signature sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(aPK);

            // update the message
            // check the signature
            sig.update(aMessage);
            return sig.verify(aSignature);
        } catch (Exception e) {
            System.out.println("Verify signature error");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Sign a message
     * Parameters
     * aMessage : byte[] to be signed
     * Result : signature in byte[]
     **/
    public byte[] mySign(byte[] aMessage) {

        try {
            // get an instance of a cipher with RSA with ENCRYPT_MODE
            // Init the signature with the Public key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.thePublicKey);


            // get an instance of the java.security.MessageDigest with SHA1
            // process the digest
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(aMessage);


            // return the encrypted digest
            return cipher.doFinal(digest);
        } catch (Exception e) {
            System.out.println("Signature error");
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Check aSignature is the signature of aMessage with aPK
     * Parameters
     * aMessage : byte[] to be signed
     * aSignature : byte[] associated to the signature
     * aPK : a public key used for the message signature
     * Result : signature true or false
     **/
    public boolean myCheckSignature(byte[] aMessage, byte[] aSignature, PublicKey aPK) {
        try {
            // get an instance of a cipher with RSA with ENCRYPT_MODE
            // Init the signature with the private key
            // decrypt the signature
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.thePrivateKey);

            // get an instance of the java.security.MessageDigest with SHA1
            // process the digest
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(aMessage);

            // check if digest1 == digest2
            return Arrays.equals(digest, aSignature);

        } catch (Exception e) {
            System.out.println("Verify signature error");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Encrypt aMessage with aPK
     * Parameters
     * aMessage : byte[] to be encrypted
     * aPK : a public key used for the message encryption
     * Result : byte[] ciphered message
     **/
    public byte[] encrypt(byte[] aMessage, PublicKey aPK) {
        try {
            // get an instance of RSA Cipher
            // init the Cipher in ENCRYPT_MODE and aPK
            // use doFinal on the byte[] and return the ciphered byte[]
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aPK);
            return cipher.doFinal(aMessage);

        } catch (Exception e) {
            System.out.println("Encryption error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decrypt aMessage with the entity private key
     * Parameters
     * aMessage : byte[] to be encrypted
     * Result : byte[] deciphered message
     **/
    public byte[] decrypt(byte[] aMessage) {
        try {
            // get an instance of RSA Cipher
            // init the Cipher in DECRYPT_MODE and aPK
            // use doFinal on the byte[] and return the deciphered byte[]
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.thePrivateKey);
            return cipher.doFinal(aMessage);

        } catch (Exception e) {
            System.out.println("Encryption error");
            e.printStackTrace();
            return null;
        }

    }



}