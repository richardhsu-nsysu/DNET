package tw.edu.nsysu.cse.iccsl.dnet;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AES {
//	AES/ECB/PKCS5Padding
	
	private Cipher enc = null;
	private Cipher dec = null;
	
	AES() throws Exception{
		//gen key
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256,new SecureRandom( ) );
		SecretKey secretKey = keyGen.generateKey();
		//gen encrypter
		enc = Cipher.getInstance("AES");
		enc.init(Cipher.ENCRYPT_MODE, secretKey); 
		//gen decrypter
		dec = Cipher.getInstance("AES");
		dec.init(Cipher.DECRYPT_MODE, secretKey);  
	}
	 
	byte[] Encrypt(byte[] PT) throws Exception{  
		return enc.doFinal(PT);  
	}  

	byte[] Decrypt(byte[] CT) throws Exception{  
		return dec.doFinal(CT);    
	}  
}
