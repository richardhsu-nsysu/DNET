import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import java.util.Random;
import java.math.BigInteger;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.Base64;

public class PrivateKey {

	//pairing prameters
	private String 		mCurveFilePath	= null;
	private Pairing 	mPairing 		= null;
	private Field 		mZr 			= null;
	private Field		mG1				= null;
	private Field		mG2				= null;
	private Field		mGT				= null;
	
	//master secret key
	private Element[] msk               = null;
  	private Element barA 				= null; 			//e(g,g)^{\alpha}
	private Element alpha				= null;				//\alpha
	private Element alphap				= null;				//\alpha'
	private Element betap				= null;				//\beta'
	private Element delta				= null;				//\delta

	//master public key										
	private Element g 					= null;				//g
	private Element tildeA 			= null;				//\tilde{A}
	private Element h_0 				= null;				//h_0
	private Element h						= null;				//h
	private Element u						= null;				//u
	private Element v 					= null; 			//v
	private Element u_ap				= null;				//u^{\alpha'}
	private Element v_bp				= null;				//v^{\beta'}
	private Element Delta				= null;				//\Delta				
	private Element[] vecH			= null;				//\vec{H}
															
	//cooperative secret key													
	private Element xp					= null;				//x'
	private Element	yp					= null;				//y'

	//number of attributes
	private int mNumAttributeType 		= 50;				//the number of attribute type is set to 50 as default value
	
    public PrivateKey (String curve_file_path) {
		mCurveFilePath = curve_file_path;
    	mPairing = PairingFactory.getPairing(mCurveFilePath);
        mZr = mPairing.getZr();
        mG1 = mPairing.getG1();
        mG2 = mPairing.getG2();
        mGT = mPairing.getGT();

		barA = 	 mG1.newElement();
		alpha =  mZr.newElement();
		alphap = mZr.newElement();
		betap =  mZr.newElement();
		delta =  mZr.newElement();

		g 		= mG1.newElement();
		tildeA 	= mGT.newElement();
		h_0 	= mG1.newElement();
	 	h		= mG1.newElement();
		u 		= mG1.newElement();
		v 		= mG1.newElement();
		u_ap    = mG1.newElement();
		v_bp	= mG1.newElement();
		Delta 	= mG1.newElement();

		xp = mZr.newElement();
		yp = mZr.newElement();
    }

	public void setMsk (String [] strs) {
		byte[][] decoded_byte = new byte[strs.length][];
		for(int i=0; i<strs.length; i++){decoded_byte[i] = Base64.getDecoder().decode(strs[i]);} 
		int i = 0;
		barA.setFromBytes(decoded_byte[i]); i++;
		alpha.setFromBytes(decoded_byte[i]); i++;
		alphap.setFromBytes(decoded_byte[i]); i++;
		betap.setFromBytes(decoded_byte[i]); i++;
		delta.setFromBytes(decoded_byte[i]); i++;
	}

	public void setMpk (String [] strs) {
		byte[][] decoded_byte = new byte[strs.length][];
		for(int i=0; i<strs.length; i++){
			decoded_byte[i] = Base64.getDecoder().decode(strs[i]);
		} 
		int i = 0;
		g.setFromBytes(decoded_byte[i]); i++;
		tildeA.setFromBytes(decoded_byte[i]); i++;
		h_0.setFromBytes(decoded_byte[i]); i++;
	 	h.setFromBytes(decoded_byte[i]); i++;
		u.setFromBytes(decoded_byte[i]); i++;
		v.setFromBytes(decoded_byte[i]); i++;
		u_ap.setFromBytes(decoded_byte[i]); i++;
		v_bp.setFromBytes(decoded_byte[i]); i++;
		Delta.setFromBytes(decoded_byte[i]); i++;
		vecH = new Element[strs.length-9];
		for (int j = 0; j < strs.length-9; j++) {
			vecH[j] = mG1.newElement();
			vecH[j].setFromBytes(decoded_byte[j+9]);
		}
	}

	public void setCsk (String [] strs) {
		xp = mZr.newElement();
		xp.setFromBytes(Base64.getDecoder().decode(strs[0].getBytes()));
		yp = mZr.newElement();
		yp.setFromBytes(Base64.getDecoder().decode(strs[1].getBytes()));
	}
	
	public Element[] genVector(String[] attr_set){
		int attr_len 	= attr_set.length;
		Element [] vector = new Element[attr_len*4];
		for (int i=0; i < attr_len; i++) {
			int j = i*4;
			vector[j] = mZr.newElement().set(1);
			vector[j+1] = mZr.newElement().set(new BigInteger(attr_set[i].getBytes()));
			vector[j+2] = vector[j+1].getImmutable().mul(vector[j+1].getImmutable());
			vector[j+3] = vector[j+2].getImmutable().mul(vector[j+1].getImmutable());
		}
		return vector;
	}

	public Element[] genKey(Element[] vector) {

		int sk_len 	= vector.length + 2;
		Element[] sk 	= new Element[sk_len];
		Element r     = mZr.newRandomElement();
		//set sk[0],sk[1]
		Element D1 		= ((g.getImmutable().powZn(alpha.getImmutable())).mul(h_0.getImmutable().powZn(r.getImmutable())) ).mul( h.getImmutable().powZn( (alphap.getImmutable().add(betap.getImmutable())).getImmutable().mul(delta.getImmutable()) ) );
		Element D2 		= g.getImmutable().powZn(r.getImmutable());
		sk[0] = D1;
		sk[1] = D2;
		//set sk[2~]
		for (int i=2; i < sk_len; i++) {
			sk[i] = (vecH[0].getImmutable().powZn( (vector[i-2].getImmutable().negate()).div(vector[0].getImmutable()) ).mul(vecH[i-2].getImmutable()) ).powZn(r.getImmutable());
		}
		return sk;
	}

	public BigInteger innerProduct(Element[] a,Element[] b){
		Element sum = mZr.newElement();
		sum.set(0);
		for(int i=0;i<a.length && i<a.length;i++){
			Element t = a[i].getImmutable().mul(b[i].getImmutable());
			sum = sum.getImmutable().add(t.getImmutable());
		}
		return sum.toBigInteger();
	}

	public Element[] stringsToElements(String[] s){
		Element[] e = new Element[s.length];
		for (int i=0; i < s.length; i++) {
			byte[] b = Base64.getDecoder().decode(s[i]);
			e[i] = mZr.newElement();
			e[i].setFromBytes(b);
		}
		return e;
	}

	public String elementsToString(Element[] e){
		String S = "";
		for (int i=0; i < e.length; i++) {
			S = S + Base64.getEncoder().encodeToString(e[i].toBytes()) + "\n";
		}
		return S;
	}

	public String[] readFromFile(String filename,int number) {
		System.out.println("reading from " + filename + " ...");
		String[] strs = new String[number];
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			for(int i=0;i<number;i++){
				strs[i] = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strs;
	}

	public static void writeToFile(String filename,String content) {
		System.out.println("writing to " + filename + " ...");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
				bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//*************************************************************************************************
	//PreEnc is performed by the encryptor itself
	//return element[] PC: PC[0] = C_1, PC[1] = C_2, PC[2] = C_3, PC[3] = e_1, PC[4] = e_2
	//*************************************************************************************************
	public Element[] PreEnc(Element[] policy_set,String StrM) {
		Element M = mGT.newElement();
		M.set(new BigInteger(StrM.getBytes()));
		Element s = mZr.newRandomElement();
		Element tmp = h_0.getImmutable();
		//(h_0xh_1^{y_1}x...xh_n^{y_n})^s
		for (int i=0;i<policy_set.length;i++) {
			tmp = tmp.getImmutable().mul( vecH[i].getImmutable().powZn(policy_set[i].getImmutable()) );
		}
		Element[] PC = new Element[5];
		PC[0] = g.getImmutable().powZn(s.getImmutable()); 	//C_1 = g^s																						
		PC[1] = tmp.getImmutable().powZn(s.getImmutable()); //C_2																				
		PC[2] = M.getImmutable().mul(mPairing.pairing(g.getImmutable(),g.getImmutable()).getImmutable().powZn( alpha.getImmutable().mul(s.getImmutable()))); //C_3
		PC[3] = ( u.getImmutable().powZn(alphap.getImmutable()) ).getImmutable().powZn(s.getImmutable()); //e_1
		PC[4] = ( v.getImmutable().powZn(betap.getImmutable()) ).getImmutable().powZn(s.getImmutable());//e_2
		
		return PC;
	}
	
	
	//*************************************************************************************************
	//CoEnc is performed by the cooperator device
	//return element[] C: C[0] = C_1', C[1] = C_2', C[2] = C_3', C[3] = e_1', C[4] = e_2'
	//*************************************************************************************************
	public Element[] CoEnc(Element[] PC, Element[] policy_set) {
		Element[] C = new Element[5];
		Element sp  = mZr.newRandomElement();	//select s' at random
		C[0] = PC[0].getImmutable().mul( g.getImmutable().powZn(sp.getImmutable()) );
		
		//(h_0xh_1^{y_1}x...xh_n^{y_n})^s'
		Element tmp = h_0.getImmutable();
		for (int i=0;i<policy_set.length;i++) {
			tmp = tmp.getImmutable().mul( vecH[i].getImmutable().powZn(policy_set[i].getImmutable()) );
		}
		C[1] = PC[1].getImmutable().mul( tmp.getImmutable().powZn(sp.getImmutable()) );
		C[2] = PC[2].getImmutable().mul( mPairing.pairing(g.getImmutable(),g.getImmutable()).getImmutable().powZn( alpha.getImmutable().mul(sp.getImmutable()) ) );
		C[3] = ( PC[3].getImmutable().mul( 
			( u.getImmutable().powZn(alphap.getImmutable()) ).getImmutable().powZn(sp.getImmutable()) ) ).getImmutable().powZn(xp.getImmutable());
		C[4] = ( PC[4].getImmutable().mul( 
			( v.getImmutable().powZn(betap.getImmutable()) ).getImmutable().powZn(sp.getImmutable()) ) ).getImmutable().powZn(yp.getImmutable());
		
		return C;
	}
	
	//*************************************************************************************************
	//Dec is to decrypt the given ciphertext
	//input sk: element sk[0] = D_1, sk[1] = D_2, sk[2~1+n] = K[0~n-1], 
	//          element[] C: C[0] = C_1', C[1] = C_2', C[2] = C_3', C[3] = e_1', C[4] = e_2'
	//			element[] policy_set = y_1,....,y_n
	//return M
	//*************************************************************************************************
	public Element Dec(Element[] sk, Element[] C, Element[] policy_set) {
		//D1
		Element tmp = sk[0].getImmutable();
		//D1 * K2^y2 ~ Kn^yn
		for (int i=0; i < policy_set.length-1; i++) {
			tmp = tmp.getImmutable().mul( sk[i+3].getImmutable().powZn(policy_set[i+1].getImmutable()) );
		}
		//e(C1p * D1 * K2^y2 ~ Kn^yn)
		Element part_1 = mPairing.pairing(C[0].getImmutable(), tmp.getImmutable());
		//e(C2p, D2) * e(Delta, e1p, e2p)
		Element part_2 = mPairing.pairing(C[1].getImmutable(),sk[1].getImmutable()).mul(mPairing.pairing(Delta.getImmutable(), C[3].getImmutable().mul(C[4].getImmutable())) );
		Element Gamma = part_1.getImmutable().div(part_2.getImmutable());
		Element M = C[2].getImmutable().div(Gamma.getImmutable());
		return M;
	}

	public static void main (String[] args) {
		System.out.println("----------------------");
		System.out.println("start generate private key ...");
		System.out.println("----------------------");
		PrivateKey privateKey = new PrivateKey("curves/a.properties");
		privateKey.setMpk(privateKey.readFromFile("mpk.txt",9+50));
		privateKey.setMsk(privateKey.readFromFile("msk.txt",5));
		privateKey.setCsk(privateKey.readFromFile("csk.txt",2));
		//print privateKey
		Element[] attrVector = privateKey.genVector(args);
		Element[] sk= privateKey.genKey(attrVector);
		String output = privateKey.elementsToString(sk);
		writeToFile("PrivateKey.txt",output);
		//check inner product
		System.out.println("----------------------");
		System.out.println("start testing ...");
		System.out.println("----------------------");
		String[] policyStrings = privateKey.readFromFile("PolicyVector.txt",12);
		Element[] policyVector = privateKey.stringsToElements(policyStrings);
		System.out.println("inner product of attr vector and policy vector : "+privateKey.innerProduct(attrVector,policyVector));
		//check enc and dec
		System.out.println("Message : " + new BigInteger("ABCDE".getBytes()));
		Element[] PC = privateKey.PreEnc(policyVector,"ABCDE");
		Element[] C = privateKey.CoEnc(PC,policyVector);
		Element M   = privateKey.Dec(sk,C,policyVector);
		System.out.println("Decrypted Message : " + M.toBigInteger());
	}
}