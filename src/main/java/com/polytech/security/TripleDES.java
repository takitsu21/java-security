package com.polytech.security;



import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import org.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.util.*;

public class TripleDES{

	static public void main(String[] argv){
		
		Provider prov = new BouncyCastleProvider();
		Security.addProvider(prov);
		
		try{
	
			if(argv.length>0){
			
				// Create a TripleDES object 
				TripleDES the3DES = new TripleDES();
			
				if(argv[0].compareTo("-ECB")==0){
					// ECB mode
				  	// encrypt ECB mode
				  	Vector<SecretKey> Parameters=
					  	the3DES.encryptECB(
					  			new FileInputStream(new File(argv[1])),  	// clear text file 
				   	  			new FileOutputStream(new File(argv[2])), 	// file encrypted
				   	  			"DES", 										// KeyGeneratorName
				   	  			"DES/ECB/NoPadding"); 						// CipherName

				  	// decrypt ECB mode
				  	the3DES.decryptECB(Parameters,				 			// the 3 DES keys
				  				new FileInputStream(new File(argv[2])),  	// the encrypted file 
				   	  			new FileOutputStream(new File(argv[3])),	// the decrypted file
				   	  			"DES/ECB/NoPadding"); 		  				// CipherName
				}	
				else if(argv[0].compareTo("-CBC")==0){
					// decryption
				  	// encrypt CBC mode
				  	Vector<SecretKey> Parameters =
					  	the3DES.encryptCBC(
					  			new FileInputStream(new File(argv[1])),  	// clear text file 
				   	  			new FileOutputStream(new File(argv[2])), 	// file encrypted
				   	  			"DES", 										// KeyGeneratorName
					  			"DES/CBC/NoPadding"); 						// CipherName
				   	  			//"DES/CBC/PKCS5Padding"); 					// CipherName 
				  	// decrypt CBC mode	
				  	the3DES.decryptCBC(
				  				Parameters,				 					// the 3 DES keys
			  					new FileInputStream(new File(argv[2])),  	// the encrypted file 
			  					new FileOutputStream(new File(argv[3])),	// the decrypted file
				  				"DES/CBC/NoPadding"); 						// CipherName			
				  				//"DES/CBC/PKCS5Padding"); 		  			// CipherName	  
				}
			
			}
			
			else{
				System.out.println("java TripleDES -ECB clearTextFile EncryptedFile DecryptedFile");
				System.out.println("java TripleDES -CBC clearTextFile EncryptedFile DecryptedFile");
			} 
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("java TripleDES -ECB clearTextFile EncryptedFile DecryptedFile");
			System.out.println("java TripleDES -CBC clearTextFile EncryptedFile DecryptedFile");
		}
	}

	
	/**
	 * 3DES ECB Encryption
	 */
	private Vector<SecretKey> encryptECB(FileInputStream in,
							FileOutputStream out, 
							String KeyGeneratorInstanceName,
							String CipherInstanceName){
		try{
			KeyGenerator keygenerator = KeyGenerator.getInstance(KeyGeneratorInstanceName);

			SecretKey desKey = keygenerator.generateKey();
			SecretKey desKey2 = keygenerator.generateKey();
			SecretKey desKey3 = keygenerator.generateKey();


			// GENERATE 3 DES KEYS

			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName
				// FOR ENCRYPTION 
				// WITH THE FIRST GENERATED DES KEY
			Cipher desCipher = Cipher.getInstance(CipherInstanceName);
			desCipher.init(Cipher.ENCRYPT_MODE, desKey);


			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName
				// FOR DECRYPTION
				// WITH THE SECOND GENERATED DES KEY
			Cipher desCipher2 = Cipher.getInstance(CipherInstanceName);
			desCipher2.init(Cipher.DECRYPT_MODE, desKey2);

			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName 
				// FOR ENCRYPTION
				// WITH THE THIRD GENERATED DES KEY
			Cipher desCipher3 = Cipher.getInstance(CipherInstanceName);
			desCipher3.init(Cipher.ENCRYPT_MODE, desKey3);
			// GET THE MESSAGE TO BE ENCRYPTED FROM IN


			// CIPHERING
				// CIPHER WITH THE FIRST KEY
				// DECIPHER WITH THE SECOND KEY
				// CIPHER WITH THE THIRD KEY
				// write encrypted file
		// key1.cipher(key2.decipher(key3.cipher(message)))


			byte[] ciphered = desCipher.doFinal(in.readAllBytes());
			byte[] deciphered = desCipher2.doFinal(ciphered);
			byte[] ciphered2 = desCipher3.doFinal(deciphered);

			out.write(ciphered2);
			// WRITE THE ENCRYPTED DATA IN OUT
			Vector parameters = new Vector<>();
			parameters.add(desKey);
			parameters.add(desKey2);
			parameters.add(desKey3);
			System.out.println(parameters);
			// return the DES keys list generated
			return parameters;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 3DES ECB Decryption 
	 */
	private void decryptECB(Vector<SecretKey> Parameters,
						FileInputStream in, 
						FileOutputStream out, 
						String CipherInstanceName){
		try{
			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName
				// FOR DECRYPTION 
				// WITH THE THIRD GENERATED DES KEY
			Cipher desCipher = Cipher.getInstance(CipherInstanceName);
			desCipher.init(Cipher.DECRYPT_MODE, Parameters.get(2));


			
			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName
				// FOR ENCRYPTION
				// WITH THE SECOND GENERATED DES KEY
			Cipher desCipher2 = Cipher.getInstance(CipherInstanceName);
			desCipher2.init(Cipher.ENCRYPT_MODE, Parameters.get(1));

				
			// CREATE A DES CIPHER OBJECT FOR ENCRYPTION
				// WITH CipherInstanceName
				// FOR DECRYPTION
				// WITH THE FIRST GENERATED DES KEY
			Cipher desCipher3 = Cipher.getInstance(CipherInstanceName);
			desCipher3.init(Cipher.DECRYPT_MODE, Parameters.get(0));
			
			// GET THE ENCRYPTED DATA FROM IN
			byte[] ciphered = in.readAllBytes();
			
			// DECIPHERING     
				// DECIPHER WITH THE THIRD KEY
				// 	CIPHER WITH THE SECOND KEY
				// 	DECIPHER WITH THE FIRST KEY

			byte[] deciphered = desCipher.doFinal(ciphered);
			byte[] ciphered2 = desCipher2.doFinal(deciphered);
			byte[] deciphered2 = desCipher3.doFinal(ciphered2);

			// WRITE THE DECRYPTED DATA IN OUT
			out.write(deciphered2);

		}catch(Exception e){
			e.printStackTrace();
		}

	}
	  
	/**
	 * 3DES CBC Encryption
	 */
	private Vector<SecretKey> encryptCBC(FileInputStream in,
							FileOutputStream out, 
							String KeyGeneratorInstanceName, 
							String CipherInstanceName){
		try{
		
			// GENERATE 3 DES KEYS
			// GENERATE THE IV
			KeyGenerator keygenerator = KeyGenerator.getInstance(KeyGeneratorInstanceName);
			SecretKey desKey = keygenerator.generateKey();
			SecretKey desKey2 = keygenerator.generateKey();
			SecretKey desKey3 = keygenerator.generateKey();
			byte[] iv = new byte[8];
			SecureRandom random = new SecureRandom();
			random.nextBytes(iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);


		
			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName
				// FOR ENCRYPTION 
				// WITH THE FIRST GENERATED DES KEY
			Cipher desCipher = Cipher.getInstance(CipherInstanceName);
			desCipher.init(Cipher.ENCRYPT_MODE, desKey, ivspec);

			
			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName
				// FOR DECRYPTION
				// WITH THE SECOND GENERATED DES KEY
			Cipher desCipher2 = Cipher.getInstance(CipherInstanceName);
			desCipher2.init(Cipher.DECRYPT_MODE, desKey2, ivspec);

				
			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName 
				// FOR ENCRYPTION
				// WITH THE THIRD GENERATED DES KEY
			Cipher desCipher3 = Cipher.getInstance(CipherInstanceName);
			desCipher3.init(Cipher.ENCRYPT_MODE, desKey3, ivspec);
				
			// GET THE DATA TO BE ENCRYPTED FROM IN 
			
			// CIPHERING     
				// CIPHER WITH THE FIRST KEY
				// DECIPHER WITH THE SECOND KEY
				// CIPHER WITH THE THIRD KEY


			// WRITE THE ENCRYPTED DATA IN OUT
			byte[] ciphered = desCipher.doFinal(in.readAllBytes());
			byte[] deciphered = desCipher2.doFinal(ciphered);
			byte[] ciphered2 = desCipher3.doFinal(deciphered);

			out.write(ciphered2);
			
			// return the DES keys list generated
			Vector parameters = new Vector<>();
			parameters.add(desKey);
			parameters.add(desKey2);
			parameters.add(desKey3);
			parameters.add(ivspec);
			return parameters;
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 3DES CBC Decryption 
	 */
	private void decryptCBC(Vector Parameters,
						FileInputStream in, 
						FileOutputStream out, 
						String CipherInstanceName){
		try{
		
			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName
				// FOR DECRYPTION  
				// WITH THE THIRD GENERATED DES KEY
			IvParameterSpec ivspec = (IvParameterSpec) Parameters.get(3);
			Cipher desCipher = Cipher.getInstance(CipherInstanceName);
			desCipher.init(Cipher.DECRYPT_MODE, (SecretKey)Parameters.get(2), ivspec);


			
			// CREATE A DES CIPHER OBJECT 
				// WITH CipherInstanceName
				// FOR ENCRYPTION 
				// WITH THE SECOND GENERATED DES KEY
			Cipher desCipher2 = Cipher.getInstance(CipherInstanceName);
			desCipher2.init(Cipher.ENCRYPT_MODE, (SecretKey)Parameters.get(1), ivspec);
				
			// CREATE A DES CIPHER OBJECT FOR ENCRYPTION
				// WITH CipherInstanceName
				// FOR DECRYPTION 
				// WITH THE FIRST GENERATED DES KEY
			Cipher desCipher3 = Cipher.getInstance(CipherInstanceName);
			desCipher3.init(Cipher.DECRYPT_MODE, (SecretKey)Parameters.get(0), ivspec);
			
			// GET ENCRYPTED DATA FROM IN
			
			// DECIPHERING     
				// DECIPHER WITH THE THIRD KEY
				// 	CIPHER WITH THE SECOND KEY
				// 	DECIPHER WITH THE FIRST KEY
			byte[] deciphered = desCipher.doFinal(in.readAllBytes());
			byte[] ciphered = desCipher2.doFinal(deciphered);
			byte[] deciphered2 = desCipher3.doFinal(ciphered);

			// WRITE THE DECRYPTED DATA IN OUT
			out.write(deciphered2);

			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	  

}