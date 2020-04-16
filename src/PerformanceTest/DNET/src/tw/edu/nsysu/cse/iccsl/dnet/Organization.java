package tw.edu.nsysu.cse.iccsl.dnet;

import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class Organization {
	
	
	private static Pairing pairing		= null;
	@SuppressWarnings("rawtypes")
	private static Field zr				= null;	
	@SuppressWarnings("rawtypes")
	private static Field gt				= null;
	
	//master public key
	private Element g 					= null;				//g
	private Element tiledA 				= null;				//e(g,g)^{alpha}
	private Element h_0 				= null;				//h_0
	private Element u_ap				= null;				//u^{\alpha'}
	private Element v_bp				= null;				//v^{\beta'}			
	private Element[] vecH				= null;				//\vec{H}	
	
	private int [] policyFormat 		= null;
	private int attrNum					= 0;
	private Element[] attributes        = null; 

	public Organization(Element[] mpk, int[] pf) {
		
		pairing = PairingFactory.getPairing("curves/a.properties");
		zr = pairing.getZr();
		gt = pairing.getGT();
		
  		g = mpk[0];
  		tiledA = mpk[1];
  		h_0 = mpk[3];
  		u_ap = mpk[4];
  		v_bp = mpk[5];
  		vecH = new Element[mpk.length-7];
  		for(int i=7; i<mpk.length; i++) {
  			vecH[i-7]=mpk[i];
  		}
  		
  		policyFormat = pf;
  		attrNum = pf[0];
  		
  		//set attributes
  		attributes 	= new Element[attrNum];
  		for (int i = 0; i < attrNum; i++) {
  			attributes[i] = zr.newRandomElement();
  		}
	}

	public Element[] preEncrypt(BigInteger m, Element[] vectorY) {
		Element s = zr.newRandomElement();
		Element[] PC = new Element[5];
		Element M = gt.newElement(m);
		
		PC[0] = g.getImmutable().powZn(s); 								//C_1 = g^s
		Element tmp = h_0.getImmutable();								//C_2 = (h_0xh_1^{y_1}x...xh_n^{y_n})^s
		for (int i=0;i<vectorY.length;i++) {
			tmp.mul(vecH[i].getImmutable().powZn(vectorY[i]));
		}
		PC[1] = tmp.getImmutable().powZn(s); 
		PC[2] = M.getImmutable().mul(tiledA.getImmutable().powZn(s)); 	//C_3
		PC[3] = u_ap.getImmutable().powZn(s); 							//e_1
		PC[4] = v_bp.getImmutable().powZn(s); 							//e_2
		
		return PC;
	}

	public Element[] genVectorY(int[] policy) {
		int typeNum = policyFormat[1];
		Element [] Y = new Element[0];
		for(int i=0,j=0; i<typeNum && j<attrNum;i++) {
			int thisTypeValNum = policyFormat[i+2];
			PBCPolynomial p = new PBCPolynomial();
			for(int k=0;k<thisTypeValNum;k++,j++) {
				boolean match = false;
				for(int l=0;l<policy.length;l++) {
					if(policy[l]==j) {
						match = true;
						break;
					}
				}
				if(match) {
					Element[]e = new Element[2];
					e[0] = zr.newOneElement();
					e[1] = attributes[j].getImmutable().negate();
					p.mul(e);
				}
			}
			Element[] A = Y;
			Element[] B = p.getPolynomial(thisTypeValNum);
			Y = new Element[A.length+B.length];
			for(int l=0;l<A.length;l++) {
				Y[l] = A[l];
			}
			for(int l=0,m=A.length;l<B.length;l++,m++) {
				Y[m] = B[l];
			}
		}
//		System.out.println("----------------");
//		System.out.println("vector Y");
//		System.out.println("----------------");
//		for(int i=0;i<Y.length;i++) {
//			System.out.println(Y[i]);
//		}
		return Y;
	}

	public Element[] genVectorX(int[] userAttrs) {
//		System.out.println("----------------");
//		System.out.println("vector X");
//		System.out.println("----------------");
		int typeNum = policyFormat[1];
		Element[] X = new Element[typeNum+attrNum];
		for(int i=0,j=0; i<typeNum && j<attrNum;i++) {
			int thisTypeValNum = policyFormat[i+2];
			for(int k=thisTypeValNum;k>=0;k--,j++) {
				X[j]=attributes[userAttrs[i]].getImmutable().pow(BigInteger.valueOf(k));
			}
		}
//		for(int i=0;i<X.length;i++) {
//			System.out.println(X[i]);
//		}
		return X;
	}
	
	

	public Element getRamdomZr() {
		Element z = zr.newRandomElement();
		return z;
	}

}
