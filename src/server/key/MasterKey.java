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

public class MasterKey {

	private String 		mCurveFilePath	= null;
	private Pairing 	mPairing 		= null;
	private Field 		mZr 			= null;
	private Field		mG1				= null;
	private Field		mG2				= null;
	private Field		mGT				= null;
	
	private Element[] mMasterSecret 	= new Element[5];	//mMasterSecret[0] = \bar{A}=g^{\alpha}
															//mMasterSecret[1] = \alpha
															//mMasterSecret[2] = \alpha'
															//mMasterSecret[3] = \beta'
															//mMasterSecret[4] = \delta
    private Element barA 				= null; 			//e(g,g)^{\alpha}
	private Element alpha				= null;				//\alpha
	private Element alphap				= null;				//\alpha'
	private Element betap				= null;				//\beta'
	private Element delta				= null;				//\delta
	
															
	private Element[] mPublicParameters = null; 			//mPublicParameters[0] = g
															//mPublicParameters[1] = \tilde{A}=e(g,g)^{\alpha}
															//mPublicParameters[2] = h_0
															//mPublicParameters[3] = h
															//mPublicParameters[4] = u
															//mPublicParameters[5] = v
															//mPublicParameters[6] = u^{\alpha'}
															//mPublicParameters[7] = v^{\beta'} 
															//mPublicParameters[8] = \Delta=g^{\delta} 
															//mPublicParameters[9]~[8+mNumAttributeType] = \vec{H}
	private Element g 					= null;				//g
	private Element tildeA 				= null;				//\tilde{A}
	private Element h_0 				= null;				//h_0
	private Element h					= null;				//h
	private Element u					= null;				//u
	private Element v 					= null; 			//v
	private Element u_ap				= null;				//u^{\alpha'}
	private Element v_bp				= null;				//v^{\beta'}
	private Element Delta				= null;				//\Delta				
	private Element[] vecH				= null;				//\vec{H}
															
	private int mNumAttributeType 		= 50;				//the number of attribute type is set to 50 as default value
																				
	private Element[] 	mCoSecret 		= new Element[2];	//mCoSecret[0] = x'
															//mCoSecret[1] = y'
	private Element xp					= null;				//x'
	private Element	yp					= null;				//y'
	
    public MasterKey (int mode, String curve_file_path) {
		mCurveFilePath = curve_file_path;
    	genPairingParameters();
    }
	
	//*************************************************************************************************
	//setup method is to generate the required public parameters and the corresponding
	//master secret keys for the CORE system
	//for system adminstrator only
	//*************************************************************************************************
	public boolean setup (int sec_param) {
		alpha 	= mZr.newRandomElement();	//\alpha
		alphap 	= mZr.newRandomElement();	//\alpha'
		betap 	= mZr.newRandomElement();	//\beta'
		delta 	= mZr.newRandomElement();	//delta
		
		xp		= mZr.newRandomElement();	//x'
		yp		= mZr.newRandomElement();	//y'
		
		g 		= mG1.newRandomElement();	//g
		barA 	= g.getImmutable().powZn(alpha.getImmutable()); //\bar{A} = g^{\alpha}
		//\tilde{A}
		tildeA 	= mPairing.pairing(g.getImmutable(),g.getImmutable()).getImmutable().powZn(alpha.getImmutable());
		h_0		= mG1.newRandomElement();
		Element[] vec_alpha = new Element[mNumAttributeType];
		vecH				= new Element[mNumAttributeType];
		
		
		for (int i = 0; i < mNumAttributeType; i++) {
			vec_alpha[i] = mZr.newRandomElement();
			vecH[i] = g.getImmutable().powZn(vec_alpha[i].getImmutable());
		}
		
		h = mG1.newRandomElement();
		//generate u and v such that u^x'=v^y'=h
		u = h.getImmutable().powZn(xp.getImmutable().invert());
		//u = 1;
		v = h.getImmutable().powZn(yp.getImmutable().invert());
		
		u_ap = u.getImmutable().powZn(alphap.getImmutable()); 	//u^{\alpha}'
		v_bp = v.getImmutable().powZn(betap.getImmutable());	//v^{\beta'}
		Delta = g.getImmutable().powZn(delta.getImmutable());
		
		
		//;mPublicParameters = pub_params;
		mPublicParameters 	 = new Element[9+mNumAttributeType];
		mPublicParameters[0] = g 		; //mG1
		mPublicParameters[1] = tildeA 	; //mGT
		mPublicParameters[2] = h_0 	    ; //mG1
	 	mPublicParameters[3] = h		; //mG1
	    mPublicParameters[4] = u 		; //mG1
		mPublicParameters[5] = v 		; //mG1
		mPublicParameters[6] = u_ap     ; //mG1
		mPublicParameters[7] = v_bp	    ; //mG1
		mPublicParameters[8] = Delta 	; //mG1

		
		for (int i = 0; i < mNumAttributeType; i++) {
			mPublicParameters[9+i] = vecH[i]; //mG1
		}
			
		mMasterSecret[0] = barA 	; //mG1
		mMasterSecret[1] = alpha	; //mZr
		mMasterSecret[2] = alphap   ; //mZr
		mMasterSecret[3] = betap 	; //mZr
		mMasterSecret[4] = delta	; //mZr
		
		mCoSecret[0] = xp; //mZr
		mCoSecret[1] = yp; //mZr
		return true;
	}

    //*************************************************************************************************
	// getNumAttributeType(): return int, get number of attribute type
	//*************************************************************************************************
	public int getNumAttributeType() {
		return mNumAttributeType;
	}

	//*************************************************************************************************
	// getPubParam(): return Element[], get public parameters of the COABE system
	//*************************************************************************************************
	public Element[] getPubParam() {
		return mPublicParameters;
	}
	
	//*************************************************************************************************
	// getMasterSecret(): return Element[], get master secret of the COABE system
	//*************************************************************************************************
	public Element[] getMasterSecret() {
		return mMasterSecret;
	}
	
	//*************************************************************************************************
	// getCoSecret(): return Element[], get cooperative secret of the COABE system
	//*************************************************************************************************
	public Element[] getCoSecret() {
		return mCoSecret;
	}
	
	//*************************************************************************************************
	//generate the required parameters for pairing operations
	//*************************************************************************************************
	public void genPairingParameters() {
        mPairing = PairingFactory.getPairing(mCurveFilePath);
        mZr = mPairing.getZr();
        mG1 = mPairing.getG1();
        mG2 = mPairing.getG2();
        mGT = mPairing.getGT();
    }
	
	//*************************************************************************************************
	// generate new random element
	// input type: 0 - G1 element, 1 - G2 element, 2 - GT element
	// output element
	//*************************************************************************************************
	public Element newRandomElement(int type) {
		if (type == 0) {
			return mG1.newRandomElement();
		}
		else
		if (type == 1) {
			return mG2.newRandomElement();
		}
		else 
		if (type == 2) {
			return mGT.newRandomElement();
		}
		return null;
	}

	//****************************************
	// write key data to file
	//****************************************
	public static void writeToFile(String filename,String content) {
		System.out.println("writing to " + filename + " ...");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
				bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//****************************************
	// main function for generate key
	//****************************************
	public static void main (String[] args) {

		String curve_file_path = "curves/a.properties";
		MasterKey mastrerKey = new MasterKey (0,curve_file_path);
        mastrerKey.setup(512);
        
        /****** Generate Public paramters**********/
        Element[] public_param = mastrerKey.getPubParam();
        String public_key = "";
        String public_str = "";
        int attrNum = mastrerKey.getNumAttributeType();
        for(int l=0;l<9+attrNum;l++){
            Element element = public_param[l];
			System.out.println(element.getField());
            public_str = Base64.getEncoder().encodeToString(element.toBytes());
            public_key = public_key + public_str + "\n";
        }
        writeToFile("mpk.txt",public_key);
        
        /****** Generate Master Key**********/
        Element[] master_secret = mastrerKey.getMasterSecret();
        String private_key = "";
        String private_str = "";
        for(int m = 0; m <5;m++){
            Element element = master_secret[m];
			System.out.println(element.getField());
            private_str = Base64.getEncoder().encodeToString(element.toBytes());
            private_key = private_key + private_str + "\n";
        }
		writeToFile("msk.txt",private_key);

        /****** Generate Cooperative Key**********/
		Element[] co_secret = mastrerKey.getCoSecret();
        String co_key = "";
        String co_str = "";
        for(int m = 0; m <2;m++){
            Element element = co_secret[m];
			System.out.println(element.getField());
            co_str = Base64.getEncoder().encodeToString(element.toBytes());
            co_key = co_key + co_str + "\n";
        }
		writeToFile("csk.txt",co_key);
        
	}
}
