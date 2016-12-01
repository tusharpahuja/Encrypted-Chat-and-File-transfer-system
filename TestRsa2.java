package sample;

import java.security.KeyPair;		//Including the required libraries
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.math.BigInteger;
import java.security.KeyFactory;		
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;		//Library for private key 
import java.security.interfaces.RSAPublicKey;		//Library for public key 
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import sun.misc.BASE64Decoder;			//Library for decoding the text
import sun.misc.BASE64Encoder;			//Library for encoding the text

import javax.crypto.Cipher;

public class TestRsa2
{
	private KeyPair keyPair;		

	public TestRsa2() throws Exception
	{
		Initialize();
	}

	public void Initialize() throws Exception
	{
		KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");			//Generating key pairs
		keygen.initialize(512);
		keyPair = keygen.generateKeyPair();
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");			//Generating a key factory
    RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
        "12345678", 16), new BigInteger("11", 16));
    RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger(
        "12345678", 16), new BigInteger("12345678",
        16));
	}

	public String encrypt(String plaintext)  throws Exception		//Function for encrypting the text
	{
		PublicKey key = keyPair.getPublic();		//Generating the public key
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF8"));
		return encodeBASE64(ciphertext);
	}

	public String decrypt(String ciphertext)  throws Exception		//Function for decrypting the text
	{
		PrivateKey key = keyPair.getPrivate();
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] plaintext = cipher.doFinal(decodeBASE64(ciphertext));		//Returning the final encoded ciphertext
		return new String(plaintext, "UTF8");
	}

    private static String encodeBASE64(byte[] bytes)		//Function for base64 encoding
    {
        BASE64Encoder b64 = new BASE64Encoder();
        return b64.encode(bytes);
    }

    private static byte[] decodeBASE64(String text) throws Exception		//Function for base64 decoding
    {
        BASE64Decoder b64 = new BASE64Decoder();
        return b64.decodeBuffer(text);
    }

	public static void main(String[] args) throws Exception
	{
		TestRsa2 app = new TestRsa2();

		System.out.println("Enter a line: ");
		java.io.InputStreamReader sreader = new java.io.InputStreamReader(System.in);		//Reading the input text
		java.io.BufferedReader breader = new java.io.BufferedReader(sreader);
		String input = breader.readLine();

		System.out.println("Plaintext = " + input);

		String ciphertext = app.encrypt(input);					
		System.out.println("After Encryption Ciphertext = " + ciphertext);		//Printing the encrypted and decrypted text to the console
		System.out.println("After Decryption Plaintext = " + app.decrypt(ciphertext));

	}
}