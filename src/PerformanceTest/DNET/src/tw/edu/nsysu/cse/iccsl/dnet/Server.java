package tw.edu.nsysu.cse.iccsl.dnet;

import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class Server {
	
	private static Pairing pairing		= null;
	@SuppressWarnings("rawtypes")
	private static Field zr				= null;		
	@SuppressWarnings("rawtypes")
	private static Field g1				= null;
	private static Field gt				= null;
	//master secret key
	private Element [] msk 				= null;
  	private Element barA 				= null; 			//g^{\alpha}
	private Element alpha				= null;				//\alpha
	private Element alphap				= null;				//\alpha'
	private Element betap				= null;				//\beta'
	private Element delta				= null;				//\delta
	//master public key
	private Element [] mpk				= null;
	private Element g 					= null;				//g
	private Element tiledA 				= null;				//e(g,g)^{alpha}
	private Element h					= null;
	private Element h_0 				= null;				//h_0
	private Element u_ap				= null;				//u^{\alpha'}
	private Element v_bp				= null;				//v^{\beta'}
	private Element Delta				= null;				//\Delta				
	private Element[] vecH				= null;				//\vec{H}													
	//cooperative secret key											
	private Element [] csk				= null;
	private Element xp					= null;				//x'
	private Element	yp					= null;				//y'
	
	private int maxAttrNum 				= 50;
	private Element[] attributes        = null; 
	
	//test
	
	private Element r;
	
	Server (int maxAttrNum) {
		
		pairing = PairingFactory.getPairing("curves/a.properties");
		zr = pairing.getZr();
        g1 = pairing.getG1();
        gt = pairing.getGT();
        
    	//set master secret key
  		alpha =  zr.newRandomElement();
  		alphap = zr.newRandomElement();
  		betap =  zr.newRandomElement();
  		delta =  zr.newRandomElement();
  		
  		//set secret key	
  		xp = zr.newRandomElement();
  		yp = zr.newRandomElement();
  		csk = new Element[2];
  		csk[0] = xp;
  		csk[1] = yp;
  		
  		//set public key
  		g 		= g1.newRandomElement();
  		tiledA 	= pairing.pairing(g,g).powZn(alpha);
  		h_0 	= g1.newRandomElement();
  		vecH 	= new Element[maxAttrNum];
  		for (int i = 0; i < maxAttrNum; i++) {
  			Element a = zr.newRandomElement();
  			vecH[i] = g.getImmutable().powZn(a);
  		}
  		h	= g1.newRandomElement();
  		Element u = h.getImmutable().powZn(xp.getImmutable().invert());
  		Element v = h.getImmutable().powZn(yp.getImmutable().invert());
  		u_ap = u.getImmutable().powZn(alphap.getImmutable()); 	//u^{\alpha}'
  		v_bp = v.getImmutable().powZn(betap.getImmutable());	//v^{\beta'}
  		Delta = g.getImmutable().powZn(delta.getImmutable());
  		mpk = new Element[7+maxAttrNum];
  		mpk[0] = g;
  		mpk[1] = tiledA;
  		mpk[2] = h;
  		mpk[3] = h_0;
  		mpk[4] = u_ap;
  		mpk[5] = v_bp;
  		mpk[6] = Delta;
  		for(int i=0; i<maxAttrNum; i++) {
  			mpk[i+7] = vecH[i];
  		}
  		
  		//set master secret key
  		barA = g.getImmutable().powZn(alpha);
  		Element [] msk = new Element[4];
  		msk[0] = alpha;
  		msk[1] = alphap;
  		msk[2] = betap;
  		msk[3] = delta;
  		
  		//test
  		attributes 	= new Element[maxAttrNum];
  		for (int i = 0; i < maxAttrNum; i++) {
  			attributes[i] = zr.newRandomElement();
  		}
  		
    }
	

	Element[] getCsk() {
		
		return csk;
	}


	Element[] getMpk() {
		return mpk;
	}


	Element[] getSk(Element[] vector) {
		int sk_len 	= vector.length + 1;
		Element[] sk 	= new Element[sk_len];
		r     = zr.newRandomElement();
		//set sk[0], sk[1]
		sk[0] = barA.getImmutable().mul(h_0.getImmutable().powZn(r)).mul(h.getImmutable().powZn( alphap.getImmutable().add(betap).mul(delta)));
		sk[1] = g.getImmutable().powZn(r.getImmutable());
		//set sk[2~]
		for (int i=1; i < vector.length; i++) {
			sk[i+1] = vecH[0].getImmutable().powZn(vector[i].getImmutable().negate().div(vector[0])).mul(vecH[i].getImmutable()).powZn(r);
			
		}
		return sk;
	}
	
	// test
	// test
	// test
	// test
	// test
	// test
	// test
	// test
	// test


	public Element[] genVectorX(int[] userAttrs, int[] policyFormat) {
		int typeNum = policyFormat[1];
		int attrNum = policyFormat[0];
		Element[] X = new Element[typeNum+attrNum];
		for(int i=0,j=0; i<typeNum;i++) {
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


	public Element[] genVectorY(int[] policy, int[] policyFormat) {
		int typeNum = policyFormat[1];
		int attrNum = policyFormat[0];
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


	public Element[] preEncrypt(BigInteger m, Element[] vectorY, Element s) {
		Element[] PC = new Element[5];
		Element M = gt.newElement(m);
		
		PC[0] = g.getImmutable().powZn(s); 								//C_1 = g^s
		Element tmp = h_0.getImmutable();												//C_2 = (h_0xh_1^{y_1}x...xh_n^{y_n})^s
		for (int i=0;i<vectorY.length;i++) {
			tmp.mul(vecH[i].getImmutable().powZn(vectorY[i]));
		}
		PC[1] = tmp.getImmutable().powZn(s); 
		PC[2] = M.getImmutable().mul(tiledA.getImmutable().powZn(s)); 	//C_3
		PC[3] = u_ap.getImmutable().powZn(s); 							//e_1
		PC[4] = v_bp.getImmutable().powZn(s); 							//e_2
		
		return PC;
	}


	public void innerProduct(Element[] X, Element[] Y) {
		Element e = zr.newZeroElement();
		for(int i=0;i<X.length && i<Y.length;i++) {
			e.add(X[i].getImmutable().mul(Y[i]));
		}

		System.out.println("----------------");
		System.out.println("X Y inner product");
		System.out.println("----------------");
		System.out.println(e);
	}


	public Element[] coEncrypt(Element[] vectorY, Element[] PC, Element sp) {
		Element[] C = new Element[5];
		C[0] = PC[0].getImmutable().mul( g.getImmutable().powZn(sp));
		//(h_0xh_1^{y_1}x...xh_n^{y_n})^s'
		Element tmp = h_0.getImmutable();
		for (int i=0;i<vectorY.length;i++) {
			tmp.mul( vecH[i].getImmutable().powZn(vectorY[i]) );
		}
		C[1] = PC[1].getImmutable().mul( tmp.getImmutable().powZn(sp) );
		C[2] = PC[2].getImmutable().mul( tiledA.getImmutable().powZn(sp));
		C[3] = PC[3].getImmutable().mul( u_ap.getImmutable().powZn(sp)).powZn(xp);
		C[4] = PC[4].getImmutable().mul( v_bp.getImmutable().powZn(sp)).powZn(yp);
		
		return C;
	}


	public BigInteger decrypt(Element[] C, Element[] VectorY,Element[] sk) {
		//D1
		Element tmp = sk[0].getImmutable();
		//D1 * K2^y2 ~ Kn^yn
		for (int i=0; i+2 < VectorY.length-1; i++) {
			tmp.mul( sk[i+2].getImmutable().powZn(VectorY[i+1]) );
		}
		//e(C1p * D1 * K2^y2 ~ Kn^yn)
		Element part_1 = pairing.pairing(C[0], tmp);
		//e(C2p, D2) * e(Delta, e1p, e2p)
		Element part_2 = pairing.pairing(C[1],sk[1]).mul(pairing.pairing(Delta, C[3].getImmutable().mul(C[4])) );
		Element Gamma = part_1.getImmutable().div(part_2);
		Element M = C[2].getImmutable().div(Gamma);
		
		
		return M.toBigInteger();
	}


	public Element[] getMsk() {
		return msk;
	}


	public Element getRandomZr() {
		return zr.newRandomElement();
	}

}
