package tw.edu.nsysu.cse.iccsl.dnet;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import it.unisa.dia.gas.jpbc.Element;

public class Main {
	
	
	public static void main(String[] args) throws Exception {
		CoABETest();
	}
	
	static void AESTest() throws Exception {
		//AES256 ECB decrypt performance
		//test work or not
		AES aes = new AES();
		byte[] a = "4531212564612313212345312125646123132123453121256461231321234531212564612313212345312125646123132123".getBytes(); 
		System.out.println(a.length);
		byte[] b = aes.Encrypt(a);
		byte[] c = aes.Decrypt(b);
		System.out.println(Base64.getEncoder().encodeToString(a));
		System.out.println(Base64.getEncoder().encodeToString(b));
		System.out.println(Base64.getEncoder().encodeToString(c));
		
		//test performance
		long t1 = System.currentTimeMillis();
		for(int i=0;i<100000000;i++) {
			aes.Decrypt(b);
		}
		long t2 = System.currentTimeMillis();
		System.out.println(t2-t1);
	}
	static void SHATest() throws Exception {
		MessageDigest messageDigest;
		messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] a = "012345678901234567898".getBytes(); 
		//test performance
		for(int j=0;j<10;j++) {
			long t1 = System.currentTimeMillis();
			for(int i=0;i<10000000;i++) {
				messageDigest.digest(a);
			}
			long t2 = System.currentTimeMillis();
			System.out.println(t2-t1);	
		}

		
	}
	
	static void CoABETest() {
		int maxAttrNumb = 120;
		
//		{9,3,3,3,3}
//		int[] policyFormat = {9,3,3,3,3};
//		int[] policy = {0,1,2,3,4,5,6,7,8};
//		int[] userAttrs = {1,4,10};
		
//		{27,3,9,9,9}
//		int[] policyFormat = {27,3,9,9,9};
//		int[] userAttrs = {1,10,19};
//		int[] policy = new int[policyFormat[0]];
//		for(int i=0;i<policyFormat[0];i++) {
//			policy[i]=i;
//		}
		
//		{54,3,18,18,18}
//		int[] policyFormat = {54,3,18,18,18};
//		int[] userAttrs = {1,1+18,1+18+18};
//		int[] policy = new int[policyFormat[0]];
//		for(int i=0;i<policyFormat[0];i++) {
//			policy[i]=i;
//		}
		
//		{81,3,27,27,27}
//		int[] policyFormat = {81,3,27,27,27};
//		int[] userAttrs = {1,1+27,1+27+27};
//		int[] policy = new int[policyFormat[0]];
//		for(int i=0;i<policyFormat[0];i++) {
//			policy[i]=i;
//		}
		
//		{81,9,9,...,9}
//		int[] policyFormat = new int[11];
//		policyFormat[0] = 81;
//		policyFormat[1] = 9;
//		int[] userAttrs  = new int[9];
//		for(int i=0;i<9;i++) {
//			policyFormat[i+2]=9;
//			userAttrs[i]=i*9;
//		}
//		int[] policy = new int[81];
//		for(int i=0;i<81;i++) {
//			policy[i]=i;
//		}
		
//		{81,27,3,...,3}
		int[] policyFormat = new int[29];
		policyFormat[0] = 81;
		policyFormat[1] = 27;
		int[] userAttrs  = new int[27];
		for(int i=0;i<27;i++) {
			policyFormat[i+2]=3;
			userAttrs[i]=i*3;
		}
		int[] policy = new int[81];
		for(int i=0;i<81;i++) {
			policy[i]=i;
		}
		
		long t1,t2;
		
		BigInteger M1 = new BigInteger("2334273489724922");
		
		//server setup
		Server server = new Server(maxAttrNumb);
		Element[] mpk = server.getMpk();
		Element[] csk = server.getCsk();
		
		//generate key sk
		Element[] vectorX = server.genVectorX(userAttrs,policyFormat);
		Element[] sk = server.getSk(vectorX);

		//organization pre-encrypt
		Organization org = new Organization(mpk,policyFormat);
		Element [] vectorY = server.genVectorY(policy,policyFormat);
		Element [] PC = org.preEncrypt(M1,vectorY);
		t1 = System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			org.preEncrypt(M1,vectorY);
		}
		t2 = System.currentTimeMillis();
		System.out.println("--- preenc ---");
		System.out.println(t2-t1);	
		
		//edge device co-encrypt
		EdgeDevice edge = new EdgeDevice(mpk,csk);
		Element [] CC = edge.coEncrypt(vectorY,PC);
		//test performance
		t1 = System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			edge.coEncrypt(vectorY,PC);
		}
		t2 = System.currentTimeMillis();
		System.out.println("--- coenc ---");
		System.out.println(t2-t1);	
		
		//user decrypt
		User user = new User(mpk,sk);
		BigInteger M2 = user.decrypt(CC,vectorY);
		t1 = System.currentTimeMillis();
		for(int i=0;i<100;i++) {
			user.decrypt(CC,vectorY);
		}
		t2 = System.currentTimeMillis();
		System.out.println("--- dec ---");
		System.out.println(t2-t1);	
		
		
		//print result
		System.out.println("----------------");
		System.out.println("M1");
		System.out.println("----------------");
		System.out.println(M1);
		System.out.println("----------------");
		System.out.println("M2");
		System.out.println("----------------");
		System.out.println(M2);
	}
	
	static void CoABE() {
		int maxAttrNumb = 50;
		
		int[] policyFormat = {9,3,3,3,3};
		int[] policy = {0,1,2,3,4,5,6,7,8};
		int[] userAttrs = {1,4,10};
		
		BigInteger M1 = new BigInteger("2334273489724922");
		
		//server setup
		Server server = new Server(maxAttrNumb);
		Element[] mpk = server.getMpk();
		Element[] csk = server.getCsk();
		
		//generate key sk
		Element[] vectorX = server.genVectorX(userAttrs,policyFormat);
		Element[] sk = server.getSk(vectorX);

		//organization pre-encrypt
		Organization org = new Organization(mpk,policyFormat);
		Element [] vectorY = server.genVectorY(policy,policyFormat);
		Element [] PC = org.preEncrypt(M1,vectorY);
		
		//edge device co-encrypt
		EdgeDevice edge = new EdgeDevice(mpk,csk);
		Element [] CC = edge.coEncrypt(vectorY,PC);

		//user decrypt
		User user = new User(mpk,sk);
		BigInteger M2 = user.decrypt(CC,vectorY);
		
		//print result
		System.out.println("----------------");
		System.out.println("M1");
		System.out.println("----------------");
		System.out.println(M1);
		System.out.println("----------------");
		System.out.println("M2");
		System.out.println("----------------");
		System.out.println(M2);
	}

}
